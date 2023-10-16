import dayjs from 'dayjs/esm';

import { IUpazila, NewUpazila } from './upazila.model';

export const sampleWithRequiredData: IUpazila = {
  id: 24689,
  name: 'AGP Rubber Generic',
  bnName: 'hacking Accounts',
  createdBy: 'Florida neural Handcrafted',
  createdDate: dayjs('2023-10-01T19:55'),
};

export const sampleWithPartialData: IUpazila = {
  id: 80255,
  name: 'Senior input indigo',
  bnName: 'Kentucky Pataca',
  createdBy: 'South',
  createdDate: dayjs('2023-10-02T14:01'),
  lastModifiedBy: 'Islands Regional',
  lastModifiedDate: dayjs('2023-10-02T10:47'),
};

export const sampleWithFullData: IUpazila = {
  id: 38249,
  name: 'compress Hawaii',
  bnName: "Liaison Customer People's",
  createdBy: 'robust Dollar',
  createdDate: dayjs('2023-10-02T09:03'),
  lastModifiedBy: 'transmit',
  lastModifiedDate: dayjs('2023-10-02T17:44'),
};

export const sampleWithNewData: NewUpazila = {
  name: 'Solomon',
  bnName: 'interface wireless back-end',
  createdBy: 'Account sensor',
  createdDate: dayjs('2023-10-02T14:26'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
