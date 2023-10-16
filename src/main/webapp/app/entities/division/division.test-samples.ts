import dayjs from 'dayjs/esm';

import { IDivision, NewDivision } from './division.model';

export const sampleWithRequiredData: IDivision = {
  id: 46356,
  name: 'azure',
  bnName: 'network blue enable',
  createdBy: 'Albania',
  createdDate: dayjs('2023-10-02T09:51'),
};

export const sampleWithPartialData: IDivision = {
  id: 7027,
  name: 'Music',
  bnName: 'Account Belize microchip',
  createdBy: 'edge Cambridgeshire',
  createdDate: dayjs('2023-10-02T13:41'),
  lastModifiedDate: dayjs('2023-10-02T09:45'),
};

export const sampleWithFullData: IDivision = {
  id: 27302,
  name: 'Isle multi-byte indexing',
  bnName: 'withdrawal',
  createdBy: 'Slovenia',
  createdDate: dayjs('2023-10-02T17:41'),
  lastModifiedBy: 'Director Wyoming transmitter',
  lastModifiedDate: dayjs('2023-10-01T21:32'),
};

export const sampleWithNewData: NewDivision = {
  name: 'Agent orchid',
  bnName: 'ubiquitous directional clicks-and-mortar',
  createdBy: 'global Avon',
  createdDate: dayjs('2023-10-02T09:24'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
