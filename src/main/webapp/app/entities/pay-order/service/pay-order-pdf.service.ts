import { Injectable } from '@angular/core';
import dayjs from 'dayjs/esm';

import { TDocumentDefinitions } from 'pdfmake/interfaces';

import { Converter, enUS, bnBD } from 'any-number-to-words';
import { IPayOrder, MICRPayOrder } from '../pay-order.model';
import { EnglishToBanglaNumber } from 'app/helpers/english-to-bangla-number';
const converter = new Converter(bnBD);

@Injectable({ providedIn: 'root' })
export class PayOrderPdfService {
  micrPayOrder: MICRPayOrder = new MICRPayOrder();

  constructor() {}

  makeMICRCheckForPayOrder(payOrder: any): TDocumentDefinitions {
    console.log('payOrder', payOrder);

    this.micrPayOrder.payOrderDate = dayjs(payOrder?.payOrderDate).format('DD/MM/YYYY');
    this.micrPayOrder.amount = payOrder.amount;
    this.micrPayOrder.amountInBangla = EnglishToBanglaNumber.convertToBanglaNumber(payOrder?.amount, true) ?? '';
    this.micrPayOrder.amountInBanglaWords = converter.toWords(payOrder?.amount);
    this.micrPayOrder.dealer = payOrder?.dealer;
    this.micrPayOrder.upazila = payOrder?.dealer?.upazila;
    this.micrPayOrder.district = payOrder?.dealer?.upazila?.district;
    this.micrPayOrder.fertilizer = payOrder?.fertilizer;

    this.micrPayOrder.controllingNo = payOrder.controllingNo;
    this.micrPayOrder.controllingNoBangla = EnglishToBanglaNumber.convertToBanglaNumber(payOrder?.controllingNo) ?? '';

    let dayMonthYearArrayInBangla = EnglishToBanglaNumber.formatDateInfo(dayjs(payOrder?.payOrderDate).format('YYYY-MM-DD'));
    this.micrPayOrder.letterDate = dayMonthYearArrayInBangla[0] + ' ' + dayMonthYearArrayInBangla[1] + ' ' + dayMonthYearArrayInBangla[2];
    this.micrPayOrder.letterDateBangla = EnglishToBanglaNumber.formatDateInfoBangla(dayjs(payOrder?.payOrderDate).format('YYYY-MM-DD'));

    this.micrPayOrder.payOrderNumber = payOrder?.payOrderNumber;
    this.micrPayOrder.payOrderNoBangla = EnglishToBanglaNumber.convertToBanglaNumber(payOrder?.payOrderNumber) ?? '';
    this.micrPayOrder.slipNoBangla = EnglishToBanglaNumber.convertToBanglaString(payOrder?.slipNo) ?? '';

    switch (payOrder?.fertilizer?.name) {
      case 'Urea': {
        this.micrPayOrder.fertilizerAmount = payOrder?.amount ? (payOrder?.amount / 25000).toFixed(2) : '0';
        break;
      }
      case 'TSP': {
        this.micrPayOrder.fertilizerAmount = payOrder?.amount ? (payOrder?.amount / 25000).toFixed(2) : '0';
        break;
      }
      case 'DAP': {
        this.micrPayOrder.fertilizerAmount = payOrder?.amount ? (payOrder?.amount / 19000).toFixed(2) : '0';
        break;
      }
      default: {
        this.micrPayOrder.fertilizerAmount = '0';
        break;
      }
    }
    this.micrPayOrder.fertilizerAmount = EnglishToBanglaNumber.convertToBanglaString(this.micrPayOrder.fertilizerAmount) ?? '';

    let micrPayOrderPdfPrint: TDocumentDefinitions = {
      background: function (currentPage, pageCount): any {
        if (currentPage === 2) {
          return {
            image: 'bankLogo',
            width: 300,
            height: 220,
            alignment: 'center',
            opacity: 0.1,
            margin: [0, 300],
          };
        }
      },
      content: [
        {
          columns: [
            {
              type: 'none',
              fontSize: 12,
              margin: [5, 0, 0, 0],
              width: 128,
              ol: [
                {
                  text: '/' + this.micrPayOrder.controllingNo,
                  fontSize: 10,
                  margin: [50, 10, 0, 0],
                },
                {
                  text: this.micrPayOrder.payOrderDate,
                  fontSize: 10,
                  bold: true,
                  margin: [5, 0, 0, 0],
                },
                {
                  text: this.micrPayOrder.dealer?.shortName ?? '',
                  fontSize: 10,
                  font: 'Bangla',
                  margin: [5, 26, 0, 0],
                },
                {
                  text: this.micrPayOrder.upazila?.bnName ?? '',
                  fontSize: 10,
                  font: 'Bangla',
                  margin: [0, 3, 0, 0],
                },
                {
                  text: '=' + this.micrPayOrder.amountInBangla + '/-',
                  fontSize: 10,
                  font: 'Bangla',
                  margin: [0, 20, 0, 0],
                },
                {
                  text: 'JFCL-' + this.micrPayOrder.fertilizer?.name,
                  fontSize: 10,
                  font: 'Bangla',
                  bold: true,
                  margin: [3, 2, 0, 0],
                },
              ],
            },
            {
              type: 'none',
              fontSize: 12,
              margin: [0, 0, 0, 0],
              ol: [
                {
                  text: 'Not Over TK=' + this.micrPayOrder.amount + '/- Only',
                  decoration: 'underline',
                  fontSize: 12,
                  bold: true,
                  margin: [128, 25, 0, 0],
                },
                {
                  text: this.micrPayOrder.fertilizer?.accountTitle ?? '',
                  fontSize: 14,
                  font: 'Bangla',
                  margin: [55, 25, 0, 0],
                },
                {
                  text: '~' + this.micrPayOrder.amountInBanglaWords + ' টাকা মাত্র~',
                  fontSize: 13,
                  font: 'Bangla',
                  margin: [100, 10, 0, 0],
                },
              ],
            },
            {
              type: 'none',
              fontSize: 6,
              width: 113,
              margin: [0, 0, 0, 0],
              ol: [
                {
                  text: '/' + this.micrPayOrder.controllingNo,
                  fontSize: 10,
                  alignment: 'right',
                  margin: [0, 20, 10, 0],
                },
                {
                  text: this.micrPayOrder.payOrderDate,
                  fontSize: 10,
                  bold: true,
                  alignment: 'right',
                  margin: [0, 15, 10, 0],
                },
                {
                  text: '=' + this.micrPayOrder.amountInBangla + '/-',
                  fontSize: 12,
                  font: 'Bangla',
                  alignment: 'right',
                  margin: [0, 50, 10, 0],
                },
              ],
            },
          ],
          pageOrientation: 'portrait',
        },
        {
          columns: [
            {
              margin: [0, 25, 0, 0],
              alignment: 'right',
              ul: [{ height: 60, width: 80, image: 'bankLogo' }],
              width: 250,
            },
            {
              type: 'none',
              fontSize: 20,
              alignment: 'left',
              margin: [0, 30, 0, 0],
              ol: [
                {
                  text: 'জনতা ব্যাংক পিএলসি',
                  font: 'Bangla',
                  // bold: true,
                },
                {
                  text: 'JANATA BANK PLC',
                  fontSize: 16,
                  // bold: true,
                },
              ],
            },
          ],
          margin: [0, 0],
          pageBreak: 'before',
        },
        {
          columns: [
            {
              type: 'none',
              fontSize: 10,
              margin: [0, 25, 0, 0],
              ol: [
                'Head Office Janata Bhaban',
                'Post Box No:468, 110 Motijheel Comercial Area',
                'Dhaka - 1000 Bangladesh',
                'Website: www.janatabank-bd.com',
              ],
            },
            {
              type: 'none',
              fontSize: 10,
              alignment: 'right',
              margin: [0, 25, 0, 0],
              ol: ['Janata Bank Limited', 'Pulhat Branch, Dinajpur', 'Phone: 053163348'],
            },
          ],

          margin: [20, 0, 20, 5],
        },
        { canvas: [{ type: 'line', x1: 0, y1: 0, x2: 595, y2: 0, lineWidth: 1, lineColor: '#4089c1' }] },
        {
          columns: [
            {
              type: 'none',
              alignment: 'left',
              margin: [0, 0, 0, 0],
              ol: [
                {
                  text:
                    'পত্র সূত্র নং : পু শা/বাফার/নি.প/' +
                    this.micrPayOrder?.letterDateBangla[2] +
                    ', কন্ট্রলিং নং : ' +
                    this.micrPayOrder?.controllingNoBangla,
                  font: 'Bangla',
                },
              ],
            },
            {
              type: 'none',
              alignment: 'right',
              margin: [0, 0, 0, 0],
              ol: [
                {
                  text:
                    'তারিখ : ' +
                    this.micrPayOrder?.letterDateBangla[0] +
                    ' ' +
                    this.micrPayOrder?.letterDateBangla[1] +
                    ' ' +
                    this.micrPayOrder?.letterDateBangla[2],
                  font: 'Bangla',
                },
              ],
            },
          ],
          margin: [20, 5, 20, 0],
        },
        {
          type: 'none',
          margin: [20, 25, 20, 0],
          ol: ['ভারপ্রাপ্ত কর্মকর্তা', 'বি. সি. আই. সি', 'বাফার গুদাম', 'পুলহাট, দিনাজপুর।'],
          font: 'Bangla',
        },
        {
          type: 'none',
          margin: [20, 25, 20, 0],
          ol: [
            {
              text:
                'বিষয় : আপনাদের হিসাব নং ' +
                this.micrPayOrder.fertilizer?.accountNo +
                ' এ ' +
                this.micrPayOrder.fertilizer?.bnName +
                ' (' +
                this.micrPayOrder.fertilizer?.name +
                ') এর টাকা জমাকরণ প্রসংগে।',
              font: 'Bangla',
            },
          ],
        },
        {
          type: 'none',
          margin: [20, 25, 20, 0],
          ol: [
            {
              text: 'জনাব,',
              font: 'Bangla',
            },
            {
              alignment: 'justify',
              text: [
                'আপনাদের অবগতির জন্য জানানো যাচ্ছে যে, আপনাদের অনুমোদিত ডিলার কর্তৃক আপনাদের অনুকূলে ক্রয়কৃত পে-অর্ডার এর টাকা আপনাদের হিসাব নং ' +
                  this.micrPayOrder.fertilizer?.accountNo +
                  ' এ জমা করা হয়েছে। বিবরণী নিম্নরূপ : ',
              ],
              font: 'Bangla',
            },
          ],
        },
        {
          margin: [30, 25, 20, 0],
          font: 'Bangla',
          type: 'none',
          ol: [
            {
              text: '১) পে-অর্ডারকারীর নাম : ' + this.micrPayOrder.dealer?.bnName,
            },
            [
              {
                columns: [
                  {
                    text: '২) উপেজলা : ' + this.micrPayOrder.upazila?.bnName,
                  },
                  {
                    text: 'জেলা : ' + this.micrPayOrder.district?.bnName,
                  },
                ],
              },
            ],
            [
              {
                columns: [
                  {
                    text: '৩) পে-অর্ডার নং : ' + this.micrPayOrder.payOrderNoBangla + '/' + this.micrPayOrder.controllingNoBangla,
                  },
                  {
                    text:
                      'পে-অর্ডার তারিখ : ' +
                      this.micrPayOrder?.letterDateBangla[0] +
                      ' ' +
                      this.micrPayOrder?.letterDateBangla[1] +
                      ' ' +
                      this.micrPayOrder?.letterDateBangla[2] +
                      ' ইং',
                  },
                ],
              },
            ],
            {
              text: '৪) টাকার পরিমাণ : ' + this.micrPayOrder.amountInBangla + '/-',
            },
            {
              text: '৫) টাকার পরিমাণ কথায় : ' + this.micrPayOrder.amountInBanglaWords + ' টাকা মাত্র',
            },
            {
              text: '৬) সংযুক্ত পে-ইন স্লিপ নম্বর : ' + this.micrPayOrder.slipNoBangla,
            },
            {
              text: '৭) সার : ' + this.micrPayOrder.fertilizer?.bnName + ' (' + this.micrPayOrder.fertilizer?.name + ')',
            },
            {
              text: '৮) সারের পরিমাণ : ' + this.micrPayOrder.fertilizerAmount + ' টন',
            },
          ],
        },
        {
          type: 'none',
          font: 'Bangla',
          margin: [20, 25, 20, 0],
          ol: [
            {
              text: 'ধন্যবাদ সহ',
            },
          ],
        },
        {
          type: 'none',
          font: 'Bangla',
          margin: [20, 80, 20, 0],
          ol: [
            {
              text: '(অথরাইজড কর্মকর্তার স্বাক্ষর ও সীল)',
            },
          ],
        },
      ],
      images: {
        // in browser is supported loading images via url (https or http protocol) (minimal version: 0.1.67)
        bankLogo:
          'data:image/png;base64,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',
      },
      pageSize: 'A4',
      pageMargins: [0, 0, 0, 0],
      defaultStyle: {
        // alignment: 'justify'
        fontSize: 12,
      },
      info: {
        title: 'JBDaily_Pay_Order_Forwarding_' + this.micrPayOrder.payOrderNumber + '.pdf',
        author: 'Pulhat Branch, AO Dinajpur',
        subject: 'Foreign Remittance',
        keywords: 'Foreign Remittance',
      },
    };

    return micrPayOrderPdfPrint;
  }
}
