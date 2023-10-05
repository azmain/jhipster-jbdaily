import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDealer, NewDealer } from '../dealer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDealer for edit and NewDealerFormGroupInput for create.
 */
type DealerFormGroupInput = IDealer | PartialWithRequiredKeyOf<NewDealer>;

type DealerFormDefaults = Pick<NewDealer, 'id'>;

type DealerFormGroupContent = {
  id: FormControl<IDealer['id'] | NewDealer['id']>;
  name: FormControl<IDealer['name']>;
  bnName: FormControl<IDealer['bnName']>;
  shortName: FormControl<IDealer['shortName']>;
  mobile: FormControl<IDealer['mobile']>;
  upazila: FormControl<IDealer['upazila']>;
};

export type DealerFormGroup = FormGroup<DealerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DealerFormService {
  createDealerFormGroup(dealer: DealerFormGroupInput = { id: null }): DealerFormGroup {
    const dealerRawValue = {
      ...this.getFormDefaults(),
      ...dealer,
    };
    return new FormGroup<DealerFormGroupContent>({
      id: new FormControl(
        { value: dealerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(dealerRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      bnName: new FormControl(dealerRawValue.bnName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      shortName: new FormControl(dealerRawValue.shortName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      mobile: new FormControl(dealerRawValue.mobile, {
        validators: [Validators.maxLength(255)],
      }),
      upazila: new FormControl(dealerRawValue.upazila),
    });
  }

  getDealer(form: DealerFormGroup): IDealer | NewDealer {
    return form.getRawValue() as IDealer | NewDealer;
  }

  resetForm(form: DealerFormGroup, dealer: DealerFormGroupInput): void {
    const dealerRawValue = { ...this.getFormDefaults(), ...dealer };
    form.reset(
      {
        ...dealerRawValue,
        id: { value: dealerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DealerFormDefaults {
    return {
      id: null,
    };
  }
}
