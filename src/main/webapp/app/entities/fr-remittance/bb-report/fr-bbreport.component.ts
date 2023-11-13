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

import * as ExcelJS from 'exceljs';
// import * as saveAs from 'file-saver';

@Component({
  selector: 'jhi-bbreport',
  templateUrl: './fr-bbreport.component.html',
  styleUrls: ['./fr-bbreport.component.scss'],
})
export class FrBBReportComponent implements OnInit {
  frRemittanceSearchForm: FormGroup;

  transactionTypeValues = TransactionType;
  genderValues = Gender;
  documentTypeValues = DocumentType;

  moneyExchangesSharedCollection: IMoneyExchange[] = [];

  frRemittances?: IFrRemittance[] = [];
  frRemittancesDataTable: any[] = [];
  isLoading = false;

  tableColumns: any[] = [
    { key: 'id', header: 'SN', width: 20 },

    { key: 'recvName', header: 'Name*', width: 20 },
    { key: 'recvGender', header: 'Gender', width: 20 },
    { key: 'documentType', header: 'Document Type', width: 20 },
    { key: 'documentId', header: 'Document/Account', width: 20 },
    { key: 'bankName', header: 'Name of Bank/MFS*', width: 20 },
    { key: 'transactionType', header: 'Type of Transaction*', width: 20 },
    { key: 'recvMobileNo', header: 'Mobile*', width: 20 },

    { key: 'remitersName', header: 'Name*', width: 20 },
    { key: 'remitersGender', header: 'Gender', width: 20 },
    { key: 'remitersProfession', header: 'Profesion', width: 20 },
    { key: 'remitersCountry', header: 'Country', width: 20 },

    { key: 'exchangeCompany', header: 'Name of Bank/ Exchange Co*', width: 20 },
    { key: 'remiSendingDate', header: 'Remittance Sending Date', width: 20 },
    { key: 'remiFrCurrency', header: 'Amount of Remittance in Foreign Currency', width: 20 },
    { key: 'exchangeRate', header: 'Exchange Rate', width: 20 },
    { key: 'remitanceAmount', header: 'Amount (BDT)*', width: 20 },
    { key: 'remiAmountReimDate', header: 'Amount Reimburse Date', width: 20 },

    { key: 'incentivePercentage', header: 'Incentive Percentage*', width: 20 },
    { key: 'incentiveAmount', header: 'Amount of Incentive (BDT)*', width: 20 },
    { key: 'incPaymentDate', header: 'Payment Date of Incentive*', width: 20 },
    { key: 'incAmountReimDate', header: 'Incentive Amount Reimburse Date', width: 20 },

    { key: 'remarks', header: 'Remarks', width: 20 },
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
  }

  runExcelJSExport() {
    let wb = new ExcelJS.Workbook();
    let workbookName = 'FR_BB_REGISTER_' + dayjs().format('YYYY_MM_DD') + '.xlsx';
    let worksheetName = 'Foreign Remittance';

    let ws = wb.addWorksheet(worksheetName, {
      properties: {
        tabColor: { argb: 'FFFF0000' },
      },
    });

    ws.mergeCells('A1:W1');
    ws.getCell('A1').style = {
      font: {
        size: 20,
        bold: true,
      },
      alignment: {
        horizontal: 'center',
        vertical: 'middle',
        wrapText: true,
      },
    };
    const firstRow = ws.getRow(1);
    firstRow.height = 42.5;

    ws.getCell('A1').value = 'Foreign Remittance Incentive Report as On ' + dayjs().format('DD-MM-YYYY');

    ws.mergeCells('A2:W2');
    ws.getCell('A2').style = {
      font: {
        size: 16,
        bold: true,
      },
      alignment: {
        horizontal: 'center',
        vertical: 'middle',
        wrapText: true,
      },
    };
    ws.getCell('A2').value = 'Janata Bank Limited';

    ws.mergeCells('A3:W3');
    ws.getCell('A3').style = {
      font: {
        size: 16,
        bold: true,
      },
      alignment: {
        horizontal: 'center',
        vertical: 'middle',
        wrapText: true,
      },
    };
    ws.getCell('A3').value = 'Pulhat Branch';

    ws.mergeCells('B4:H4');
    ws.getCell('B4').value = "Cash Incentive Receiver's Information";

    ws.mergeCells('I4:L4');
    ws.getCell('I4').value = "Remitter's Information";

    ws.mergeCells('M4:R4');
    ws.getCell('M4').value = 'Remittance Information';

    ws.mergeCells('S4:V4');
    ws.getCell('S4').value = 'Cash Incentive Information';

    ws.getCell('W4').value = 'Remarks';

    [2, 3, 4].map(x => {
      ws.getRow(x).font = { size: 16, bold: true };
      ws.getRow(x).alignment = {
        horizontal: 'center',
        vertical: 'middle',
        wrapText: true,
      };
    });

    // Array of { key, header, width } objects for column headers
    // Add headers to the worksheet
    this.tableColumns.forEach((column, index) => {
      // console.log('column index', column, index);
      const cell: ExcelJS.Cell = ws.getCell(5, index + 1); // Row 5, Column 1 (A)
      cell.name = column.key;
      cell.value = column.header;
      cell.alignment = { horizontal: 'center', vertical: 'middle', wrapText: true };
    });
    // Set column widths
    ws.columns = this.tableColumns.map(column => ({ key: column.key, width: column.width, wrapText: true }));

    // adding data
    ws.addRows(this.frRemittancesDataTable);

    // const dataRange = ws.getCell(6, 1).address + ':' + ws.getCell(frRemittanceData.length + 5, columnHeaders.length).address;

    // console.log('range', ws.getCell(6, 1).address, ws.getCell(frRemittanceData.length + 5, columnHeaders.length).address, dataRange);

    ws.getCell('B6', 'W6').alignment = { horizontal: 'center', wrapText: true };

    const dataRows: ExcelJS.Row[] = ws.getRows(6, this.frRemittancesDataTable.length) ?? [];
    // console.log('rows', dataRows);
    dataRows.forEach((row, index) => {
      // console.log('row index', row, index);
      row.eachCell(cell => {
        cell.alignment = { horizontal: 'center', vertical: 'middle', wrapText: true };
      });
    });

    // Row index to apply formatting
    const lastRowIndex = this.frRemittancesDataTable.length + 6;
    const totalFormula = `SUM(Q6:Q${lastRowIndex - 1})`;
    console.log(totalFormula);

    ws.getCell(`P${lastRowIndex}`).value = 'TOTAL';
    ws.getCell(`P${lastRowIndex}`).style = {
      font: {
        size: 12,
        bold: true,
      },
      alignment: {
        horizontal: 'center',
        vertical: 'middle',
        wrapText: true,
      },
    };
    ws.getCell(`Q${lastRowIndex}`).value = { formula: `SUM(Q6:Q${lastRowIndex - 1})` };

    ws.getCell(`S${lastRowIndex}`).value = 'TOTAL';
    ws.getCell(`S${lastRowIndex}`).style = {
      font: {
        size: 12,
        bold: true,
      },
      alignment: {
        horizontal: 'center',
        vertical: 'middle',
        wrapText: true,
      },
    };
    ws.getCell(`T${lastRowIndex}`).value = { formula: `SUM(T6:T${lastRowIndex - 1})` };

    // ws.getCell("Q6").alignment = { horizontal: "right" };
    ws.views = [{ state: 'frozen', ySplit: 5 }];

    // col = ws.getColumn("E");
    // col.eachCell(function(cell, rowNumber) {
    //   if (rowNumber > 1) {
    //     cell.dataValidation = {
    //       type: "list",
    //       allowBlank: false,
    //       formulae: ['"Employee,Freelance"']
    //     };
    //   }
    // });

    // col = ws.getColumn("F");
    // col.eachCell(function(cell, rowNumber) {
    //   if (rowNumber > 1) {
    //     cell.dataValidation = {
    //       type: 'date',
    //       operator: 'lessThan',
    //       showErrorMessage: true,
    //       allowBlank: false,
    //       formulae: [new Date(1981,1,1)],
    //       errorStyle: 'error',
    //       errorTitle: 'Date is too soon!',
    //       error: 'The dob value must be before 01/01/1981'
    //     };
    //   }
    // });

    // console.log(ws.getRow(5).getCell(9).address);

    // Save the Excel file
    wb.xlsx.writeBuffer().then(buffer => {
      const blob = new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = workbookName + '.xlsx';
      link.click();
    });
    // wb.xlsx.writeBuffer().then(function (buffer) {
    //   FileSaver.saveAs(new Blob([buffer], { type: 'application/octet-stream' }), workbookName);
    // });
  }

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
        remitersGender: item.remiGender,
        remitersProfession: '',
        remitersCountry: item.country,

        //remittance info
        exchangeCompany: item.moneyExchange.name,
        remiSendingDate: item.remiSendingDate ? dayjs(item.remiSendingDate).format('DD/MM/YYYY') : '',
        remiFrCurrency: item.remiFrCurrency, // amount
        exchangeRate: item.exchangeRate,
        remitanceAmount: Number(item.amount),
        remiAmountReimDate: item.amountReimDate ? dayjs(item.amountReimDate).format('DD/MM/YYYY') : '',

        //cash incentive info
        incentivePercentage: item.incPercentage.name,
        incentiveAmount: Number(item.incentiveAmount),
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

    this.runExcelJSExport();
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
