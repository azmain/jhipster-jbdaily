import { IDistrict } from 'app/entities/district/district.model';

export interface IUpazila {
  id: number;
  name?: string | null;
  bnName?: string | null;
  district?: Pick<IDistrict, 'id'> | null;
}

export type NewUpazila = Omit<IUpazila, 'id'> & { id: null };
