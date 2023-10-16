import dayjs from 'dayjs/esm';

import { IIncPercentage, NewIncPercentage } from './inc-percentage.model';

export const sampleWithRequiredData: IIncPercentage = {
  id: 49984,
  name: 'program Unbranded',
  createdBy: 'deposit salmon grey',
  createdDate: dayjs('2023-10-03T22:49'),
};

export const sampleWithPartialData: IIncPercentage = {
  id: 53140,
  name: "Row North People's",
  createdBy: 'virtual Malaysia',
  createdDate: dayjs('2023-10-04T04:52'),
  lastModifiedDate: dayjs('2023-10-04T02:55'),
};

export const sampleWithFullData: IIncPercentage = {
  id: 21772,
  name: 'Analyst',
  createdBy: 'niches Indonesia',
  createdDate: dayjs('2023-10-04T01:06'),
  lastModifiedBy: 'Steel',
  lastModifiedDate: dayjs('2023-10-04T06:31'),
};

export const sampleWithNewData: NewIncPercentage = {
  name: 'Buckinghamshire',
  createdBy: 'Intelligent state Operations',
  createdDate: dayjs('2023-10-04T05:48'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
