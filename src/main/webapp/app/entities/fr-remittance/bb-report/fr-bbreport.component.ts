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
    'SN',

    'Name*',
    'Gender',
    'Document Type',
    'Document/Account',
    'Name of Bank/MFS*',
    'Type of Transaction*',
    'Mobile*',

    'Name*',
    'Gender',
    'Profesion',
    'Country',

    'Name of Bank/ Exchange Co*',
    'Remittance Sending Date',
    'Amount of Remittance in Foreign Currency',
    'Exchange Rate',
    'Amount (BDT)*',
    'Amount Reimburse Date',

    'Amount of Incentive (BDT)*',
    'Payment Date of Incentive*',
    'Incentive Amount Reimburse Date',
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

  /* Tried excel creation using xlsx(sheetjs) but couldn't complete it because of bad documentation */
  export() {
    let firstHeading = [['Incentive Received']];
    let secondHeading = [['Pulhat Branch']];
    let thirdHeading = [['Janata Bank PLC']];

    let groupedHeaders = [
      { name: "Cash Incentive Receiver's Information", origin: 'B4' },
      { name: "Remitter's Information", origin: 'I4' },
      { name: 'Remittance Information', origin: 'M4' },
      {
        name: 'Cash Incentive Information',
        origin: 'S4',
      },
      {
        name: 'Remarks',
        origin: 'V4',
      },
    ];

    //Had to create a new workbook and then add the header
    const wb = XLSX.utils.book_new();
    const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet([]);

    ws['!merges'] = [
      XLSX.utils.decode_range('A1:V1'),
      XLSX.utils.decode_range('A2:V2'),
      XLSX.utils.decode_range('A3:V3'),

      XLSX.utils.decode_range('B4:H4'),
      XLSX.utils.decode_range('I4:L4'),
      XLSX.utils.decode_range('M4:R4'),
      XLSX.utils.decode_range('S4:U4'),
    ];

    ws['!cols'] = [{ wch: 12 }];

    XLSX.utils.sheet_add_aoa(ws, firstHeading, { origin: 'A1', cellStyles: true });
    XLSX.utils.sheet_add_aoa(ws, secondHeading, { origin: 'A2', cellStyles: true });
    XLSX.utils.sheet_add_aoa(ws, thirdHeading, { origin: 'A3', cellStyles: true });

    groupedHeaders.forEach(({ name, origin }) => {
      console.log('LOG1', name, origin);
      XLSX.utils.sheet_add_aoa(ws, [[name]], { origin });
    });

    XLSX.utils.sheet_add_aoa(
      ws,
      [
        [
          'SN',

          'Name*',
          'Gender',
          'Document Type',
          'Document/Account',
          'Name of Bank/MFS*',
          'Type of Transaction*',
          'Mobile*',

          'Name*',
          'Gender',
          'Profesion',
          'Country',

          'Name of Bank/ Exchange Co*',
          'Remittance Sending Date',
          'Amount of Remittance in Foreign Currency',
          'Exchange Rate',
          'Amount (BDT)*',
          'Amount Reimburse Date',

          'Amount of Incentive (BDT)*',
          'Payment Date of Incentive*',
          'Incentive Amount Reimburse Date',
          '',
        ],
      ],
      { origin: 'A5' }
    );

    // XLSX.utils.sheet_add_aoa(ws, Heading);

    //Starting in the second row to avoid overriding and skipping headers
    XLSX.utils.sheet_add_json(ws, this.frRemittancesDataTable, { origin: 'A6', skipHeader: true });

    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');

    XLSX.writeFile(wb, 'filename.xlsx');
  }

  runExcelJSExport() {
    let frRemittanceData = [
      {
        'salesman-name': 'Jim Smith',
        sales: 12345,
        uri: 'https://www.google.com',
        'met-target': true,
        status: 'Employee',
        dob: new Date(),
        level: 1.1,
      },
    ];

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

    ws.mergeCells('M4:S4');
    ws.getCell('M4').value = 'Remittance Information';

    ws.mergeCells('T4:V4');
    ws.getCell('T4').value = 'Cash Incentive Information';

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
    const columnHeaders = [
      { key: 'salesman-name', header: 'Salesman Name', width: 20 },
      { key: 'sales', header: 'Sales', width: 25 },
      { key: 'status', header: 'Status', width: 20 },
      { key: 'met-target', header: 'Met Traget', width: 25 },
      { key: 'uri', header: 'URI', width: 20 },
      { key: 'dob', header: 'Date of Birth', width: 25 },
      { key: 'level', header: 'Level', width: 25 },

      // Add more headers as needed
    ];

    // Add headers to the worksheet
    columnHeaders.forEach((column, index) => {
      console.log('column index', column, index);
      const cell: ExcelJS.Cell = ws.getCell(5, index + 1); // Row 5, Column 1 (A)
      cell.name = column.key;
      cell.value = column.header;
      cell.alignment = { horizontal: 'center' };
    });

    // Set column widths
    ws.columns = columnHeaders.map(column => ({ key: column.key, width: column.width, wrapText: true }));

    // Add data to the worksheet starting from row 6
    // frRemittanceData.forEach((rowData, rowIndex) => {
    //   columnHeaders.forEach((column, columnIndex) => {
    //     const cell = ws.getCell(rowIndex + 6, columnIndex + 1); // Starting from Row 6, Column 1 (A)
    //     cell.value = rowData[column.key];
    //   });
    // });

    ws.addRows(frRemittanceData);

    const dataRange = ws.getCell(6, 1).address + ':' + ws.getCell(frRemittanceData.length + 5, columnHeaders.length).address;

    console.log('range', ws.getCell(6, 1).address, ws.getCell(frRemittanceData.length + 5, columnHeaders.length).address, dataRange);

    // ws.getCell(dataRange).alignment = { horizontal: 'center', wrapText: true };
    const rows = ws.getRows(6, frRemittanceData.length);
    console.log('rows', rows);

    ws.eachRow(function (row, rowNumber) {
      console.log('Row ' + rowNumber + ' = ' + JSON.stringify(row.values));
    });
    // ws.eachCell(dataRange, { horizontal: 'center', wrapText: true });
    // for (const row of ws.getRows(dataRange)) {
    //   for (const cell of row) {
    //     cell.alignment = { horizontal: 'center', wrapText: true };
    //   }
    // }
    // frRemittanceData.forEach((rowData, rowIndex) => {
    //   columnHeaders.forEach((column, columnIndex) => {
    //     const cell = ws.getCell(rowIndex + 6, columnIndex + 1); // Starting from Row 6, Column 1 (A)
    //     cell.alignment = { horizontal: 'center', wrapText: true };
    //   });
    // });

    // ws.addRow([
    //   'SN',
    //   {
    //     header: 'Name*',
    //     key: 'name',
    //     width: 32
    //   }, 'Gender', 'Document Type', 'Document/Account', 'Name of Bank / MFS*',
    //   'Type of Transaction*', 'Extra1',
    //   'Name*', 'Gender', 'Profession', 'Country',
    //   'Name of Bank/ Exchange Co*', 'Remittance Sending Date', 'Amount of Remittance in Foreign Currency', 'Exchange Rate', 'Amount (BDT)*', 'Extra2', 'Extra3',
    //   'Amount of Incentive (BDT)*', 'Payment Date of Incentive*', 'Remarks'
    // ]);

    // ws.addRows(frRemittanceData);

    // ws.columns = [
    //   { header: 'Id', key: 'id', width: 10 },
    //   { header: 'Name', key: 'name', width: 32 },
    //   { header: 'D.O.B.', key: 'DOB', width: 10, outlineLevel: 1 }
    // ];

    // ws.addRow(5, {id: 3, name: 'john doe', dob: 'date'});

    // available in latest version
    // ws.getRows(1, 4).forEach((row) => {
    //   // for(let i = 1; i <= 7; i++) {
    //   //   row.getCell(i).value = 'hello';
    //   // }
    //   row.font = {
    //     size: 16,
    //     bold: true
    //   };
    // });

    // let col = ws.getRow();
    // col.eachCell(function(cell, rowNumber) {
    //   if (rowNumber > 1) {
    //     if (cell.value) {
    //       cell.value = "\u2713";
    //     } else {
    //       cell.value = null;
    //     }
    //   }
    // });

    // ws.columns = []

    // ws.columns = [
    //   {
    //     key: "salesman-name",
    //     header: "Salesman-Name",
    //     width: 20
    //   },
    //   {
    //     key: "sales",
    //     header: "Sales",
    //     width: 15,
    //     style: { numFmt: '"$"#,##0.00;[Red]-"$"#,##0.00' }
    //   },
    //   {
    //     key: "uri",
    //     header: "URI",
    //     width: 30,
    //     outlineLevel: 1 ,
    //     hidden: false
    //   },
    //   {
    //     key: "met target",
    //     header: "Met Target?",
    //     width: 12,
    //     style: {
    //       alignment: { horizontal: "center" },
    //       font: { color: { argb: "008000" } }
    //     }
    //   },
    //   {
    //     key: "status",
    //     header: "Status"
    //   },
    //   {
    //     key: "dob",
    //     header: "Date of Birth",
    //     width: 12,
    //     style: { numFmt: "dd/mm/yyyy" }
    //   },
    //   {
    //     key: "level",
    //     header: "Level",
    //     width: 5,
    //     style: { numFmt: "0.0" }
    //   },
    //   {
    //     key: "comments",
    //     header: "Comments",
    //     width: 30,
    //     style: { alignment: { wrapText: true }, numFmt: '@' },
    //     outlineLevel: 1 ,
    //     hidden: false
    //   },
    //   {
    //     key: "dob_linked",
    //     header: "Date of Birth (Linked and Formatted)",
    //     width: 35,
    //     style: { numFmt: "dddd, MMMM dd, yyyy" },
    //     outlineLevel: 2,
    //     hidden: false
    //   },

    // ];

    // ws.getRow(1).font = { bold: true };
    // ws.getCell("B1").alignment = { horizontal: "right" };
    // ws.getCell('F1').alignment = { textRotation: 90 };
    // ws.views = [{ state: "frozen", ySplit: 2 }];

    // ws.addRows(data);

    // ws.getCell("C2").value = {
    //   text: "www.google.com",
    //   hyperlink: "http://www.google.com",
    //   tooltip: "Click to go to google.com"
    // };
    // ws.getCell("C2").font = {
    //   color: { argb: "0000FF" },
    //   underline: true
    // };

    // let totCell = "B" + (data.length + 2);
    // let totFormula = "SUM(B2:B" + (data.length + 1) + ")";
    // ws.getCell(totCell).value = { formula: totFormula };
    // ws.getCell(totCell).border = {
    //   top: { style: "thin" },
    //   bottom: { style: "double" }
    // };
    // // We can name this cell to make it easier to reference later
    // ws.getCell(totCell).name = 'salestotal';

    // let col = ws.getColumn("D");
    // col.eachCell(function(cell, rowNumber) {
    //   if (rowNumber > 1) {
    //     if (cell.value) {
    //       cell.value = "\u2713";
    //     } else {
    //       cell.value = null;
    //     }
    //   }
    // });

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

    // // Remove all HTML from the comments field
    // col = ws.getColumn("H");
    // col.eachCell(function(cell, rowNumber) {
    //   if (cell.value !== null && String(cell.value).trim() !== '') {
    //     let doc = new DOMParser().parseFromString(cell.value, "text/html");
    //     cell.value = doc.documentElement.textContent;
    //   }
    // });

    // col = ws.getColumn("I");
    // col.eachCell(function(cell, rowNumber) {
    //   if (rowNumber > 1) {
    //     cell.value = {formula: 'F' + rowNumber};
    //   }
    // });

    // // Reference cell by rownum, colnum
    // ws.getCell(5,1).fill = {
    //   type: "pattern",
    //   pattern: "solid",
    //   fgColor: { argb: "F3FF33" }
    // };

    // ws.getCell("G4").value = {
    //   richText: [
    //     { text: "This " },
    //     { font: { italic: true }, text: "is" },
    //     {
    //       font: {
    //         size: 14,
    //         color: { argb: "FF00FF00" },
    //         bold: true
    //       },
    //       text: "rich"
    //     },
    //     { font: { underline: true }, text: " text!" }
    //   ]
    // };

    // // Merge cells
    // ws.mergeCells('A15:I17');
    // ws.getCell('A15').style = {
    //   font: {
    //     size: 20,
    //     bold: true
    //   },
    //   alignment: {
    //     horizontal: 'center',
    //     vertical: 'middle',
    //     wrapText: true
    //   }
    // };
    // ws.getCell('A15').value = 'This is merged cells (A15:I17), with large bold text, vertically and horizontally aligned';

    // ws.getRow(15).outlineLevel = 1;
    // ws.getRow(16).outlineLevel = 1;
    // ws.getRow(17).outlineLevel = 1;

    // ws.getColumn('I').hidden = true;
    // ws.getRow(4).hidden = true;

    // // Retrieve a named cell value from another worksheet
    // ws2.getColumn('A').width = 25;
    // ws2.getCell('A3').value = 'Value from 1st sheet:';
    // ws2.getCell('B3').value = { formula: 'salestotal' };

    console.log(ws.getRow(5).getCell(9).address);

    var FileSaver = require('file-saver');

    wb.xlsx.writeBuffer().then(function (buffer) {
      FileSaver.saveAs(new Blob([buffer], { type: 'application/octet-stream' }), workbookName);
    });
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
