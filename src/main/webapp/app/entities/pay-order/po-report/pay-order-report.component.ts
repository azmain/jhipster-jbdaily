import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { IDealer } from 'app/entities/dealer/dealer.model';
import { DealerService } from 'app/entities/dealer/service/dealer.service';
import { IFertilizer } from 'app/entities/fertilizer/fertilizer.model';
import { FertilizerService } from 'app/entities/fertilizer/service/fertilizer.service';
import { Observable, combineLatest, map, switchMap, tap } from 'rxjs';
import { IPayOrder } from '../pay-order.model';
import { FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter/filter.model';
import { ITEMS_FOR_DROPDOWN, ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { EntityArrayResponseType, PayOrderService } from '../service/pay-order.service';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { ASC, DEFAULT_SORT_DATA, DESC, SORT } from 'app/config/navigation.constants';
import dayjs from 'dayjs';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';

import { font } from 'content/fonts/custom-font';

import pdfMake from 'pdfmake/build/pdfmake';
import pdfFonts from 'pdfmake/build/vfs_fonts';
import { TDocumentDefinitions } from 'pdfmake/interfaces';
import { EnglishToBanglaNumber } from 'app/helpers/english-to-bangla-number';

pdfMake.vfs = pdfFonts.pdfMake.vfs;

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
  payOrdersDataTable: any[] = [];
  isLoading = false;

  tableColumns: string[] = [
    'Serial',
    'Dealer',
    'Fertilizer',
    'Pay To',
    'Slip No',
    'Pay Order Date',
    'Controlling No',
    'Pay Order No',
    'Amount',
  ];

  predicate = 'payOrderDate';
  ascending = false;
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE + 10;
  itemsToFetch = ITEMS_FOR_DROPDOWN;
  totalItems = 0;
  page = 1;
  amountTotal = 0;

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
    this.queryBackend(this.page, this.predicate, this.ascending).subscribe({
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

  export() {
    console.log(window.location.origin);

    let columns: any[] = [];
    let tableRows: any[] = [];

    // console.log(font);
    console.log(this.payOrdersDataTable);
    if (this.payOrdersDataTable.length > 0) {
      columns = Object.keys(this.payOrdersDataTable[0]);
      console.log(columns);
      tableRows = this.payOrdersDataTable.map((payOrder, i) => {
        return [
          payOrder[columns[0]] ?? 'N/A',
          payOrder[columns[1]] ?? 'N/A',
          payOrder[columns[2]] ?? 'N/A',
          payOrder[columns[3]] ?? 'N/A',
          payOrder[columns[4]] ?? 'N/A',
          payOrder[columns[5]] ?? 'N/A',
          payOrder[columns[6]] ?? 'N/A',
          payOrder[columns[7]] ?? 'N/A',
          { text: payOrder[columns[8]] ?? 'N/A', style: 'amountStyle' },
        ];
      });

      console.log(tableRows);
    } else {
    }

    let docDefinition: TDocumentDefinitions = {
      content: [
        {
          text: 'Pay Orders Printed On ' + dayjs().format('DD-MM-YYYY'),
          style: 'header',
        },
        {
          style: 'payOrderTable',
          table: {
            headerRows: 1,
            dontBreakRows: true,
            widths: 'auto',
            body: [
              this.tableColumns,
              ...tableRows,
              [
                {
                  text: 'Total',
                  colSpan: 8,
                  alignment: 'right',
                },
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {
                  text: this.amountTotal.toLocaleString('en-US', { useGrouping: true }),
                  alignment: 'right',
                },
              ],
            ],
          },
        },
      ],
      styles: {
        header: {
          alignment: 'center',
        },
        amountStyle: {
          alignment: 'right',
        },
      },
      defaultStyle: {
        font: 'Bangla',
        alignment: 'center',
      },
    };
    pdfMake.vfs = {
      ...pdfFonts.pdfMake.vfs,
      'Bangla-normal.ttf': font,
    };
    pdfMake.fonts = {
      Bangla: {
        normal: 'Bangla-normal.ttf',
      },
    };

    pdfMake.createPdf(docDefinition).open();
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.payOrders = dataFromBody;
    this.payOrdersDataTable = this.payOrders.map((item: any, index) => {
      return {
        serial: index + 1,
        dealer: item.dealer?.bnName + ', ' + item.dealer?.upazila?.bnName,
        fertilizer: item.fertilizer?.name,
        payTo: 'JFCL - ' + item.fertilizer?.name,
        slipNo: item.slipNo,
        payOrderDate: dayjs(item.payOrderDate).format('DD/MM/YY'),
        controllingNo: item.controllingNo,
        payOrderNumber: item.payOrderNumber,
        amount: `${item.amount.toLocaleString('en-US', { useGrouping: true })}`,
      };
    });
    this.calculateTotalAmount(dataFromBody);
  }
  calculateTotalAmount(data: any) {
    let total = 0;
    for (let payOrder of data) {
      total += payOrder.amount;
    }

    this.amountTotal = total;
  }

  protected fillComponentAttributesFromResponseBody(data: IPayOrder[] | null): IPayOrder[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsToFetch,
      eagerload: true,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    Object.keys(this.payOrderSearchForm.controls).forEach((key: string) => {
      const abstractControl = this.payOrderSearchForm.get(key);

      if (abstractControl instanceof FormControl) {
        console.log('Key = ' + key + ' && Value = ' + abstractControl.value);
        if (abstractControl.value !== null) {
          if (key === 'dealerId') {
            queryObject['dealerId.equals'] = abstractControl.value;
          } else if (key === 'fertilizerId') {
            queryObject['fertilizerId.equals'] = abstractControl.value;
          } else if (key === 'payOrderDateFrom') {
            queryObject['payOrderDate.greaterThanOrEqual'] = dayjs(abstractControl.value).format(DATE_FORMAT);
          } else if (key === 'payOrderDateTo') {
            console.log(dayjs(abstractControl.value, DATE_FORMAT));
            console.log(dayjs(abstractControl.value).format(DATE_FORMAT));
            console.log(abstractControl.value);
            queryObject['payOrderDate.lessThanOrEqual'] = dayjs(abstractControl.value).format(DATE_FORMAT);
          } else if (key === 'payOrderNumber') {
            queryObject['payOrderNumber.equals'] = abstractControl.value;
          } else if (key === 'payOrderNumberFrom') {
            queryObject['payOrderNumber.greaterThanOrEqual'] = abstractControl.value;
          } else if (key === 'payOrderNumberTo') {
            queryObject['payOrderNumber.lessThanOrEqual'] = abstractControl.value;
          } else if (key === 'controllingNo') {
            queryObject['controllingNo.equals'] = abstractControl.value;
          } else if (key === 'slipNo') {
            queryObject['slipNo.equals'] = abstractControl.value;
          }
        }
      }
    });

    return this.payOrderService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
