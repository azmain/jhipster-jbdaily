import { IDivision } from 'app/entities/division/division.model';

export interface IDistrict {
  id: number;
  name?: string | null;
  bnName?: string | null;
  division?: Pick<IDivision, 'id'> | null;
}

export type NewDistrict = Omit<IDistrict, 'id'> & { id: null };
