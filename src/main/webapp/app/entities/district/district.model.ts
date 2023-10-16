import dayjs from 'dayjs/esm';
import { IDivision } from 'app/entities/division/division.model';

export interface IDistrict {
  id: number;
  name?: string | null;
  bnName?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  division?: Pick<IDivision, 'id' | 'name'> | null;
}

export type NewDistrict = Omit<IDistrict, 'id'> & { id: null };
