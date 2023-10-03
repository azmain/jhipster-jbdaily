import { IDistrict, NewDistrict } from './district.model';

export const sampleWithRequiredData: IDistrict = {
  id: 85653,
  name: 'Soap invoice Dong',
  bnName: 'protocol compressing',
};

export const sampleWithPartialData: IDistrict = {
  id: 29280,
  name: 'collaboration extend compressing',
  bnName: 'tan',
};

export const sampleWithFullData: IDistrict = {
  id: 24114,
  name: 'Generic transmitting Checking',
  bnName: 'transmitting',
};

export const sampleWithNewData: NewDistrict = {
  name: 'ivory Dollar',
  bnName: 'Italy Frozen defect',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
