import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type FrRemittanceFormDefaults = Pick<NewFrRemittance, 'id'>;

type FrRemittanceFormGroupContent = {
  id: FormControl<IFrRemittance['id'] | NewFrRemittance['id']>;
  pin: FormControl<IFrRemittance['pin']>;
  remitersName: FormControl<IFrRemittance['remitersName']>;
  amount: FormControl<IFrRemittance['amount']>;
  incentiveAmount: FormControl<IFrRemittance['incentiveAmount']>;
  paymentDate: FormControl<IFrRemittance['paymentDate']>;
  incPaymentDate: FormControl<IFrRemittance['incPaymentDate']>;
  remiSendingDate: FormControl<IFrRemittance['remiSendingDate']>;
  remiFrCurrency: FormControl<IFrRemittance['remiFrCurrency']>;
  currency: FormControl<IFrRemittance['currency']>;
  country: FormControl<IFrRemittance['country']>;
  exchangeRate: FormControl<IFrRemittance['exchangeRate']>;
  transactionType: FormControl<IFrRemittance['transactionType']>;
  recvMobileNo: FormControl<IFrRemittance['recvMobileNo']>;
  recvName: FormControl<IFrRemittance['recvName']>;
  recvLegalId: FormControl<IFrRemittance['recvLegalId']>;
  moneyExchangeName: FormControl<IFrRemittance['moneyExchangeName']>;
  amountReimDate: FormControl<IFrRemittance['amountReimDate']>;
  incAmountReimDate: FormControl<IFrRemittance['incAmountReimDate']>;
  recvGender: FormControl<IFrRemittance['recvGender']>;
  remiGender: FormControl<IFrRemittance['remiGender']>;
  documentType: FormControl<IFrRemittance['documentType']>;
  moneyExchange: FormControl<IFrRemittance['moneyExchange']>;
  incPercentage: FormControl<IFrRemittance['incPercentage']>;
};

export type FrRemittanceFormGroup = FormGroup<FrRemittanceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FrRemittanceFormService {
  createFrRemittanceFormGroup(frRemittance: FrRemittanceFormGroupInput = { id: null }): FrRemittanceFormGroup {
    const frRemittanceRawValue = {
      ...this.getFormDefaults(),
      ...frRemittance,
    };
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
      moneyExchange: new FormControl(frRemittanceRawValue.moneyExchange),
      incPercentage: new FormControl(frRemittanceRawValue.incPercentage),
    });
  }

  getFrRemittance(form: FrRemittanceFormGroup): IFrRemittance | NewFrRemittance {
    return form.getRawValue() as IFrRemittance | NewFrRemittance;
  }

  resetForm(form: FrRemittanceFormGroup, frRemittance: FrRemittanceFormGroupInput): void {
    const frRemittanceRawValue = { ...this.getFormDefaults(), ...frRemittance };
    form.reset(
      {
        ...frRemittanceRawValue,
        id: { value: frRemittanceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FrRemittanceFormDefaults {
    return {
      id: null,
    };
  }
}
