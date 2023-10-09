import dayjs from 'dayjs/esm';
import { IFertilizer } from 'app/entities/fertilizer/fertilizer.model';
import { IDealer } from 'app/entities/dealer/dealer.model';

export interface IPayOrder {
  id: number;
  payOrderNumber?: number | null;
  payOrderDate?: dayjs.Dayjs | null;
  amount?: number | null;
  slipNo?: number | null;
  controllingNo?: number | null;
  fertilizer?: IFertilizer | null;
  dealer?: IDealer | null;
}

export type NewPayOrder = Omit<IPayOrder, 'id'> & { id: null };
