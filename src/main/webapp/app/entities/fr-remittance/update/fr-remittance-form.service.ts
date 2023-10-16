import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFrRemittance, NewFrRemittance } from '../fr-remittance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFrRemittance for edit and NewFrRemittanceFormGroupInput for create.
 */
type FrRemittanceFormGroupInput = IFrRemittance | PartialWithRequiredKeyOf<NewFrRemittance>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFrRemittance | NewFrRemittance> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type FrRemittanceFormRawValue = FormValueOf<IFrRemittance>;

type NewFrRemittanceFormRawValue = FormValueOf<NewFrRemittance>;

type FrRemittanceFormDefaults = Pick<NewFrRemittance, 'id' | 'createdDate' | 'lastModifiedDate'>;

type FrRemittanceFormGroupContent = {
  id: FormControl<FrRemittanceFormRawValue['id'] | NewFrRemittance['id']>;
  pin: FormControl<FrRemittanceFormRawValue['pin']>;
  remitersName: FormControl<FrRemittanceFormRawValue['remitersName']>;
  amount: FormControl<FrRemittanceFormRawValue['amount']>;
  incentiveAmount: FormControl<FrRemittanceFormRawValue['incentiveAmount']>;
  paymentDate: FormControl<FrRemittanceFormRawValue['paymentDate']>;
  incPaymentDate: FormControl<FrRemittanceFormRawValue['incPaymentDate']>;
  remiSendingDate: FormControl<FrRemittanceFormRawValue['remiSendingDate']>;
  remiFrCurrency: FormControl<FrRemittanceFormRawValue['remiFrCurrency']>;
  currency: FormControl<FrRemittanceFormRawValue['currency']>;
  country: FormControl<FrRemittanceFormRawValue['country']>;
  exchangeRate: FormControl<FrRemittanceFormRawValue['exchangeRate']>;
  transactionType: FormControl<FrRemittanceFormRawValue['transactionType']>;
  recvMobileNo: FormControl<FrRemittanceFormRawValue['recvMobileNo']>;
  recvName: FormControl<FrRemittanceFormRawValue['recvName']>;
  recvLegalId: FormControl<FrRemittanceFormRawValue['recvLegalId']>;
  moneyExchangeName: FormControl<FrRemittanceFormRawValue['moneyExchangeName']>;
  amountReimDate: FormControl<FrRemittanceFormRawValue['amountReimDate']>;
  incAmountReimDate: FormControl<FrRemittanceFormRawValue['incAmountReimDate']>;
  recvGender: FormControl<FrRemittanceFormRawValue['recvGender']>;
  remiGender: FormControl<FrRemittanceFormRawValue['remiGender']>;
  documentType: FormControl<FrRemittanceFormRawValue['documentType']>;
  createdBy: FormControl<FrRemittanceFormRawValue['createdBy']>;
  createdDate: FormControl<FrRemittanceFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<FrRemittanceFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<FrRemittanceFormRawValue['lastModifiedDate']>;
  moneyExchange: FormControl<FrRemittanceFormRawValue['moneyExchange']>;
  incPercentage: FormControl<FrRemittanceFormRawValue['incPercentage']>;
};

export type FrRemittanceFormGroup = FormGroup<FrRemittanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FrRemittanceFormService {
  createFrRemittanceFormGroup(frRemittance: FrRemittanceFormGroupInput = { id: null }): FrRemittanceFormGroup {
    const frRemittanceRawValue = this.convertFrRemittanceToFrRemittanceRawValue({
      ...this.getFormDefaults(),
      ...frRemittance,
    });
    return new FormGroup<FrRemittanceFormGroupContent>({
      id: new FormControl(
        { value: frRemittanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      pin: new FormControl(frRemittanceRawValue.pin, {
        validators: [Validators.required, Validators.maxLength(256)],
      }),
      remitersName: new FormControl(frRemittanceRawValue.remitersName, {
        validators: [Validators.required, Validators.maxLength(256)],
      }),
      amount: new FormControl(frRemittanceRawValue.amount, {
        validators: [Validators.required, Validators.maxLength(256)],
      }),
      incentiveAmount: new FormControl(frRemittanceRawValue.incentiveAmount, {
        validators: [Validators.required, Validators.maxLength(256)],
      }),
      paymentDate: new FormControl(frRemittanceRawValue.paymentDate, {
        validators: [Validators.required],
      }),
      incPaymentDate: new FormControl(frRemittanceRawValue.incPaymentDate, {
        validators: [Validators.required],
      }),
      remiSendingDate: new FormControl(frRemittanceRawValue.remiSendingDate),
      remiFrCurrency: new FormControl(frRemittanceRawValue.remiFrCurrency, {
        validators: [Validators.maxLength(256)],
      }),
      currency: new FormControl(frRemittanceRawValue.currency, {
        validators: [Validators.maxLength(256)],
      }),
      country: new FormControl(frRemittanceRawValue.country, {
        validators: [Validators.maxLength(256)],
      }),
      exchangeRate: new FormControl(frRemittanceRawValue.exchangeRate, {
        validators: [Validators.maxLength(256)],
      }),
      transactionType: new FormControl(frRemittanceRawValue.transactionType, {
        validators: [Validators.required],
      }),
      recvMobileNo: new FormControl(frRemittanceRawValue.recvMobileNo, {
        validators: [Validators.required, Validators.maxLength(256)],
      }),
      recvName: new FormControl(frRemittanceRawValue.recvName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      recvLegalId: new FormControl(frRemittanceRawValue.recvLegalId, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      moneyExchangeName: new FormControl(frRemittanceRawValue.moneyExchangeName, {
        validators: [Validators.maxLength(256)],
      }),
      amountReimDate: new FormControl(frRemittanceRawValue.amountReimDate),
      incAmountReimDate: new FormControl(frRemittanceRawValue.incAmountReimDate),
      recvGender: new FormControl(frRemittanceRawValue.recvGender, {
        validators: [Validators.required],
      }),
      remiGender: new FormControl(frRemittanceRawValue.remiGender, {
        validators: [Validators.required],
      }),
      documentType: new FormControl(frRemittanceRawValue.documentType, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(frRemittanceRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(frRemittanceRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedBy: new FormControl(frRemittanceRawValue.lastModifiedBy, {
        validators: [Validators.maxLength(50)],
      }),
      lastModifiedDate: new FormControl(frRemittanceRawValue.lastModifiedDate),
      moneyExchange: new FormControl(frRemittanceRawValue.moneyExchange, {
        validators: [Validators.required],
      }),
      incPercentage: new FormControl(frRemittanceRawValue.incPercentage, {
        validators: [Validators.required],
      }),
    });
  }

  getFrRemittance(form: FrRemittanceFormGroup): IFrRemittance | NewFrRemittance {
    return this.convertFrRemittanceRawValueToFrRemittance(form.getRawValue() as FrRemittanceFormRawValue | NewFrRemittanceFormRawValue);
  }

  resetForm(form: FrRemittanceFormGroup, frRemittance: FrRemittanceFormGroupInput): void {
    const frRemittanceRawValue = this.convertFrRemittanceToFrRemittanceRawValue({ ...this.getFormDefaults(), ...frRemittance });
    form.reset(
      {
        ...frRemittanceRawValue,
        id: { value: frRemittanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FrRemittanceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertFrRemittanceRawValueToFrRemittance(
    rawFrRemittance: FrRemittanceFormRawValue | NewFrRemittanceFormRawValue
  ): IFrRemittance | NewFrRemittance {
    return {
      ...rawFrRemittance,
      createdDate: dayjs(rawFrRemittance.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawFrRemittance.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertFrRemittanceToFrRemittanceRawValue(
    frRemittance: IFrRemittance | (Partial<NewFrRemittance> & FrRemittanceFormDefaults)
  ): FrRemittanceFormRawValue | PartialWithRequiredKeyOf<NewFrRemittanceFormRawValue> {
    return {
      ...frRemittance,
      createdDate: frRemittance.createdDate ? frRemittance.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: frRemittance.lastModifiedDate ? frRemittance.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
