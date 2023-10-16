import dayjs from 'dayjs/esm';

import { IDistrict, NewDistrict } from './district.model';

export const sampleWithRequiredData: IDistrict = {
  id: 85653,
  name: 'Soap invoice Dong',
  bnName: 'protocol compressing',
  createdBy: 'copy',
  createdDate: dayjs('2023-10-01T22:19'),
};

export const sampleWithPartialData: IDistrict = {
  id: 67829,
  name: 'executive tan Virtual',
  bnName: 'Streamlined Rubber',
  createdBy: 'Refined sky',
  createdDate: dayjs('2023-10-02T00:02'),
  lastModifiedDate: dayjs('2023-10-02T14:12'),
};

export const sampleWithFullData: IDistrict = {
  id: 68115,
  name: 'Pound experiences Usability',
  bnName: 'Rican Chicken action-items',
  createdBy: 'deposit installation',
  createdDate: dayjs('2023-10-02T01:04'),
  lastModifiedBy: 'collaborative Savings solid',
  lastModifiedDate: dayjs('2023-10-02T04:27'),
};

export const sampleWithNewData: NewDistrict = {
  name: 'metrics Bedfordshire',
  bnName: 'magenta port',
  createdBy: 'implement',
  createdDate: dayjs('2023-10-02T08:30'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
