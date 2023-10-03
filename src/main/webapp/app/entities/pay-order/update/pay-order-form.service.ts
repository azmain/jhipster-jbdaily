import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type PayOrderFormDefaults = Pick<NewPayOrder, 'id'>;

type PayOrderFormGroupContent = {
  id: FormControl<IPayOrder['id'] | NewPayOrder['id']>;
  payOrderNumber: FormControl<IPayOrder['payOrderNumber']>;
  payOrderDate: FormControl<IPayOrder['payOrderDate']>;
  amount: FormControl<IPayOrder['amount']>;
  slipNo: FormControl<IPayOrder['slipNo']>;
  controllingNo: FormControl<IPayOrder['controllingNo']>;
  fertilizer: FormControl<IPayOrder['fertilizer']>;
  dealer: FormControl<IPayOrder['dealer']>;
};

export type PayOrderFormGroup = FormGroup<PayOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PayOrderFormService {
  createPayOrderFormGroup(payOrder: PayOrderFormGroupInput = { id: null }): PayOrderFormGroup {
    const payOrderRawValue = {
      ...this.getFormDefaults(),
      ...payOrder,
    };
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
      fertilizer: new FormControl(payOrderRawValue.fertilizer),
      dealer: new FormControl(payOrderRawValue.dealer),
    });
  }

  getPayOrder(form: PayOrderFormGroup): IPayOrder | NewPayOrder {
    return form.getRawValue() as IPayOrder | NewPayOrder;
  }

  resetForm(form: PayOrderFormGroup, payOrder: PayOrderFormGroupInput): void {
    const payOrderRawValue = { ...this.getFormDefaults(), ...payOrder };
    form.reset(
      {
        ...payOrderRawValue,
        id: { value: payOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PayOrderFormDefaults {
    return {
      id: null,
    };
  }
}
