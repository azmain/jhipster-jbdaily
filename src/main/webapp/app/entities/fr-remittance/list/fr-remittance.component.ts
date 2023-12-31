import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFrRemittance } from '../fr-remittance.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, FrRemittanceService } from '../service/fr-remittance.service';
import { FrRemittanceDeleteDialogComponent } from '../delete/fr-remittance-delete-dialog.component';
import { FilterOptions, IFilterOptions, IFilterOption } from 'app/shared/filter/filter.model';

import { Converter, bnBD, enUS } from 'any-number-to-words';
const converter = new Converter(enUS);

import { font } from 'content/fonts/custom-font';
import pdfMake from 'pdfmake/build/pdfmake';
import pdfFonts from 'pdfmake/build/vfs_fonts';
import { TDocumentDefinitions } from 'pdfmake/interfaces';
import { EnglishToBanglaNumber } from 'app/helpers/english-to-bangla-number';
import dayjs from 'dayjs';
import { FrRemittancePdfService } from '../service/fr-remittance-pdf.service';

@Component({
  selector: 'jhi-fr-remittance',
  templateUrl: './fr-remittance.component.html',
})
export class FrRemittanceComponent implements OnInit {
  frRemittances?: IFrRemittance[];
  isLoading = false;

  predicate = 'lastModifiedDate';
  ascending = false;
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  searchFilter = null;

  constructor(
    protected frRemittanceService: FrRemittanceService,
    protected frRemittancePdfService: FrRemittancePdfService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: IFrRemittance): number => this.frRemittanceService.getFrRemittanceIdentifier(item);

  ngOnInit(): void {
    this.load();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.predicate, this.ascending, filterOptions));
  }

  delete(frRemittance: IFrRemittance): void {
    const modalRef = this.modalService.open(FrRemittanceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.frRemittance = frRemittance;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.searchFilter = null;
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending, this.filters.filterOptions);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending, this.filters.filterOptions);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending, this.filters.filterOptions))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.frRemittances = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IFrRemittance[] | null): IFrRemittance[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(
    page?: number,
    predicate?: string,
    ascending?: boolean,
    filterOptions?: IFilterOption[]
  ): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      eagerload: true,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    filterOptions?.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });

    if (this.searchFilter) {
      // queryObject['pin.contains'] = this.searchFilter;
      // queryObject['remitersName.contains'] = this.searchFilter;
      // queryObject['paymentDate.contains'] = this.searchFilter;
      // queryObject['transactionType.contains'] = this.searchFilter;
      // queryObject['recvMobileNo.contains'] = this.searchFilter;
      queryObject['recvName.contains'] = this.searchFilter;
      // queryObject['recvLegalId.contains'] = this.searchFilter;
      // queryObject['documentType.contains'] = this.searchFilter;
    }

    return this.frRemittanceService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    filterOptions?.forEach(filterOption => {
      queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
    });

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

  printFrIncentiveVoucher(frRemittance: IFrRemittance): void {
    let dd = this.frRemittancePdfService.convertDateFromServer(frRemittance);

    pdfMake.vfs = {
      ...pdfFonts.pdfMake.vfs,
      'Bangla-normal.ttf': font,
    };
    pdfMake.fonts = {
      Bangla: {
        normal: 'Bangla-normal.ttf',
        bold: 'Bangla-normal.ttf',
      },
      Roboto: {
        normal: 'Roboto-Regular.ttf',
        bold: 'Roboto-Medium.ttf',
        italics: 'Roboto-Italic.ttf',
        bolditalics: 'Roboto-MediumItalic.ttf',
      },
    };

    pdfMake.createPdf(dd).open();
  }

  search(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }
}
