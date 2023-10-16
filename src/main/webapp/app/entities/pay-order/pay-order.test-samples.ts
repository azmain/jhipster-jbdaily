import dayjs from 'dayjs/esm';

import { IPayOrder, NewPayOrder } from './pay-order.model';

export const sampleWithRequiredData: IPayOrder = {
  id: 94017,
  payOrderNumber: 51015,
  payOrderDate: dayjs('2023-10-01'),
  amount: 45147,
  slipNo: 88227,
  controllingNo: 30257,
  createdBy: 'Loan',
  createdDate: dayjs('2023-10-02T02:50'),
};

export const sampleWithPartialData: IPayOrder = {
  id: 47765,
  payOrderNumber: 5636,
  payOrderDate: dayjs('2023-10-02'),
  amount: 25410,
  slipNo: 28972,
  controllingNo: 16231,
  createdBy: 'ADP Coordinator Locks',
  createdDate: dayjs('2023-10-02T10:33'),
  lastModifiedBy: 'Australian Buckinghamshire',
  lastModifiedDate: dayjs('2023-10-02T12:10'),
};

export const sampleWithFullData: IPayOrder = {
  id: 10880,
  payOrderNumber: 36705,
  payOrderDate: dayjs('2023-10-02'),
  amount: 6516,
  slipNo: 22142,
  controllingNo: 74098,
  createdBy: 'Movies',
  createdDate: dayjs('2023-10-01T19:44'),
  lastModifiedBy: 'impactful',
  lastModifiedDate: dayjs('2023-10-01T20:40'),
};

export const sampleWithNewData: NewPayOrder = {
  payOrderNumber: 19314,
  payOrderDate: dayjs('2023-10-01'),
  amount: 1433,
  slipNo: 69588,
  controllingNo: 10959,
  createdBy: 'Louisiana deposit Soap',
  createdDate: dayjs('2023-10-02T17:59'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
