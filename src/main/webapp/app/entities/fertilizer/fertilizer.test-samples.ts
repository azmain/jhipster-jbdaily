import dayjs from 'dayjs/esm';

import { IFertilizer, NewFertilizer } from './fertilizer.model';

export const sampleWithRequiredData: IFertilizer = {
  id: 82164,
  name: 'Unbranded',
  bnName: 'upward-trending deposit',
  accountNo: 'solutions',
  accountTitle: 'generate monetize',
  createdBy: 'International solid Specialist',
  createdDate: dayjs('2023-10-01T23:24'),
};

export const sampleWithPartialData: IFertilizer = {
  id: 46541,
  name: 'Intelligent Bedfordshire Valleys',
  bnName: 'invoice Rapids',
  accountNo: 'Bedfordshire e-commerce',
  accountTitle: 'Granite EXE',
  createdBy: 'Ruble application',
  createdDate: dayjs('2023-10-02T00:22'),
  lastModifiedBy: 'withdrawal even-keeled Director',
  lastModifiedDate: dayjs('2023-10-02T01:09'),
};

export const sampleWithFullData: IFertilizer = {
  id: 92777,
  name: 'Secured',
  bnName: 'communities Future Borders',
  accountNo: 'facilitate gold',
  accountTitle: 'deposit Account driver',
  createdBy: 'Automotive Auto Squares',
  createdDate: dayjs('2023-10-02T03:45'),
  lastModifiedBy: 'Specialist Korea',
  lastModifiedDate: dayjs('2023-10-02T04:20'),
};

export const sampleWithNewData: NewFertilizer = {
  name: 'Soap Ridges',
  bnName: 'aggregate',
  accountNo: 'Via',
  accountTitle: 'Intelligent SSL Awesome',
  createdBy: 'Lead Generic red',
  createdDate: dayjs('2023-10-02T11:25'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
