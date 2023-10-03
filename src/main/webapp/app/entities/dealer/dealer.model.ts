import { IUpazila } from 'app/entities/upazila/upazila.model';

export interface IDealer {
  id: number;
  name?: string | null;
  bnName?: string | null;
  shortName?: string | null;
  mobile?: string | null;
  upazila?: Pick<IUpazila, 'id'> | null;
}

export type NewDealer = Omit<IDealer, 'id'> & { id: null };
