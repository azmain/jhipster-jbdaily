import { IIncPercentage, NewIncPercentage } from './inc-percentage.model';

export const sampleWithRequiredData: IIncPercentage = {
  id: 49984,
  name: 'program Unbranded',
};

export const sampleWithPartialData: IIncPercentage = {
  id: 82340,
  name: 'Clothing Singapore',
};

export const sampleWithFullData: IIncPercentage = {
  id: 35522,
  name: 'hacking Arab port',
};

export const sampleWithNewData: NewIncPercentage = {
  name: 'Granite virtual Malaysia',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
