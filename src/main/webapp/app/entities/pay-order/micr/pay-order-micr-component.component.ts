import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

// const {Converter, bnBD} = require("any-number-to-words");
// const converter = new Converter(bnBD);

import { Converter, bnBD } from 'any-number-to-words';
import { IPayOrder } from '../pay-order.model';
import dayjs from 'dayjs';
const converter = new Converter(bnBD);

@Component({
  selector: 'jhi-pay-order-micr-component',
  templateUrl: './pay-order-micr-component.component.html',
  styleUrls: ['./pay-order-micr-component.component.scss'],
})
export class PayOrderMicrComponentComponent implements OnInit {
  words: String = '';

  payOrder: IPayOrder | null = null;

  micrPayOrder: any = {};

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payOrder }) => {
      console.log('payOrder', payOrder);
      this.payOrder = payOrder;

      this.micrPayOrder['payOrderDate'] = dayjs(this.payOrder?.payOrderDate).format('DD/MM/YYYY');
      // if (payOrder) {
      //   this.updateForm(payOrder);
      // }
      // else{
      //   this.loadUserSettings();
      // }

      // this.loadRelationshipsOptions();
    });
  }

  convert(number: any) {
    console.log(number);
    this.words = converter.toWords(number);
  }

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

      #page2 {
        position: relative;
      }
      #page2::before{
      
        content: "";
        background: url(data:image/png;base64,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);
        background-repeat: no-repeat;
        background-position: center;
        -webkit-background-size: contain;
        -moz-background-size: contain;
        -o-background-size: contain;
        background-size: 500px;
        position: absolute;
        top: 300px;
        left: 0;
        right: 0;
        bottom: 0;
        
        opacity: 0.2;
      
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

  previousState(): void {
    window.history.back();
  }
}
