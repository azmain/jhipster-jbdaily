import dayjs from 'dayjs/esm';

export interface IDivision {
  id: number;
  name?: string | null;
  bnName?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewDivision = Omit<IDivision, 'id'> & { id: null };
