import dayjs from 'dayjs/esm';
import { IMoneyExchange } from 'app/entities/money-exchange/money-exchange.model';
import { IIncPercentage } from 'app/entities/inc-percentage/inc-percentage.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { DocumentType } from 'app/entities/enumerations/document-type.model';

export interface IFrRemittance {
  id: number;
  pin?: string | null;
  remitersName?: string | null;
  amount?: string | null;
  incentiveAmount?: string | null;
  paymentDate?: dayjs.Dayjs | null;
  incPaymentDate?: dayjs.Dayjs | null;
  remiSendingDate?: dayjs.Dayjs | null;
  remiFrCurrency?: string | null;
  currency?: string | null;
  country?: string | null;
  exchangeRate?: string | null;
  transactionType?: TransactionType | null;
  recvMobileNo?: string | null;
  recvName?: string | null;
  recvLegalId?: string | null;
  moneyExchangeName?: string | null;
  amountReimDate?: dayjs.Dayjs | null;
  incAmountReimDate?: dayjs.Dayjs | null;
  recvGender?: Gender | null;
  remiGender?: Gender | null;
  documentType?: DocumentType | null;
  moneyExchange?: Pick<IMoneyExchange, 'id'> | null;
  incPercentage?: Pick<IIncPercentage, 'id'> | null;
}

export type NewFrRemittance = Omit<IFrRemittance, 'id'> & { id: null };
