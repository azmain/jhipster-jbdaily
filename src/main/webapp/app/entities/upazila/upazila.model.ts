import dayjs from 'dayjs/esm';
import { IDistrict } from 'app/entities/district/district.model';

export interface IUpazila {
  id: number;
  name?: string | null;
  bnName?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  district?: Pick<IDistrict, 'id' | 'name' | 'bnName'> | null;
}

export type NewUpazila = Omit<IUpazila, 'id'> & { id: null };
