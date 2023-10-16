import dayjs from 'dayjs/esm';

export interface IIncPercentage {
  id: number;
  name?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewIncPercentage = Omit<IIncPercentage, 'id'> & { id: null };
