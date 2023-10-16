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
  createdBy: 'Nebraska Rubber',
  createdDate: dayjs('2023-10-04T00:59'),
};

export const sampleWithPartialData: IFrRemittance = {
  id: 86724,
  pin: 'Outdoors sensor',
  remitersName: 'Street Officer',
  amount: 'Strategist asynchronous Metal',
  incentiveAmount: 'Devolved Computers Applications',
  paymentDate: dayjs('2023-10-04'),
  incPaymentDate: dayjs('2023-10-04'),
  remiSendingDate: dayjs('2023-10-03'),
  transactionType: TransactionType['ACCOUNT'],
  recvMobileNo: 'Orchestrator Steel Open-architected',
  recvName: 'convergence users Metal',
  recvLegalId: '24/365 clicks-and-mortar matrix',
  moneyExchangeName: 'capacitor',
  recvGender: Gender['MALE'],
  remiGender: Gender['OTHERS'],
  documentType: DocumentType['PASSPORT'],
  createdBy: 'Shirt copy',
  createdDate: dayjs('2023-10-04T18:03'),
  lastModifiedDate: dayjs('2023-10-03T21:29'),
};

export const sampleWithFullData: IFrRemittance = {
  id: 76465,
  pin: 'Oklahoma bluetooth withdrawal',
  remitersName: 'programming integrated',
  amount: 'Fish Open-architected Burgs',
  incentiveAmount: 'Berkshire',
  paymentDate: dayjs('2023-10-04'),
  incPaymentDate: dayjs('2023-10-04'),
  remiSendingDate: dayjs('2023-10-03'),
  remiFrCurrency: 'Supervisor engage Samoa',
  currency: 'calculating multi-byte',
  country: 'Bahrain',
  exchangeRate: 'Expanded User',
  transactionType: TransactionType['ACCOUNT'],
  recvMobileNo: 'Practical back',
  recvName: 'hacking',
  recvLegalId: 'aggregate',
  moneyExchangeName: 'synthesize Interactions local',
  amountReimDate: dayjs('2023-10-04'),
  incAmountReimDate: dayjs('2023-10-03'),
  recvGender: Gender['FEMALE'],
  remiGender: Gender['MALE'],
  documentType: DocumentType['BIRTH_CERTIFICATE'],
  createdBy: 'Chicken payment Ohio',
  createdDate: dayjs('2023-10-04T03:23'),
  lastModifiedBy: 'Tasty Administrator',
  lastModifiedDate: dayjs('2023-10-04T17:47'),
};

export const sampleWithNewData: NewFrRemittance = {
  pin: 'reboot product',
  remitersName: 'Sleek Netherlands Public-key',
  amount: 'robust Island',
  incentiveAmount: 'Frozen Yen Lakes',
  paymentDate: dayjs('2023-10-03'),
  incPaymentDate: dayjs('2023-10-04'),
  transactionType: TransactionType['SPOT'],
  recvMobileNo: 'Wyoming Handmade',
  recvName: 'plum communities',
  recvLegalId: 'Intelligent',
  recvGender: Gender['OTHERS'],
  remiGender: Gender['FEMALE'],
  documentType: DocumentType['BIRTH_CERTIFICATE'],
  createdBy: 'Small generate',
  createdDate: dayjs('2023-10-03T20:07'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
