import dayjs from 'dayjs/esm';

import { IDealer, NewDealer } from './dealer.model';

export const sampleWithRequiredData: IDealer = {
  id: 47955,
  name: 'Movies Montana Bedfordshire',
  bnName: 'generation Sports',
  shortName: 'maroon Wooden',
  createdBy: 'bypass Solomon',
  createdDate: dayjs('2023-10-01T21:45'),
};

export const sampleWithPartialData: IDealer = {
  id: 58130,
  name: 'Lead Metal',
  bnName: 'partnerships',
  shortName: 'Bridge',
  mobile: 'Kenyan',
  createdBy: 'navigate Health',
  createdDate: dayjs('2023-10-02T11:00'),
  lastModifiedBy: 'Directives recontextualize',
  lastModifiedDate: dayjs('2023-10-02T15:27'),
};

export const sampleWithFullData: IDealer = {
  id: 91666,
  name: 'Interactions Strategist',
  bnName: 'deposit SCSI',
  shortName: 'Club',
  mobile: 'Northern Borders Consultant',
  createdBy: 'capacitor',
  createdDate: dayjs('2023-10-02T17:23'),
  lastModifiedBy: 'Islands Soft auxiliary',
  lastModifiedDate: dayjs('2023-10-02T05:22'),
};

export const sampleWithNewData: NewDealer = {
  name: 'Missouri connecting',
  bnName: 'Table',
  shortName: 'concept',
  createdBy: 'Human haptic Nebraska',
  createdDate: dayjs('2023-10-02T04:36'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
