import dayjs from 'dayjs/esm';

export interface IUserSettings {
  id: number;
  name?: string | null;
  payOrderNumSeq?: string | null;
  payOrderControlNum?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
}

export type NewUserSettings = Omit<IUserSettings, 'id'> & { id: null };
