import dayjs from 'dayjs/esm';
import { IFertilizer } from 'app/entities/fertilizer/fertilizer.model';
import { IDealer } from 'app/entities/dealer/dealer.model';
import { IUpazila } from '../upazila/upazila.model';

export interface IPayOrder {
  id: number;
  payOrderNumber?: number | null;
  payOrderDate?: dayjs.Dayjs | null;
  amount?: number | null;
  slipNo?: number | null;
  controllingNo?: number | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  fertilizer?: Pick<IFertilizer, 'id' | 'name'> | null;
  dealer?: Pick<IDealer, 'id' | 'name' | 'shortName'> | null;
}

export type NewPayOrder = Omit<IPayOrder, 'id'> & { id: null };

export class MICRPayOrder {
  payOrderDate?: string;

  payOrderNumber?: string;
  payOrderNoBangla?: string;

  amount?: string;
  amountInBangla?: string;
  amountInBanglaWords?: string;

  dealer?: Pick<IDealer, 'id' | 'name' | 'shortName' | 'bnName'>;
  upazila?: Pick<IUpazila, 'id' | 'name' | 'bnName'>;
  district?: Pick<IUpazila, 'id' | 'name' | 'bnName'>;
  fertilizer?: Pick<IFertilizer, 'id' | 'name' | 'bnName' | 'accountTitle' | 'accountNo'>;

  controllingNo: string;
  controllingNoBangla?: string;

  letterDate?: string;
  letterDateBangla?: [string, string, string];

  slipNo?: string;
  slipNoBangla?: string;

  fertilizerAmount?: string;

  constructor() {
    this.payOrderDate = '';

    this.payOrderNumber = '';
    this.payOrderNoBangla = '';

    this.amount = '';
    this.amountInBangla = '';
    this.amountInBanglaWords = '';

    this.dealer = { id: 0, name: '', shortName: '', bnName: '' };
    this.upazila = { id: 0, name: '', bnName: '' };
    this.district = { id: 0, name: '', bnName: '' };
    this.fertilizer = { id: 0, name: '', bnName: '', accountTitle: '', accountNo: '' };

    this.controllingNo = '';
    this.controllingNoBangla = '';

    this.letterDate = '';
    this.letterDateBangla = ['', '', ''];

    this.slipNo = '';
    this.slipNoBangla = '';

    this.fertilizerAmount = '';
  }
}
