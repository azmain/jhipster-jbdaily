import { IDivision, NewDivision } from './division.model';

export const sampleWithRequiredData: IDivision = {
  id: 46356,
  name: 'azure',
  bnName: 'network blue enable',
};

export const sampleWithPartialData: IDivision = {
  id: 5620,
  name: 'Electronics Jewelery',
  bnName: 'Music',
};

export const sampleWithFullData: IDivision = {
  id: 77585,
  name: 'Turnpike Cotton',
  bnName: 'Granite',
};

export const sampleWithNewData: NewDivision = {
  name: 'Lesotho Electronics',
  bnName: 'RSS',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
