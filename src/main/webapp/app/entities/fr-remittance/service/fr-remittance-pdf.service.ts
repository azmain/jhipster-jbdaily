import { Injectable } from '@angular/core';
import dayjs from 'dayjs/esm';

import { TDocumentDefinitions } from 'pdfmake/interfaces';
import { IFrRemittance } from '../fr-remittance.model';

import { Converter, enUS } from 'any-number-to-words';
const converter = new Converter(enUS);

@Injectable({ providedIn: 'root' })
export class FrRemittancePdfService {
  constructor() {}

  convertDateFromServer(frRemittance: IFrRemittance): TDocumentDefinitions {
    let receiverName = frRemittance.recvName ?? '';
    let frPin = frRemittance.pin ?? '';
    let frIdDocument = frRemittance.recvLegalId ?? '';
    let frMainAmount = isNaN(frRemittance.amount ? +frRemittance.amount : 0) ? 0 : Number(frRemittance.amount);
    let frIncentiveAmount = isNaN(frRemittance.incentiveAmount ? +frRemittance.incentiveAmount : 0)
      ? 0
      : Number(frRemittance.incentiveAmount);
    let receiverMobile = frRemittance.recvMobileNo ?? '';
    let frVoucherDate = dayjs().format('DD/MM/YYYY');
    let frMoneyExchangeName = frRemittance.moneyExchange?.name ?? '';
    let frTotalAmount = frMainAmount + frIncentiveAmount;
    let frIncentiveAmountWords = converter.toWords(frIncentiveAmount);

    let frRemittanceVoucherPrint: TDocumentDefinitions = {
      content: [
        {
          text: '২.৫% প্রণোদনা/নগদ সহায়তার অর্থ প্রাপ্তির স্বীকারোক্তি',
          alignment: 'center',
          decoration: 'underline',
          fontSize: 14,
          marginBottom: 10,
        },
        {
          text: [
            'আমি ',
            { text: receiverName },
            ' এই মর্মে স্বীকারোক্তি প্রদান করিতেছি যে, জনতা ব্যাংক পুলহাট শাখা হতে অদ্য ',
            frVoucherDate,
            'ইং তারিখে ',
            frMoneyExchangeName,
            ' ব্যাংক/এক্সচেঞ্জ কোম্পানির রেমিট্যান্স নম্বর ',
            { text: frPin, bold: true, font: 'Roboto' },
            ' এর বিপরীতে মূল রেমিট্যান্স অর্থ ',
            { text: frMainAmount, bold: true, font: 'Roboto' },
            ' টাকা এবং উক্ত রেমিট্যান্স এর বিপরীতে ২.৫% প্রণোদনা/নগদ সহায়তার অর্থ বাবদ ',
            { text: frIncentiveAmount, bold: true, font: 'Roboto' },
            ' সহ মোট ',
            { text: frTotalAmount, bold: true, font: 'Roboto' },
            ' টাকা গ্রহণ করলাম। এ ক্ষেত্রে পরবর্তীতে যদি অতিরিক্ত অর্থ গ্রহণ বা বিষয়ে কোন অনিয়ম',
            ' উদঘাটিত হয় তবে আমি গৃহীত অর্থ প্রদাণে বাধ্য থাকবো।',
          ],
          alignment: 'justify',
        },
        {
          margin: [0, 20, 0, 0],
          columns: [
            {
              text: '',
              width: '30%',
            },
            [
              {
                columns: [
                  {
                    stack: [
                      // second column consists of paragraphs
                      'উপভোগকারীর স্বাক্ষরঃ ',
                      'তারিখঃ ',
                      'নামঃ ',
                      'মোবাইলঃ ',
                      'পরিচয়পত্রঃ ',
                    ],
                    alignment: 'right',
                  },
                  {
                    stack: [
                      // second column consists of paragraphs
                      '____________',
                      frVoucherDate,
                      receiverName,
                      receiverMobile,
                      frIdDocument,
                    ],
                    alignment: 'left',
                  },
                ],
              },
            ],
          ],
        },
        {
          canvas: [{ type: 'line', x1: 0, y1: 10, x2: 595, y2: 10, dash: { length: 5, space: 5 } }],
        },

        {
          text: '২.৫% প্রণোদনা/নগদ সহায়তার অর্থ প্রদাণের রশিদ',
          alignment: 'center',
          decoration: 'underline',
          margin: [0, 10, 0, 10],
          fontSize: 14,
        },
        {
          text: [
            frMoneyExchangeName,
            ' ব্যাংক/এক্সচেঞ্জ কোম্পানির রেমিট্যান্স নম্বর ',
            { text: frPin, bold: true, font: 'Roboto' },
            ' এর বিপরীতে মূল রেমিট্যান্স অর্থ ',
            { text: frMainAmount, bold: true, font: 'Roboto' },
            ' টাকা এবং বৈধ পথে বৈদেশিক রেমিট্যান্স প্রেরণে উৎসাহ প্রদাণের জন্য উক্ত রেমিট্যান্স এর বিপরীতে গণপ্রজাতন্ত্রী বাংলাদেশ সরকার কর্তুক প্রদত্ত ',
            '২.৫% প্রণোদনা/নগদ সহায়তা অর্থ বাবদ ',
            { text: frIncentiveAmount, bold: true, font: 'Roboto' },
            ' টাকা সহ মোট ',
            { text: frTotalAmount, bold: true, font: 'Roboto' },
            ' টাকা ',
            { text: receiverName },
            ' কে জনতা ব্যাংক পুলহাট শাখা হতে ',
            'অদ্য ',
            frVoucherDate,
            ' ইং তারিখে পরিশোধ করা হলো।',
          ],
          alignment: 'justify',
        },
        {
          margin: [0, 20, 0, 0],
          columns: [
            {
              text: '',
              width: '40%',
            },
            [
              {
                columns: [
                  {
                    stack: [
                      // second column consists of paragraphs
                      'স্বাক্ষরঃ ',
                      'তারিখঃ ',
                      'ব্যাংক কর্মকর্তার নামঃ ',
                      'পদবীঃ ',
                    ],
                    alignment: 'right',
                  },
                  {
                    stack: [
                      // second column consists of paragraphs
                      '____________',
                      frVoucherDate,
                      '',
                      '',
                    ],
                    alignment: 'left',
                  },
                ],
              },
            ],
          ],
        },
        {
          canvas: [{ type: 'line', x1: 0, y1: 10, x2: 595, y2: 10, dash: { length: 5, space: 5 } }],
        },

        // 		{
        // 		    margin: [0,10,0,0],
        // 		    columns: [
        // 		        {
        // 		            //image
        // 		            image: 'snow',
        // 		        },
        // 		        {
        // 		            width: '60%',
        // 		            text: 'Janata Bank PLC\n' + 'Jonota Bank piL',
        // 		        }
        // 		    ]
        // 		},

        {
          text: '(Cash Debit Voucher)',
          alignment: 'center',
        },
        {
          margin: [0, 20, 0, 0],
          columns: [
            {
              width: '80%',
              text: 'Pulhat Branch',
              alignment: 'center',
              bold: true,
              font: 'Roboto',
            },
            {
              width: '20%',
              text: 'Date: ' + frVoucherDate,
              alignment: 'left',
            },
          ],
        },
        {
          margin: [0, 20, 0, 0],
          columns: [
            {
              width: '80%',
              text: 'Debit : Susp A/C ag 2.5% FR Cash Incentive (BDT14990)',
              alignment: 'left',
              bold: true,
              font: 'Roboto',
              fontSize: 12,
            },
            {
              width: '20%',
              text: '= ' + frIncentiveAmount,
              alignment: 'left',
            },
          ],
        },
        {
          margin: [0, 0, 0, 0],
          columns: [
            {
              text: [
                'BEING THE AMOUNT PAID TO ',
                { text: receiverName, bold: true, font: 'Roboto' },
                ' against 2.5% FR Cash\n',
                'Incentive Remitance No ',
                { text: frPin, bold: true, font: 'Roboto' },
                ', ',
                'ID: ',
                frIdDocument,
                '\n',
                'DATED: ',
                frVoucherDate,
              ],
            },
            {
              width: '20%',
              text: '',
            },
          ],
        },
        {
          margin: [0, 0, 0, 0],
          columns: [
            {
              width: '80%',
              text: 'Total Taka',
              alignment: 'right',
            },
            {
              width: '20%',
              text: ['= ', { text: frIncentiveAmount, bold: true, font: 'Roboto' }],
            },
          ],
        },
        {
          text: ['In Word: ', { text: frIncentiveAmountWords, bold: true, font: 'Roboto' }],
          bold: true,
        },
        {
          margin: [0, 50, 0, 0],
          columns: [
            [
              {
                canvas: [
                  {
                    lineColor: 'gray',
                    type: 'line',
                    x1: 0,
                    y1: 0,
                    x2: 60,
                    y2: 0,
                    lineWidth: 1,
                  },
                ],
                alignment: 'center',
              },
              {
                text: 'Officer',
                alignment: 'center',
              },
            ],
            [
              {
                canvas: [
                  {
                    lineColor: 'gray',
                    type: 'line',
                    x1: 0,
                    y1: 0,
                    x2: 60,
                    y2: 0,
                    lineWidth: 1,
                  },
                ],
                alignment: 'center',
              },
              {
                text: 'Manager',
                alignment: 'center',
              },
            ],
          ],
        },
      ],
      images: {
        // in browser is supported loading images via url (https or http protocol) (minimal version: 0.1.67)
        snow: 'data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxMSEhUREBATFhIVFxgVGBUXFhgVGhcYGBcXFxgYFxYYHiggGBolGxYVITEiJSkrLi4uFx8zODMtNygtLisBCgoKDg0OGxAQGy0mICUtLy0vMjYtKzItLTUtNS0tLS0wLy0uLTUtLS01LS0tLS8tLS0tLS0tLS0tLS0tLS0tLf/AABEIAOEA4QMBIgACEQEDEQH/xAAbAAEAAgMBAQAAAAAAAAAAAAAABQYDBAcCAf/EAEgQAAECAwMEDgYHBwUBAAAAAAEAAgMEEQUGIRIxQXEHExYiMlFSYYGRobHB0RQjM0JykjVTYmOCk+EVNGSDorLCJEPS8PFE/8QAGgEBAAMBAQEAAAAAAAAAAAAAAAMEBQIBBv/EAEIRAAEDAQIJCQQHCAMAAAAAAAEAAgMEESEFEjFBUZGh0fATMjNSYXGBscEUIkLhBhU0U2Jy0iMkQ1RzkqLxRILC/9oADAMBAAIRAxEAPwDuKIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiIiLDGjNYMp7mtHGSAOtQM9e+XZg0l55hQdZ8F2yN8nNBKhmqIoRbI4DvPBVkXwrnk5faO7CG1sMfMes4dihJq1o0T2kV56aDqGCuMwfIecQNqyZsP0zOYC7YNt+xdTmLRgw/aRWN1kKMjXtlW/wC4XamnvXP5ayY0TgQnnnxA6zgpSWufMu4TQzWW+Ck9kgZz37R81XGFa6boYdhO33Qp2NfmEODCcdZDVqxb9n3YA6XeSxQbiRDw47RqBd3rbg3FZ78dztTQ3xT9yb260tww85A3+z5laJv1F+qh9ZXg34jchim23JgaXPPTRe9xct95836JytH1dh3rr2fCx/iDWP0qBF94/IYvYv3F+qh9Z81N7i5b7z5v0Xh1ypfQ546apytH1dh3p7PhYfxBrH6VGwr9u96A3ocfFbcO/UP3oThqIK+RbjQ/djOGtod4hakW4b/dmAdbS3uJSyid2a1zbhiP8X9vyU1BvfKuzvLdbT3qSl7Vgv4EVh6ad6osxc2ZbwWsfqIHeoyYsaPD4cB456VHXmXvslO/mP2j1sXv1nXRdLDsd53hdbB4l9XHpa0Y0M7yI9tNAJ7sympK+cdlA8CIOcZPaPJRvwdIOaQdimh+kFO/ngt2jZfsXR0VWkb6QH4RAYZ4+EOsY9isMtMMeKw3hw4wQVTkifHzgQtaCqhn6NwPnqyjUthERRqdERERERERERERYY8ZrGl7yA1oqSdAVLta+xqWyzaDlkVPQ3R0rLsgWjTIgNOffO/xHeVDXWsH0lznPJENtK0zknQCtGngjbHysq+fr62ofUey01xznb4ADPltu74uPMxYzt+5z3HjqeoKWkLpTETFzRDHG7P1DFX+Ss6FBFIUMN59J1nOt1JMIHJG2wcZsiQ4BZbjTuLj2bzediqclcmC3GK4xDxcEdmKnpWzIML2cNo56VPWVvIqb5pH85xK2IaSCHo2AeevKvi+oiiVhERERERERERERERERERERakxIQontIbXawO9Qc3cyA7GGTDPMcodRVnRSMlezmkhQTUsM3SMB8PXKubz9zpiHiykQfZwPynOoRrosB2Bex411XY1qzclDijJisDhzjNqOcK7HhB2SQWjjwWPNgGMnGhcWnWN42qk2TfR7KNmBljlDAjwKu8nNsitD4bg5p0juPEVRrzXX2ppjQCSwcJpxLRxg6Qta5tqGFFDCd5EOSeambtwXUsEUsZki1fJRU1bUUs4p6q8HIfK/OM194XS0RFmL6RERERF8K+rVn421w3P5LSeoIvCQLyuX3kmdtmYr9FQBqGbuXQrrye1SzG0xIyjrP6UXMpKCXxGtzlxA6ziuxQ2gAAZgKDowWphA4jGxjiy5fN4CbysstQ7Kf8A17x9F7REWWvpURERERERFG2va8OWDTFyqOJAoK5hVexabNp9I32RSubGmpV7ZF9nC+J3ctmH9F/yz/crYgbybHZybFm+1Se0yx3WNZjDvszqUse2YcyHGFlb2gNRTOsMreGDEi7Q0nLqRiMKtz49Chdjrgxtbf8AJR9ifSP44nipDTMD5G9UXKuyvmMVO+617rDd25larUvFCl3hkStSAcBXOpXbMMrRSvZVc/2QP3hvwjvCvwALKHNk49Shmia2NjhlNtvgrVLUvkqJo3WWMLbPG3LqUBu0lvt9SbtJb7fyrHudkOU380eabnJDlN/N/VS4tLocq1uE+tF/ksm7SW+38qbtJb7fyrHuckOU3839U3OSHKb+b+qYtLocvbcJ9aLasu7OW43/ACrdkbwS8WgbFFTodvT1FRm5uQdg0trzRAT3rTn7jDPBi/hdp6f0XmLSm61ze9OUwm0Yxax40AkHarqvq5tJWvMyLtrihxYM7DxcbXf9Cv0hPMjMESG6rT1g8RGgqGandFYcoOQq1SV8dTa0AhwytOUca9ICzRoQcC1wqHAg6iuQz0uYUV7NLCR1HDwXY1zG+kHJmnmnCoetuPcrWDn2PLdI8lnfSCEOhbJoNngfnYuhWZMbZCZE0uaCdentW4q9ceLlSrfslze0nxVhVKVmI8t0ErYppeVhZIc4B1hERFGp0UJe+NkSsQjmH9QB7FNqr3+iUlqcp4HUD5KanFsrR2hVK55ZTSOHVPkqpdCBlzcM8kk9i6muc3Bh1mCeS13bQLoysYQNstnYFQwCzFpbdLjuXxV5l6YRmNo0VoH6CfLRVYb7WsYUMQmGj4gz8TdPXmWrc+x2wmelRaAkEivuimLtZ7lwyFgiMkme4BTTVcjqltPBZde8nIBo7zxnVyRVOy717dMbUIZMNxo0jOKaTrVsUEkTozY4K7T1MVQ0ujNoBsRERRqdU/ZF9nC+J3ctmH9F/wAs/wBy1tkX2cL4ndy2Yf0X/LP9y0B0Ef5lif8ANn/pjyC09jrgxtbf8lH2J9In44nipDY64MbW3/JR9ifSJ+OJ4qZ3STd3oqcX2ej/AD+pXy//AO8N+Ed6vxbVlONtOsKg3/8A3hvwjvV+NcnDPk4a6KtUdBF4+iv0H2yp72+RVH3BxProfylfNwcT66H1FY8u1PvOxfNttT7zsV3Gn+8askRUX8tLqP6ll3BxProfUU3BxProfUVi221PvOxNttT7zsXmPP8AeNXvJUX8tLqP6lkdcWKM0WHXUR2rT2ybkHDKygyuYmrXdKyxZ+0YQy3mIAM9WgjpwwVgsK2WTrHQozBlUxGgjwIXj3zNbbJY9uexdxQ0kj8SDHikzW59p405F7hRINowS1wo8ZxpYdBHGFWrGm3SMyYUTgE0eNHM4Lw0OkJ2ld4D8zHDTq71LbIEkCIcdujenUcQVwxrWu5O21jxd2FdSyPkjNRZZNCbHdoyasvhbpuuqoOyHDpFhu42kdRVouvOGLLQ3nPSh6DTuooPZEZvITviHYCq1ICyoDT2haWE3NmoC9uQhrhrG9Zdj2J6l7eJwPXh4K3KibHJ30YczD1F3mr2uKwWTu4ygKXBDsajj8RqJCIiKstFFUtkI+qYPt+Ctqp+yH7OHrPgrNJ0ze9UMKfY5O7ctDY7HrIh4mDvV+VF2OR6yNqb3lW61I2RBiPGcMJ7Cu60WzkDs8lBgYhtE0n8XmVQXD02fpX1ZP8ASweXepe/do5DWy7MARV1NAHB6MOxa2x3Bq+I8jMBQ6zj3LVnW+kWlkHFuWB0NbiOw9auEN5ez4WN447FktL/AGEvHPmfZb3k7N6nbvybJOWMeIN+4VPGB7rRzrWuzbkePHeC0GEcToyBoodK87IU2QIcIZnVcejALdkcmSkcsjfUBPOXHejUK9hVci2PHcLXPNgV8OxagQxuxY4Ra7t7/M+Oewizr6qVcqNMRHviOfWETjXGpPFxK6qpNFyT8Um1alJUioiEgaRbpVP2RfZwvid3LZh/Rf8ALP8ActbZF9nC+J3ctmH9F/yz/crY6CP8yzf+bP8A0x5Baex1wY2tv+Sj7E+kT8cTxUhsdcGNrb/ko+xPpH8cTxUzukm7vRU4vs9H+f1K+X//AHhvwjvXQYXBGody59sgfvDfhHeFfiSGVGemHUq1R0EXcfRX6D7ZU97fIrKi5wbdtHlO/KH/ABX39u2jyn/lN/4r36vf1m6/kvBhyE/w3/2j9S6Mi5z+3bR5T/ym/wDFP27aPKf+U3/insD+s3X8k+u4vu36hvXRXAEU0Lm9lANtECDwNsIFOTjXoR9o2hGBh+sNdAhhvaKKduvYPowMeOQH44VwYNJJXbWCnY4uIJcLAAoJJnV80fJscA04xcbrLMwvz9/ko3ZEA2yGdOSa6q4eKlr2/uArnpDVbmohn5wZI3pIaOZgzk9p6VN7IE2Gw4cAe8akcwwHapMUh0MecXlQcq1zKuoHNdY0dt1nqNa27g19F/GadQWLZCZ6lh4n97SFJXTlDDloYOcguP4jUdlFp3+H+m/EFXa62rtHWWhJGW4LLXZmDytUJseOpGeK52VpqI810Fc5uAf9Q74CujLyv6Y9wTAZ/cx3nzRERU1roqfsh+zh6z4K4Kn7Ifs4es+Cs0fTtWfhX7HJ3eoWpsc8ONqZ3lWu3W1l4vwHsVT2OT6yNqb3lXWYgh7XMOZwI6xRd1hsqCe70UOCW41A1unGG0qn7HTx61uFd6e8KPkN7amPLfn5wad693JjbVNOhOwywW9LTUDvXm9kIwJwRm+8Q8a2gYdg61dLbah7es27Ushrv3CGQ/w5L+y8n1GtZ9kQUiwjoye4qXvS3LkA5uIAhnow818vXKiZlWxoWJaMttNIIx6vBalzbVZFhmVjUOBAr7wOcaxVV2k8ix4+A3hXnNAq5YnG6ZoxTmyWceGchbVwZlpgbWDv2kkjmJwKtS53aN35iWibbLEloxBGdo5xp8V5deSeIyRXK4wweSSU3LOMkThYdi9p8IeyRiGoY4ObdcLQdFh413CR2RI7aQmV3wJNObMt6JvLM331Y/qcKd6hrKuvGjv22bqG1qamrn83MFsX4tduSJaGRhQvpmFMwXQaCWQsNthtKiMrmCasmGLjNxWg5TdZ6W/K85tjobyMedvc5Rl399aFRynn+5T9jwvQ5IviYOoXkc5G9GvMoXY+ly6K+Ia70Z+MuJ8u1C4HlpM2T0XjWFvslOecPePZn36lhv8AurMMHE1vaVf3vyGZR91tT0Bc/tY7faOTSoyg3oAz966HFYHAtIqCKEcxUVVdHE06PNW8HAunqJBncAPC3eFWBfiByH9nmvu7iByH9nmpLc3LfUjrKbm5X6kdZXFtJ1XceKlxMJddmoqN3cQOQ/s803cQOQ/s81Jbm5X6kdZTc3LfUjrKW0nVdx4piYS67NRUbCvvAJo5sRo4yAe41W/akoydgUhxMDi0g4V4iPBYZ26cu9tGsyHUwdU5+cE4qAuTHfDjulzmNajiLcaqQMjLTJDaC2+9QmWobI2CrAc19wItF+grXunNGWmTCitALjtZJztNcMeInvCz3+ki2K2LnDxp0FtMNVF5v9ByI7YjcC4A9LTn7upTV8t/JBxz1Z24FWA/9rHMPiuKoGH92qKRx6M4zT338d6mrHmhFgsiCmLcw0EYEdYUTfz91PxBfbixKyoHJc4dx8V5v8f9NrcFTjZiVIboctWeUy4OdIcpZbsvVfuD+8n4CujLnNwB/qHczD3roy6r+m8AuMB/ZB3lERFSWuiqmyEPUsP21a1XL9w6yrjyS09Zp4qelNkze9UsIi2kk/Kdl6gtjx9IsQcbR2FdAXNbixKTIHG0jsr4LpSmrxZN4BVcButpB2E7/Vc7vfKul5kR2YZZygeJwzjx6VPWnLtn5UPh8MCreYjhNP8A3iUrbNmNmIRhuwOdp4nDMVRrHtKJIRnQorTkE0c3i4iOPxUkZMzAW89m0ccXqtO1tLO8SD9jLl/C755dXVv27pW7tJMCMaNrgT7p014gs14rquDjGlRXSWDODnq3j1Ldt677Jpu3y7hluFeZ/kVB2deCPKHaozC4DDIOBbqPF2KRpL3crBzvibp44sUEjWwsFNWC1nwPGbjgEXjNZ98I0LeR2ZdMKnBw18ZUo6/UKmEJ9dYWUW3IzArFyQ77baEanBevRLNGPq/md3KN/JE+/EQexWojVhtkVSxze2y312lQM9euYj+rgtyK4UbUuPT5KQu5dctIjzVARiGE5viPgtx145KAKQaE8TBSutxVen7amZ121Q2kNPuN063f9ClAkc3FjbiNzk5VVc6GN4knk5aT4WjJb4bvA2LNeq2vSXiBAqWDDD3zmFOZT8JrZCTJcRl59b3Zhz08FjsWxIcmwxo7hlgZ+TzDjPOq5aU7En5hrIYOSMGt4hpcVyA2SyNnMbeTp44usXbnSU+NPLfO+5rR8I43Zyt24kkXxnTDhg2tDxudgeyqs1t29Dli0PBJdU4UwA0mpC2ZGVhy0EMqA1oqXHCp0kqLtCXkY7suK9rnUA9o4YDiAKrPkbNNjOBxezZvV+KnlpaURxubj5STkvy9+jasO7eX5ETqb5pu3l+RE6m+ax/sizPsfmO80/ZFmfY/Md5qTFpuq/ZvUONhLrxazuWTdvL8iJ1N803by/IidTfNY/2RZn2PzHeafsizPsfmO80xabqv2b0xsJdeLWdyxzl92ZJ2qG4v0E0AHPhWq1biyD3RHTL60FQCfeJzlSDJCzWHKqzDjiOI7TitW272tDTClRjSmVSmSOYKQC1pjgYb8pKheSx7Z62Vvu3hrdOnNwMtlqjr3x9vmhDYa5NGDTiTj3jqU3fh4ZKth6SQPkAK1Lm2EQfSYwpnLAefO41UZb04Z2abDhYtByWc+OJ/7oCkaAZWsB91gvKrvc9lPJM8WPmNjW9mTyPlpVquTCyZVp5TnHtp4LS2Q30gsHG49gVllZcMY1jczQAOhVHZFjeyZ8R8FUgPKVQd2krTrWchg4s0NDfILU2PIdYz3cTadZHkugqj7HML2zvhHeT4K8LyuNs58F1gVuLRt7bTtI9EREVRaiKKvFAy5aK2ld7Xpbj4KVWOLDygWnMQR1rprsUh2hcSMD2lhzgjXcuUXdj5EzCcTQZYr04eK60FxuNCMOIWnhNJHSD+i65JRxEhsfygD2YrRwk29rhxnHmvn/o88hskRygg+h8lsqGt6w2TLcd68cF3gRpCmUWc1xacZuVfQSRtkaWPFoK5nCmJmzomS4b0ngnFrucHQVZIFuSk20NjANdxPw6n/wDisMzLsiNLYjQ5p0EVVXtG5MN1TBeWHidvh5q7y0Ut8nuu0hY/slVSgtgIfH1XeQ47wvUe5UF+MKIWV1P6sy1dwP8AEf0fqtHcxOQidrNRxtf4L76DaZw9Z8481OHP+GYeNnqqbo4D0lG4Hsts2WDYpmBc+Wh76K8uAz1IYOn/ANX2avDKSrciXaHHksw/q09qhRdObimsVwbzlxJ6gpmzrlwm0MVxiHi4I8yonmLLLIXdg43KxEKnJTQCP8Ry6stvfaq6XTVoxKe6OhrNfH3q72HY0OWZRuLjwnnOebmHMtt9ITKQ4RIGAY2g/wDFU7UdaMV1Ww3Q2jM1rm9+krgvM4xG2MaO3i1StgZRnlXh0khzgE6sw126LrlO29ZL5loYI2QzORk1yjoqa5uZQW4H+JPyfqtL0K0/vPnHmnoVp/efOPNTRiSNuK2VoHhuVWd0E78eWmkJ/wC29bu4H+JPyfqm4H+JPyfqtL0K0/vPnHmnoVp/efOPNd4833zdm5RcjR/ykn+e9bu4H+JPyfqm4H+IPyfqtL0K0/vPnHmnoVp/efOPNMeb75uzcnI0f8pJ/nvW8Lg/xJ+T9VvQLClJQbZFcCR70Q9zdJ61B+hWn9584816gXNjxDWM5rec79cvLiP2kws7LPRdxtjYbaekON+K2wayfTvX28F53R/Uy4IY7AmmL+YDQFN3UsDaBtkQDbXDNyRxa1vWPYEGXxaKv5R8OJTCqy1DcXk4hY3aeOLrlpU1DIZfaKl2M/MMze7jbei5xfyPlTGTyGgdJxK6MuQWxNbbHiROUTTVmCkwcy2QnQPNVsPy4tOGdY+V+5Xi4MHJly4++8kahTxqrQo6wZba5eGylCG1Os4ntKkVVnfjyOd2rUo4uSp2MOYD5oiIolZREREXM77SW1zJcODEAcNeY9verJcWfy4Jhk76Gex2I7arbvVZJmIO8HrG4t5+MdK51LzEWA+rCWPGB0HUQdC1owKmnDLbxx5L5adzsH1xlstY+3blHeDf3LsSKj2ZfilBMQ/xN8W+Ss0jbECN7OICeScD1FZ8lPJHzhuW9T11PUdG4W6Mh1H/AEpJERQq2iIiIiIiIiIiIiIiIiIiIiIiIiIiIiLRnbThQRWLEDebOeoKsWnfcYiXZX7TsOoealjgkk5oVWorYKcftHAdmU6h62BTN7bQ2mXdThP3renOepUG70ht8drKb2u+1DErDPT8WYdlRHFzswHFzUGZXm51jGAwviCkR+jS0aBrOdaVgpYDf7x42L54P+s6xpA9xvpff2nRoVmX1EWQvq0REREREREUfaNkwo4pFYCePMR0hSCL0Eg2hcvY14xXC0HMqLaNxyMYEQEcl2B+YZ1Wp2zI0E+shFtNNKjrGC6+vLgDgVdjr5W86/z1rGqMBU8l7PdOsaj6EBcpkrwTELgxXEcRxHap2Uvy7NFgtPGQ7J76qyTt3peLUvhCp0jensUJN3HaamFFpzEVHWMVNy9LJz22Hu3Kt7FhKn6KTGGi30cLNRW/L3zlXcJzm621/tqpODa8B/BjM6TTsKo8zcyYbwcl45iB2KMjWFMM4UGJhppUdYXnstO/mP2j1sT6ywhF0sNvg7ztK6xDeDmIOo1XtcZo9vLBGsLPDteO3NGiDpKHBjsztn+1636RR5HxkdxB8wF16q+rk7LwzQ/+h/Tism6ab+vd8oXJwbJpG3cpR9Iafqu2b11RfKrlpvNNE124/KFjdeKaP/0O6KBefV0mkbdyH6Q0/Vds3rq68OeBnIGvBckiWzHdnjRD1rXLnuzl5PSV2MGOzu2f6UTvpFH8DCe8geQK6xGtSCzhRmDpr2BRce+MqOC8uPM0+NFRINizD+DBiHo8SpOWudMO4TQ0faIPYnslOznv2j0tT6zr5eihs7w47blIzl+TiIUEfE51eweag528kzFwMQgcQAA6xj2qwSlxR/uxieYNp2lTknduWh4iECc9XY9695ali5rbTxpXnsmE6jpX4o77NjfUrm8pIRYx9XDc8nTTvJwVjs+5LzjHeGjkjfHrzDtV7YwAUAAHEMF7UUmEJHXNu2njwVinwFBHfIS46hqG+zsUXZtiQYHs2CvKOJ69HQpREVJzi42k2rZZG2NuKwWDQEREXK7REREREREREREREREREREREREXh7Ac4B14rXiWdCdwoTD+FvkttEBIyLwgOyqOdY8uc8BnVRY9z8t9Q3rKlUXYkeM51lRmCI5WjUNyitz8t9Q3rPmvbbElx/sM71JIhkec51lBBEMjRqG5aYs6EM0GGPwN8lsw2AZgBqFF7Rckk5VIAG5EREXi9REREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREX//2Q==',
      },
      styles: {
        header: {
          fontSize: 18,
          bold: true,
        },
        bigger: {
          fontSize: 15,
          italics: true,
        },
      },
      defaultStyle: {
        columnGap: 20,
        font: 'Bangla',
      },
      info: {
        title: 'FR_INCENTIVE_CASH_DEBIT_VOUCHER_' + frPin,
      },
    };

    return frRemittanceVoucherPrint;
  }
}
