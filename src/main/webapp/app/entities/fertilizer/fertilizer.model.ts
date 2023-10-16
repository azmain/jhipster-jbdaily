import dayjs from 'dayjs/esm';

export interface IFertilizer {
  id: number;
  name?: string | null;
  bnName?: string | null;
  accountNo?: string | null;
  accountTitle?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewFertilizer = Omit<IFertilizer, 'id'> & { id: null };
