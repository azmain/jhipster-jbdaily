import dayjs from 'dayjs/esm';

export interface IMoneyExchange {
  id: number;
  name?: string | null;
  digit?: string | null;
  link?: string | null;
  shortName?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewMoneyExchange = Omit<IMoneyExchange, 'id'> & { id: null };
