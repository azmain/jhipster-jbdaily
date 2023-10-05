import { IMoneyExchange, NewMoneyExchange } from './money-exchange.model';

export const sampleWithRequiredData: IMoneyExchange = {
  id: 24778,
  name: 'aggregate Trail homogeneous',
  digit: 'innovative Croatia system',
};

export const sampleWithPartialData: IMoneyExchange = {
  id: 69647,
  name: 'application',
  digit: 'Hat Concrete PCI',
};

export const sampleWithFullData: IMoneyExchange = {
  id: 96473,
  name: 'Franc customized',
  digit: 'XML',
  link: 'Islands Reunion',
  shortName: 'Infrastructure overriding transmitting',
};

export const sampleWithNewData: NewMoneyExchange = {
  name: 'Cotton',
  digit: 'Director',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
