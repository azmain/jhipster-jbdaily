import { IUpazila, NewUpazila } from './upazila.model';

export const sampleWithRequiredData: IUpazila = {
  id: 24689,
  name: 'AGP Rubber Generic',
  bnName: 'hacking Accounts',
};

export const sampleWithPartialData: IUpazila = {
  id: 76192,
  name: 'Salad overriding',
  bnName: 'Viaduct neural',
};

export const sampleWithFullData: IUpazila = {
  id: 91356,
  name: 'Shores',
  bnName: 'indigo payment Handcrafted',
};

export const sampleWithNewData: NewUpazila = {
  name: 'auxiliary payment',
  bnName: 'Regional function compress',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
