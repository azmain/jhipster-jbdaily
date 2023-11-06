import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Gender } from 'app/entities/enumerations/gender.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { IMoneyExchange } from 'app/entities/money-exchange/money-exchange.model';
import { IFrRemittance } from '../fr-remittance.model';
import { FilterOptions, IFilterOptions } from 'app/shared/filter/filter.model';
import { ITEMS_FOR_DROPDOWN, ITEMS_PER_PAGE, ITEMS_TO_FETCH, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ASC, DESC } from 'app/config/navigation.constants';
import { IDealer } from 'app/entities/dealer/dealer.model';
import { IFertilizer } from 'app/entities/fertilizer/fertilizer.model';
import { MoneyExchangeService } from 'app/entities/money-exchange/service/money-exchange.service';
import { font } from 'content/fonts/custom-font';
import dayjs from 'dayjs';
import pdfMake from 'pdfmake/build/pdfmake';
import { TDocumentDefinitions } from 'pdfmake/interfaces';
import { map, Observable, tap } from 'rxjs';
import { FrRemittanceService, EntityArrayResponseType } from '../service/fr-remittance.service';
import { FrRemittanceFormService } from '../update/fr-remittance-form.service';

import pdfFonts from 'pdfmake/build/vfs_fonts';

import * as XLSX from 'xlsx';

@Component({
  selector: 'jhi-bbreport',
  templateUrl: './fr-bbreport.component.html',
  styleUrls: ['./fr-bbreport.component.scss'],
})
export class FrBBReportComponent implements OnInit {
  @ViewChild('TABLE') table: ElementRef | undefined;

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

  onSearch() {
    // Handle frRemittanceSearchForm submission here
    console.log(this.frRemittanceSearchForm.value);
    this.queryBackend(this.page, this.predicate, this.ascending).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });

    // const ws: XLSX.WorkSheet=XLSX.utils.table_to_sheet(this.table?.nativeElement);
    // const wb: XLSX.WorkBook = XLSX.utils.book_new();
    // XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');

    // /* save to file */
    // XLSX.writeFile(wb, 'SheetJS.xlsx');
  }

  export() {}

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.frRemittances = dataFromBody;
    this.frRemittancesDataTable = this.frRemittances.map((item: any, index) => {
      return {
        id: index + 1,
        //cash incentive receiver info
        recvName: item.recvName,
        recvGender: item.recvGender,
        documentType: item.documentType,
        documentId: item.recvLegalId,
        bankName: item.transactionType === 'ACCOUNT' ? 'Janata Bank PLC' : '',
        transactionType: item.transactionType,
        recvMobileNo: item.recvMobileNo,

        //remitters info
        remitersName: item.remitersName,
        remitersGender: item.remitersGender,
        remitersProfession: '',
        remitersCountry: item.remitersCountry,

        //remittance info
        exchangeCompany: item.moneyExchange.name,
        remiSendingDate: item.remiSendingDate ? dayjs(item.remiSendingDate).format('DD/MM/YYYY') : '',
        remiFrCurrency: item.remiFrCurrency,
        exchangeRate: item.exchangeRate,
        remitanceAmount: item.amount,
        remiAmountReimDate: item.amountReimDate ? dayjs(item.amountReimDate).format('DD/MM/YYYY') : '',

        //cash incentive info
        incentiveAmount: item.incentiveAmount,
        incPaymentDate: item.incPaymentDate ? dayjs(item.incPaymentDate).format('DD/MM/YYYY') : '',
        incAmountReimDate: item.incAmountReimDate ? dayjs(item.incAmountReimDate).format('DD/MM/YYYY') : '',
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

    import('xlsx').then(xlsx => {
      const worksheet = xlsx.utils.json_to_sheet(this.frRemittancesDataTable);
      const workbook = { Sheets: { data: worksheet }, SheetNames: ['data'] };
      const excelBuffer: any = xlsx.write(workbook, { bookType: 'xlsx', type: 'array' });
      XLSX.writeFile(workbook, 'SheetJS.xlsx');
      // this.saveAsExcelFile(excelBuffer, "products");
    });
  }

  protected fillComponentAttributesFromResponseBody(data: IFrRemittance[] | null): IFrRemittance[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
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
    this.isLoading = true;
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
