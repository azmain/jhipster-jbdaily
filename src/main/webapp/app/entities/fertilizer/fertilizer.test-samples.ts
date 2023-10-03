import { IFertilizer, NewFertilizer } from './fertilizer.model';

export const sampleWithRequiredData: IFertilizer = {
  id: 82164,
  name: 'Unbranded',
  bnName: 'upward-trending deposit',
  accountNo: 'solutions',
  accountTitle: 'generate monetize',
};

export const sampleWithPartialData: IFertilizer = {
  id: 89707,
  name: 'Convertible Lead protocol',
  bnName: 'Berkshire Intelligent Bedfordshire',
  accountNo: 'Global invoice',
  accountTitle: 'Tala Bedfordshire',
};

export const sampleWithFullData: IFertilizer = {
  id: 44022,
  name: 'synthesize Designer',
  bnName: 'EXE payment',
  accountNo: 'application synthesize orchestrate',
  accountTitle: 'Personal payment',
};

export const sampleWithNewData: NewFertilizer = {
  name: 'Secured',
  bnName: 'communities Future Borders',
  accountNo: 'facilitate gold',
  accountTitle: 'deposit Account driver',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
