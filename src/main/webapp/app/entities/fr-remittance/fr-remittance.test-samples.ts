import dayjs from 'dayjs/esm';

import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { DocumentType } from 'app/entities/enumerations/document-type.model';

import { IFrRemittance, NewFrRemittance } from './fr-remittance.model';

export const sampleWithRequiredData: IFrRemittance = {
  id: 17566,
  pin: 'overriding Tuvalu',
  remitersName: 'Hawaii plug-and-play intermediate',
  amount: 'Tasty payment Bridge',
  incentiveAmount: 'Keyboard',
  paymentDate: dayjs('2023-10-04'),
  incPaymentDate: dayjs('2023-10-04'),
  transactionType: TransactionType['ACCOUNT'],
  recvMobileNo: 'synthesizing Pennsylvania Ergonomic',
  recvName: 'Handcrafted circuit withdrawal',
  recvLegalId: 'Bacon',
  recvGender: Gender['MALE'],
  remiGender: Gender['MALE'],
  documentType: DocumentType['PASSPORT'],
};

export const sampleWithPartialData: IFrRemittance = {
  id: 99410,
  pin: 'Wooden RAM',
  remitersName: 'Human',
  amount: 'JSON',
  incentiveAmount: 'Investment compressing',
  paymentDate: dayjs('2023-10-04'),
  incPaymentDate: dayjs('2023-10-03'),
  remiSendingDate: dayjs('2023-10-04'),
  remiFrCurrency: 'Administrator Awesome invoice',
  currency: 'Chips',
  country: 'Ecuador',
  transactionType: TransactionType['SPOT'],
  recvMobileNo: 'Garden methodologies matrix',
  recvName: 'Steel Open-architected',
  recvLegalId: 'convergence users Metal',
  moneyExchangeName: '24/365 clicks-and-mortar matrix',
  amountReimDate: dayjs('2023-10-04'),
  incAmountReimDate: dayjs('2023-10-04'),
  recvGender: Gender['OTHERS'],
  remiGender: Gender['OTHERS'],
  documentType: DocumentType['PASSPORT'],
};

export const sampleWithFullData: IFrRemittance = {
  id: 94984,
  pin: 'deposit',
  remitersName: 'copy Outdoors',
  amount: 'Oklahoma bluetooth withdrawal',
  incentiveAmount: 'programming integrated',
  paymentDate: dayjs('2023-10-04'),
  incPaymentDate: dayjs('2023-10-04'),
  remiSendingDate: dayjs('2023-10-04'),
  remiFrCurrency: 'Open-architected Burgs Health',
  currency: 'input mobile interface',
  country: 'Pakistan',
  exchangeRate: 'Shoes calculating',
  transactionType: TransactionType['ACCOUNT'],
  recvMobileNo: 'actuating Expanded',
  recvName: 'convergence',
  recvLegalId: 'Practical back',
  moneyExchangeName: 'hacking',
  amountReimDate: dayjs('2023-10-04'),
  incAmountReimDate: dayjs('2023-10-04'),
  recvGender: Gender['MALE'],
  remiGender: Gender['OTHERS'],
  documentType: DocumentType['DRIVING_LICENCE'],
};

export const sampleWithNewData: NewFrRemittance = {
  pin: 'Israel Future',
  remitersName: 'wireless Account',
  amount: 'Chicken payment Ohio',
  incentiveAmount: 'Barbados redefine',
  paymentDate: dayjs('2023-10-04'),
  incPaymentDate: dayjs('2023-10-04'),
  transactionType: TransactionType['SPOT'],
  recvMobileNo: 'Principal quantify Sleek',
  recvName: 'Personal Euro',
  recvLegalId: 'Cheese Pula',
  recvGender: Gender['FEMALE'],
  remiGender: Gender['OTHERS'],
  documentType: DocumentType['PASSPORT'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
