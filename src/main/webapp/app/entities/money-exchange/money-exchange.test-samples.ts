import dayjs from 'dayjs/esm';

import { IMoneyExchange, NewMoneyExchange } from './money-exchange.model';

export const sampleWithRequiredData: IMoneyExchange = {
  id: 24778,
  name: 'aggregate Trail homogeneous',
  digit: 'innovative Croatia system',
  createdBy: 'open application',
  createdDate: dayjs('2023-10-04T00:35'),
};

export const sampleWithPartialData: IMoneyExchange = {
  id: 22942,
  name: 'PCI',
  digit: 'Buckinghamshire copying Ac',
  shortName: 'redundant methodical',
  createdBy: 'invoice Oregon violet',
  createdDate: dayjs('2023-10-03T22:03'),
  lastModifiedBy: 'green grow',
};

export const sampleWithFullData: IMoneyExchange = {
  id: 2338,
  name: 'robust Malawi neural-net',
  digit: 'data-warehouse bypassing',
  link: 'index back Front-line',
  shortName: 'microchip Towels Incredible',
  createdBy: 'high-level Digitized',
  createdDate: dayjs('2023-10-04T03:09'),
  lastModifiedBy: 'XML quantifying',
  lastModifiedDate: dayjs('2023-10-04T07:37'),
};

export const sampleWithNewData: NewMoneyExchange = {
  name: 'Investment cross-platform',
  digit: 'Unit Shirt',
  createdBy: 'AGP Engineer',
  createdDate: dayjs('2023-10-03T23:52'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
