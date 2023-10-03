import { IDealer, NewDealer } from './dealer.model';

export const sampleWithRequiredData: IDealer = {
  id: 47955,
  name: 'Movies Montana Bedfordshire',
  bnName: 'generation Sports',
  shortName: 'maroon Wooden',
};

export const sampleWithPartialData: IDealer = {
  id: 75362,
  name: 'payment',
  bnName: 'SAS',
  shortName: 'Michigan Sports Saudi',
};

export const sampleWithFullData: IDealer = {
  id: 44331,
  name: 'Bike',
  bnName: 'Advanced',
  shortName: 'Tuna killer',
  mobile: 'Polynesia Directives',
};

export const sampleWithNewData: NewDealer = {
  name: 'Producer National',
  bnName: 'Rand navigate Towels',
  shortName: 'utilize Generic Northern',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
