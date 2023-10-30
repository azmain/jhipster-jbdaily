import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { IDealer } from 'app/entities/dealer/dealer.model';
import { DealerService } from 'app/entities/dealer/service/dealer.service';
import { IFertilizer } from 'app/entities/fertilizer/fertilizer.model';
import { FertilizerService } from 'app/entities/fertilizer/service/fertilizer.service';
import { Observable, combineLatest, map, switchMap, tap } from 'rxjs';
import { FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter/filter.model';
import {
  ITEMS_FOR_DROPDOWN,
  ITEMS_PER_PAGE,
  ITEMS_TO_FETCH,
  PAGE_HEADER,
  TOTAL_COUNT_RESPONSE_HEADER,
} from 'app/config/pagination.constants';

import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { ASC, DEFAULT_SORT_DATA, DESC, SORT } from 'app/config/navigation.constants';
import dayjs from 'dayjs';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';

import { font } from 'content/fonts/custom-font';

import pdfMake from 'pdfmake/build/pdfmake';
import pdfFonts from 'pdfmake/build/vfs_fonts';
import { TDocumentDefinitions } from 'pdfmake/interfaces';
import { EnglishToBanglaNumber } from 'app/helpers/english-to-bangla-number';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { IMoneyExchange } from 'app/entities/money-exchange/money-exchange.model';
import { MoneyExchangeService } from 'app/entities/money-exchange/service/money-exchange.service';
import { EntityArrayResponseType, FrRemittanceService } from '../service/fr-remittance.service';
import { FrRemittanceFormService } from '../update/fr-remittance-form.service';
import { IFrRemittance } from '../fr-remittance.model';

@Component({
  selector: 'jhi-fr-register',
  templateUrl: './fr-register.component.html',
  styleUrls: ['./fr-register.component.scss'],
})
export class FrRegisterComponent implements OnInit {
  frRemittanceSearchForm: FormGroup;

  transactionTypeValues = TransactionType;
  genderValues = Gender;
  documentTypeValues = DocumentType;

  moneyExchangesSharedCollection: IMoneyExchange[] = [];

  frRemittances?: IFrRemittance[] = [];
  frRemittancesDataTable: any[] = [];
  isLoading = false;

  tableColumns: string[] = [
    'Serial',
    'Type',
    'Benificiary Name',
    'NID / Passport No',
    'Mobile No',
    'PIN',
    'Payment Date',
    'Amount (BDT)',
    'Incentive Amount (BDT)',
  ];

  predicate = 'createdDate';
  ascending = false;
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE + 10;
  itemsForDropdown = ITEMS_FOR_DROPDOWN;
  itemsToFetch = ITEMS_TO_FETCH;
  totalItems = 0;
  page = 1;
  frAmountTotal = 0;
  frIncAmountTotal = 0;

  constructor(
    private fb: FormBuilder,
    protected frRemittanceService: FrRemittanceService,
    protected frRemittanceFormService: FrRemittanceFormService,
    protected moneyExchangeService: MoneyExchangeService
  ) {
    this.frRemittanceSearchForm = this.fb.group({
      transactionType: [null],
      moneyExchangeId: [null],

      paymentDateFrom: [null],
      paymentDateTo: [null],

      incPaymentDateFrom: [null],
      incPaymentDateTo: [null],

      recvName: [null],
    });
  }

  ngOnInit(): void {
    this.loadRelationshipsOptions();
  }

  onSearch() {
    // Handle frRemittanceSearchForm submission here
    console.log(this.frRemittanceSearchForm.value);
    this.queryBackend(this.page, this.predicate, this.ascending).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  protected loadRelationshipsOptions(): void {
    const queryObject = {
      page: this.page - 1,
      size: this.itemsForDropdown,
      eagerload: false,
      sort: this.getSortQueryParam(this.predicate, this.ascending),
    };
    this.moneyExchangeService
      .query(queryObject)
      .pipe(
        map((res: HttpResponse<IDealer[]>) => {
          return res.body ? res.body.map(item => this.convertMoneyExchangeOption(item)) : [];
        })
      )
      .subscribe((fertilizers: IFertilizer[]) => {
        this.moneyExchangesSharedCollection = fertilizers;
      });
  }

  convertMoneyExchangeOption(moneyExchange: IMoneyExchange): IMoneyExchange {
    return {
      id: moneyExchange.id,
      name: moneyExchange.name,
      shortName: moneyExchange.shortName,
    };
  }

  export() {
    let columns: any[] = [];
    let tableRows: any[] = [];
    // console.log(font);
    console.log(this.frRemittancesDataTable);
    if (this.frRemittancesDataTable.length > 0) {
      columns = Object.keys(this.frRemittancesDataTable[0]);
      console.log('pdf columns', columns);
      tableRows = this.frRemittancesDataTable.map((remittance, i) => {
        return [
          remittance[columns[0]] ?? 'N/A',
          remittance[columns[1]] ? (remittance[columns[1]] === 'ACCOUNT' ? 'A/C' : 'SPOT') : 'N/A',
          remittance[columns[2]] ?? 'N/A',
          remittance[columns[3]] ?? 'N/A',
          remittance[columns[4]] ?? 'N/A',
          remittance[columns[5]] ?? 'N/A',
          remittance[columns[6]] ?? 'N/A',
          { text: remittance[columns[7]] ?? 'N/A', style: 'amountStyle' },
          { text: remittance[columns[8]] ?? 'N/A', style: 'amountStyle' },
        ];
      });
      console.log('pdf rows', tableRows);
    } else {
    }
    let docDefinition: TDocumentDefinitions = {
      info: {
        title: 'awesome Document',
        author: 'john doe',
        subject: 'subject of document',
        keywords: 'keywords for document',
      },
      content: [
        {
          text: 'Foreign Remittance Printed On ' + dayjs().format('DD-MM-YYYY'),
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
                  colSpan: 7,
                  alignment: 'right',
                },
                {},
                {},
                {},
                {},
                {},
                {},
                {
                  text: this.frAmountTotal.toLocaleString('en-US', { useGrouping: true }),
                  alignment: 'right',
                },
                {
                  text: this.frIncAmountTotal.toLocaleString('en-US', { useGrouping: true }),
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
        font: 'Roboto',
        alignment: 'center',
        fontSize: 8,
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
      Roboto: {
        normal: 'Roboto-Regular.ttf',
        bold: 'Roboto-Medium.ttf',
        italics: 'Roboto-Italic.ttf',
        bolditalics: 'Roboto-MediumItalic.ttf',
      },
    };
    // pdfMake.createPdf(docDefinition).open();
    const pdfName = 'FR_REGISTER_IN_' + dayjs().format('YYYY-MM-DD') + '.pdf';
    pdfMake.createPdf(docDefinition).download(pdfName);
    // pdfMake.createPdf(docDefinition).print();
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.frRemittances = dataFromBody;
    this.frRemittancesDataTable = this.frRemittances.map((item: any, index) => {
      return {
        id: index + 1,
        transactionType: item.transactionType,
        recvName: item.recvName,
        recvLegalId: item.recvLegalId,
        mobile: item.recvMobileNo,
        pin: item.pin,
        paymentDate: dayjs(item.paymentDate).format('DD/MM/YYYY'),
        amount: `${item.amount.toLocaleString('en-US', { useGrouping: true })}`,
        incentiveAmount: `${item.incentiveAmount.toLocaleString('en-US', { useGrouping: true })}`,
      };
    });
    this.calculateTotalAmount(dataFromBody);
  }
  calculateTotalAmount(data: any) {
    let frTotal = 0;
    let frIncTotal = 0;
    for (let fr of data) {
      frTotal += isNaN(+fr.amount) ? 0 : Number(fr.amount);
      frIncTotal += isNaN(+fr.incentiveAmount) ? 0 : Number(fr.incentiveAmount);
    }

    this.frAmountTotal = frTotal;
    this.frIncAmountTotal = frIncTotal;
  }

  protected fillComponentAttributesFromResponseBody(data: IFrRemittance[] | null): IFrRemittance[] {
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
    Object.keys(this.frRemittanceSearchForm.controls).forEach((key: string) => {
      const abstractControl = this.frRemittanceSearchForm.get(key);

      if (abstractControl instanceof FormControl) {
        console.log('Key = ' + key + ' && Value = ' + abstractControl.value);

        if (abstractControl.value !== null) {
          if (key === 'transactionType') {
            queryObject['transactionType.equals'] = abstractControl.value;
          } else if (key === 'moneyExchangeId') {
            queryObject['moneyExchangeId.equals'] = abstractControl.value;
          } else if (key === 'paymentDateFrom') {
            queryObject['paymentDate.greaterThanOrEqual'] = dayjs(abstractControl.value).format(DATE_FORMAT);
          } else if (key === 'paymentDateTo') {
            // console.log(dayjs(abstractControl.value, DATE_FORMAT));
            // console.log(dayjs(abstractControl.value).format(DATE_FORMAT));
            // console.log(abstractControl.value);
            queryObject['paymentDate.lessThanOrEqual'] = dayjs(abstractControl.value).format(DATE_FORMAT);
          } else if (key === 'incPaymentDateFrom') {
            queryObject['incPaymentDate.greaterThanOrEqual'] = dayjs(abstractControl.value).format(DATE_FORMAT);
          } else if (key === 'incPaymentDateTo') {
            queryObject['incPaymentDate.lessThanOrEqual'] = dayjs(abstractControl.value).format(DATE_FORMAT);
          } else if (key === 'recvName') {
            queryObject['recvName.contains'] = abstractControl.value;
          }
        }
      }
    });

    return this.frRemittanceService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
