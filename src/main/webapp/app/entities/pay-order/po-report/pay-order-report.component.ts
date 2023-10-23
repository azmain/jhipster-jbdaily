import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { IDealer } from 'app/entities/dealer/dealer.model';
import { DealerService } from 'app/entities/dealer/service/dealer.service';
import { IFertilizer } from 'app/entities/fertilizer/fertilizer.model';
import { FertilizerService } from 'app/entities/fertilizer/service/fertilizer.service';
import { Observable, combineLatest, map, switchMap, tap } from 'rxjs';
import { jsPDF } from 'jspdf';
import { IPayOrder } from '../pay-order.model';
import { FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter/filter.model';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { EntityArrayResponseType, PayOrderService } from '../service/pay-order.service';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { ASC, DEFAULT_SORT_DATA, SORT } from 'app/config/navigation.constants';

@Component({
  selector: 'jhi-pay-order-report',
  templateUrl: './pay-order-report.component.html',
  styleUrls: ['./pay-order-report.component.scss'],
})
export class PayOrderReportComponent implements OnInit {
  payOrderSearchForm: FormGroup;

  fertilizersSharedCollection: IFertilizer[] = [];
  dealersSharedCollection: IDealer[] = [];

  payOrders?: IPayOrder[] = [];
  payOrdersDataTable = [];
  isLoading = false;

  predicate = 'payOrderDate';
  ascending = false;
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE + 10;
  totalItems = 0;
  page = 1;

  constructor(
    private fb: FormBuilder,
    private dealerService: DealerService,
    private fertilizerService: FertilizerService,
    protected payOrderService: PayOrderService,
    protected activatedRoute: ActivatedRoute,
    public router: Router
  ) {
    this.payOrderSearchForm = this.fb.group({
      dealerId: [null],
      fertilizerId: [null],
      payOrderDateFrom: [null],
      payOrderDateTo: [null],
      payOrderNumber: [null],
      payOrderNumberFrom: [null],
      payOrderNumberTo: [null],
      controllingNo: [null],
      slipNo: [null],
    });
  }

  ngOnInit(): void {
    this.loadRelationshipsOptions();
  }

  onSearch() {
    // Handle payOrderSearchForm submission here
    console.log(this.payOrderSearchForm.value);
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  protected loadRelationshipsOptions(): void {
    this.fertilizerService
      .query()
      .pipe(
        map((res: HttpResponse<IDealer[]>) => {
          return res.body ? res.body.map(item => this.convertFertilizerOption(item)) : [];
        })
      )
      .subscribe((fertilizers: IFertilizer[]) => {
        this.fertilizersSharedCollection = fertilizers;
      });

    this.dealerService
      .query()
      .pipe(
        map((res: HttpResponse<IDealer[]>) => {
          return res.body ? res.body.map(item => this.convertDealerOption(item)) : [];
        })
      )
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));
  }

  convertFertilizerOption(fertilizer: IFertilizer): IDealer {
    return {
      id: fertilizer.id,
      name: fertilizer.name,
    };
  }
  convertDealerOption(dealer: IDealer): IDealer {
    return {
      id: dealer.id,
      name: dealer.name,
      shortName: dealer.shortName,
    };
  }

  exportPdf() {
    const doc = new jsPDF();

    // doc.auto
    // doc.save('a4.pdf');

    // import("jspdf").then(jsPDF => {
    //     import("jspdf-autotable").then(x => {
    //         const doc = new jsPDF.default(0,0);
    //         doc.autoTable(this.exportColumns, this.products);
    //         doc.save('products.pdf');
    //     })
    // })
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
    this.payOrders = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IPayOrder[] | null): IPayOrder[] {
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
      //   page: pageToLoad - 1,
      //   size: this.itemsPerPage,
      paged: false,
      eagerload: true,
      //   sort: this.getSortQueryParam(predicate, ascending),
    };
    filterOptions?.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });

    return this.payOrderService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }
}
