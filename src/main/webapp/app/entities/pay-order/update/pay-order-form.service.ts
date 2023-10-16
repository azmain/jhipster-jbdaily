import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPayOrder, NewPayOrder } from '../pay-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPayOrder for edit and NewPayOrderFormGroupInput for create.
 */
type PayOrderFormGroupInput = IPayOrder | PartialWithRequiredKeyOf<NewPayOrder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPayOrder | NewPayOrder> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type PayOrderFormRawValue = FormValueOf<IPayOrder>;

type NewPayOrderFormRawValue = FormValueOf<NewPayOrder>;

type PayOrderFormDefaults = Pick<NewPayOrder, 'id' | 'createdDate' | 'lastModifiedDate'>;

type PayOrderFormGroupContent = {
  id: FormControl<PayOrderFormRawValue['id'] | NewPayOrder['id']>;
  payOrderNumber: FormControl<PayOrderFormRawValue['payOrderNumber']>;
  payOrderDate: FormControl<PayOrderFormRawValue['payOrderDate']>;
  amount: FormControl<PayOrderFormRawValue['amount']>;
  slipNo: FormControl<PayOrderFormRawValue['slipNo']>;
  controllingNo: FormControl<PayOrderFormRawValue['controllingNo']>;
  createdBy: FormControl<PayOrderFormRawValue['createdBy']>;
  createdDate: FormControl<PayOrderFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<PayOrderFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<PayOrderFormRawValue['lastModifiedDate']>;
  fertilizer: FormControl<PayOrderFormRawValue['fertilizer']>;
  dealer: FormControl<PayOrderFormRawValue['dealer']>;
};

export type PayOrderFormGroup = FormGroup<PayOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PayOrderFormService {
  createPayOrderFormGroup(payOrder: PayOrderFormGroupInput = { id: null }): PayOrderFormGroup {
    const payOrderRawValue = this.convertPayOrderToPayOrderRawValue({
      ...this.getFormDefaults(),
      ...payOrder,
    });
    return new FormGroup<PayOrderFormGroupContent>({
      id: new FormControl(
        { value: payOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      payOrderNumber: new FormControl(payOrderRawValue.payOrderNumber, {
        validators: [Validators.required],
      }),
      payOrderDate: new FormControl(payOrderRawValue.payOrderDate, {
        validators: [Validators.required],
      }),
      amount: new FormControl(payOrderRawValue.amount, {
        validators: [Validators.required],
      }),
      slipNo: new FormControl(payOrderRawValue.slipNo, {
        validators: [Validators.required],
      }),
      controllingNo: new FormControl(payOrderRawValue.controllingNo, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(payOrderRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(payOrderRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedBy: new FormControl(payOrderRawValue.lastModifiedBy, {
        validators: [Validators.maxLength(50)],
      }),
      lastModifiedDate: new FormControl(payOrderRawValue.lastModifiedDate),
      fertilizer: new FormControl(payOrderRawValue.fertilizer, {
        validators: [Validators.required],
      }),
      dealer: new FormControl(payOrderRawValue.dealer, {
        validators: [Validators.required],
      }),
    });
  }

  getPayOrder(form: PayOrderFormGroup): IPayOrder | NewPayOrder {
    return this.convertPayOrderRawValueToPayOrder(form.getRawValue() as PayOrderFormRawValue | NewPayOrderFormRawValue);
  }

  resetForm(form: PayOrderFormGroup, payOrder: PayOrderFormGroupInput): void {
    const payOrderRawValue = this.convertPayOrderToPayOrderRawValue({ ...this.getFormDefaults(), ...payOrder });
    form.reset(
      {
        ...payOrderRawValue,
        id: { value: payOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PayOrderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertPayOrderRawValueToPayOrder(rawPayOrder: PayOrderFormRawValue | NewPayOrderFormRawValue): IPayOrder | NewPayOrder {
    return {
      ...rawPayOrder,
      createdDate: dayjs(rawPayOrder.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawPayOrder.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertPayOrderToPayOrderRawValue(
    payOrder: IPayOrder | (Partial<NewPayOrder> & PayOrderFormDefaults)
  ): PayOrderFormRawValue | PartialWithRequiredKeyOf<NewPayOrderFormRawValue> {
    return {
      ...payOrder,
      createdDate: payOrder.createdDate ? payOrder.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: payOrder.lastModifiedDate ? payOrder.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
