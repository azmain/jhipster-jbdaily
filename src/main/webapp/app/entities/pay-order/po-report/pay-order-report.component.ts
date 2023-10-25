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
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { EntityArrayResponseType, PayOrderService } from '../service/pay-order.service';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { ASC, DEFAULT_SORT_DATA, DESC, SORT } from 'app/config/navigation.constants';
import dayjs from 'dayjs';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { jsPDF } from 'jspdf';
import autoTable from 'jspdf-autotable';
import { Table } from 'primeng/table';
// import { font } from 'content/fonts/custom-font';
import 'content/fonts/TiroBangla-Regular-normal.js';
// import 'content/fonts/SolaimanLipi_20-04-07-normal.js';

@Component({
  selector: 'jhi-pay-order-report',
  templateUrl: './pay-order-report.component.html',
  styleUrls: ['./pay-order-report.component.scss'],
})
export class PayOrderReportComponent implements OnInit {
  @ViewChild('pTableId') pTableRef: any;

  payOrderSearchForm: FormGroup;

  fertilizersSharedCollection: IFertilizer[] = [];
  dealersSharedCollection: IDealer[] = [];

  payOrders?: IPayOrder[] = [];
  payOrdersDataTable: any[] = [];
  isLoading = false;

  predicate = 'payOrderDate';
  ascending = false;
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE + 10;
  itemsToFetch = 5000;
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
  ngAfterViewInit() {
    const table = this.pTableRef.el.nativeElement.querySelector('table');
    table.setAttribute('id', 'myTableId');
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

  exportPdf() {
    // const doc = new jsPDF();

    const tableData = [
      ['Column 1', 'Column 2', 'Column 3'],
      ['Row 1, Cell 1', 'Row 1, Cell 2', 'Row 1, Cell 3'],
      ['Row 2, Cell 1', 'Row 2, Cell 2', 'Row 2, Cell 3'],
    ];
    // const d = [{
    //   'one': 'one'
    // }];
    // let styles = {
    //   autoSize: true,
    //   printHeaders: false,
    //   columnWidths: 80
    // }

    // doc.table(10, 10, d, tableData[0], styles);

    // var doc = new jsPDF('p','pt', 'a4', true);
    // var header = [1,2,3,4];
    // doc.table(10, 10, $('#test').get(0), header, {
    // left:10,
    // top:10,
    // bottom: 10,
    // width: 170,
    // autoSize:false,
    // printHeaders: true
    // });
    // doc.save('sample-file.pdf');

    // Define the coordinates and width of the table
    // const tableX = 10;
    // const tableY = 10;
    // const tableWidth = 190;

    // // Create the header row
    // doc.text(tableData[0], tableX, tableY);

    // // Create the data rows
    // for (let i = 1; i < tableData.length; i++) {
    //   const rowData = tableData[i];
    //   doc.text(rowData, tableX, tableY + i * 10);
    // }

    // Save the PDF
    // doc.save('sample.pdf');

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

  export() {
    const doc = new jsPDF();

    // doc.addFileToVFS('SolaimanLipi_20-04-07-normal.ttf', font);
    // doc.addFont('SolaimanLipi_20-04-07-normal.ttf', 'SolaimanLipi_20-04-07', 'normal');

    console.log(doc.getFontList());

    doc.setFont('TiroBangla-Regular');
    doc.setFontSize(10);

    // It can parse html:
    // <table id="my-table"><!-- ... --></table>
    // autoTable(doc, { html: '#myTableId' })

    // Or use javascript directly:
    // autoTable(doc, {
    //   head: [['Name', 'Email', 'Country']],
    //   body: [
    //     ['আব্দুর রহমান', 'david@example.com', 'Sweden'],
    //     ['সোনার বাংলা', 'castille@example.com', 'Spain'],
    //     // ...
    //   ],
    // });
    doc.text('আব্দুর রহমান সোনার বাংলা', 20, 20, {});

    doc.save('table.pdf');

    // const doc = new jsPDF();

    // // doc.addFileToVFS("Amiri-Regular.ttf", AmiriRegular);
    // // doc.addFont("Amiri-Regular.ttf", "Amiri", "normal");

    // doc.addFont("content/fonts/Nikosh.ttf", "Nikosh", "normal");

    // doc.setFont("Nikosh");
    // doc.setFontSize(10);

    // doc.text('আমার সোনার বাংলা', 10, 10);

    // doc.save('test.pdf');
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
        payOrderDate: dayjs(item.payOrderDate, 'DD-MM-YYYY'),
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
