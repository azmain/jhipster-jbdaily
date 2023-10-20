import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
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
type FormValueOf<T extends IPayOrder | NewPayOrder> = Omit<T, 'payOrderDate'> & {
  payOrderDate?: Date | null;
};

type PayOrderFormRawValue = FormValueOf<IPayOrder>;

type NewPayOrderFormRawValue = FormValueOf<NewPayOrder>;

type PayOrderFormDefaults = Pick<NewPayOrder, 'id' | 'payOrderDate'>;

type PayOrderFormGroupContent = {
  id: FormControl<PayOrderFormRawValue['id'] | NewPayOrder['id']>;
  payOrderNumber: FormControl<PayOrderFormRawValue['payOrderNumber']>;
  payOrderDate: FormControl<PayOrderFormRawValue['payOrderDate']>;
  amount: FormControl<PayOrderFormRawValue['amount']>;
  slipNo: FormControl<PayOrderFormRawValue['slipNo']>;
  controllingNo: FormControl<PayOrderFormRawValue['controllingNo']>;
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
    console.log('after reset ', form);
  }

  private getFormDefaults(): PayOrderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      payOrderDate: dayjs(currentTime, DATE_TIME_FORMAT),
    };
  }

  private convertPayOrderRawValueToPayOrder(rawPayOrder: PayOrderFormRawValue | NewPayOrderFormRawValue): IPayOrder | NewPayOrder {
    console.log('convertPayOrderRawValueToPayOrder', rawPayOrder.payOrderDate);
    console.log(dayjs(rawPayOrder.payOrderDate));
    return {
      ...rawPayOrder,
      payOrderDate: dayjs(rawPayOrder.payOrderDate),
    };
  }

  private convertPayOrderToPayOrderRawValue(
    payOrder: IPayOrder | (Partial<NewPayOrder> & PayOrderFormDefaults)
  ): PayOrderFormRawValue | PartialWithRequiredKeyOf<NewPayOrderFormRawValue> {
    return {
      ...payOrder,
      fertilizer: payOrder.fertilizer ? { id: payOrder.fertilizer.id, name: payOrder.fertilizer.name } : null,
      dealer: payOrder.dealer ? { id: payOrder.dealer.id, name: payOrder.dealer.name, shortName: payOrder.dealer.shortName } : null,
      payOrderDate: payOrder.payOrderDate ? new Date(payOrder.payOrderDate.format(DATE_TIME_FORMAT)) : undefined,
    };
  }
}
