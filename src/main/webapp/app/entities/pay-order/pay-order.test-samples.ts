import dayjs from 'dayjs/esm';

import { IPayOrder, NewPayOrder } from './pay-order.model';

export const sampleWithRequiredData: IPayOrder = {
  id: 94017,
  payOrderNumber: 51015,
  payOrderDate: dayjs('2023-10-01'),
  amount: 45147,
  slipNo: 88227,
  controllingNo: 30257,
};

export const sampleWithPartialData: IPayOrder = {
  id: 2769,
  payOrderNumber: 59677,
  payOrderDate: dayjs('2023-10-01'),
  amount: 35845,
  slipNo: 64341,
  controllingNo: 81812,
};

export const sampleWithFullData: IPayOrder = {
  id: 62089,
  payOrderNumber: 47765,
  payOrderDate: dayjs('2023-10-02'),
  amount: 48424,
  slipNo: 25410,
  controllingNo: 28972,
};

export const sampleWithNewData: NewPayOrder = {
  payOrderNumber: 16231,
  payOrderDate: dayjs('2023-10-01'),
  amount: 85630,
  slipNo: 43156,
  controllingNo: 49739,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
