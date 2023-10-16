import dayjs from 'dayjs/esm';

import { IUserSettings, NewUserSettings } from './user-settings.model';

export const sampleWithRequiredData: IUserSettings = {
  id: 11912,
  name: 'Consultant',
  payOrderNumSeq: 'Argentina Dynamic',
  payOrderControlNum: 'Central',
  createdBy: 'hierarchy Montana Books',
  createdDate: dayjs('2023-10-16T05:07'),
};

export const sampleWithPartialData: IUserSettings = {
  id: 22523,
  name: 'Keys Soft backing',
  payOrderNumSeq: 'Pants infrastructures Florida',
  payOrderControlNum: 'Architect Baby',
  createdBy: 'lime',
  createdDate: dayjs('2023-10-16T04:15'),
  lastModifiedDate: dayjs('2023-10-16T02:25'),
};

export const sampleWithFullData: IUserSettings = {
  id: 92982,
  name: 'Kina azure',
  payOrderNumSeq: 'withdrawal IB Synchronised',
  payOrderControlNum: 'firmware hack hub',
  createdBy: "People's Delaware Kentucky",
  createdDate: dayjs('2023-10-16T12:12'),
  lastModifiedBy: 'throughput port Baby',
  lastModifiedDate: dayjs('2023-10-15T18:09'),
};

export const sampleWithNewData: NewUserSettings = {
  name: 'Loan',
  payOrderNumSeq: 'Human web-enabled hacking',
  payOrderControlNum: 'proactive parallelism violet',
  createdBy: 'Minnesota Automotive synergize',
  createdDate: dayjs('2023-10-16T02:35'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
