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

  predicate = 'lastModifiedDate';
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
          { text: remittance[columns[7]] ?? 'N/A', style: 'amountStyle', font: 'Bangla' },
          { text: remittance[columns[8]] ?? 'N/A', style: 'amountStyle', font: 'Bangla' },
        ];
      });
      console.log('pdf rows', tableRows);
    } else {
    }

    const pdfName = 'FR_REGISTER_IN_' + dayjs().format('YYYY-MM-DD') + '.pdf';

    let docDefinition: TDocumentDefinitions = {
      info: {
        title: 'FR_REGISTER_IN_' + dayjs().format('YYYY-MM-DD') + '.pdf',
        author: 'Pulhat Branch, AO Dinajpur',
        subject: 'Foreign Remittance',
        keywords: 'Foreign Remittance',
      },
      content: [
        {
          text: 'Foreign Remittance Printed On ' + dayjs().format('DD-MM-YYYY'),
          style: 'header',
          fontSize: 15,
          marginBottom: 10,
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
                  text: this.frAmountTotal.toLocaleString('en-IN', { useGrouping: true }),
                  alignment: 'right',
                },
                {
                  text: this.frIncAmountTotal.toLocaleString('en-IN', { useGrouping: true }),
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
        amount: `${item.amount.toLocaleString('en-IN', { useGrouping: true })}`,
        incentiveAmount: `${item.incentiveAmount.toLocaleString('en-IN', { useGrouping: true })}`,
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

  test() {
    var documentDefinition: TDocumentDefinitions = {
      background: function (currentPage: any, pageCount: any): any {
        if (currentPage === 2) {
          return {
            image: 'snow',
            width: 250,
            height: 250,
            alignment: 'center',
            opacity: 0.2,
            margin: [0, 350],
          };
        }
      },
      content: [
        // Content for the first page
        { text: 'Page 1 Content' },

        // Insert a page break before the second page
        { text: '', pageBreak: 'before' },

        // Content for the second page
        {
          text:
            'There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which dont look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isnt anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.' +
            'There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which dont look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isnt anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.' +
            'There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which dont look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isnt anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.' +
            'There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which dont look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isnt anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.' +
            'There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which dont look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isnt anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc.',
        },

        // Content for subsequent pages
        { text: 'Page 3 Content' },
        // Add more content for subsequent pages as needed
      ],
      pageMargins: [40, 10, 40, 0], // Set the page margins as needed
      header: function (currentPage: any, pageCount: any): any {
        if (currentPage === 2) {
          return {
            width: '100%',
            text: 'Your Watermark Text',
            alignment: 'center', // Centered horizontally
            fontSize: 24,
            bold: true,
            color: 'gray',
            margin: [0, 0], // Adjust the vertical position as needed
          };
        }
      },
      pageSize: 'A4', // Set the page size as needed
      images: {
        // in browser is supported loading images via url (https or http protocol) (minimal version: 0.1.67)
        snow: 'data:image/png;base64,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',
      },
    };

    pdfMake.createPdf(documentDefinition).open();
  }
}
