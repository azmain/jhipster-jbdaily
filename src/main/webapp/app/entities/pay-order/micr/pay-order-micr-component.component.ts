import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-pay-order-micr-component',
  templateUrl: './pay-order-micr-component.component.html',
  styleUrls: ['./pay-order-micr-component.component.scss'],
})
export class PayOrderMicrComponentComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  public printPdf() {
    const printContents: any = document.getElementById('reportDoc')?.innerHTML;
    const originalContents = document.body.innerHTML;
    document.body.innerHTML = printContents;
    const style = document.createElement('style');
    style.innerHTML = `
    @media print {
      /* Set the scale of the printed document */
      body {
        transform: scale(1);
        font-size: 11px
      }
    }
  `;
    // Add the style element to the document head
    document.head.appendChild(style);
    window.print();
    // location.reload();
    document.body.innerHTML = originalContents;
    /this.downloadDivAsPDF('reportDoc', String(new Date().getTime())+'.pdf');/;
  }
}
