import dayjs from 'dayjs/esm';
import { IUpazila } from 'app/entities/upazila/upazila.model';

export interface IDealer {
  id: number;
  name?: string | null;
  bnName?: string | null;
  shortName?: string | null;
  mobile?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  upazila?: Pick<IUpazila, 'id' | 'name'> | null;
}

export type NewDealer = Omit<IDealer, 'id'> & { id: null };
