import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFertilizer, NewFertilizer } from '../fertilizer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFertilizer for edit and NewFertilizerFormGroupInput for create.
 */
type FertilizerFormGroupInput = IFertilizer | PartialWithRequiredKeyOf<NewFertilizer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFertilizer | NewFertilizer> = T & {};

type FertilizerFormRawValue = FormValueOf<IFertilizer>;

type NewFertilizerFormRawValue = FormValueOf<NewFertilizer>;

type FertilizerFormDefaults = Pick<NewFertilizer, 'id'>;

type FertilizerFormGroupContent = {
  id: FormControl<FertilizerFormRawValue['id'] | NewFertilizer['id']>;
  name: FormControl<FertilizerFormRawValue['name']>;
  bnName: FormControl<FertilizerFormRawValue['bnName']>;
  accountNo: FormControl<FertilizerFormRawValue['accountNo']>;
  accountTitle: FormControl<FertilizerFormRawValue['accountTitle']>;
};

export type FertilizerFormGroup = FormGroup<FertilizerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FertilizerFormService {
  createFertilizerFormGroup(fertilizer: FertilizerFormGroupInput = { id: null }): FertilizerFormGroup {
    const fertilizerRawValue = this.convertFertilizerToFertilizerRawValue({
      ...this.getFormDefaults(),
      ...fertilizer,
    });
    return new FormGroup<FertilizerFormGroupContent>({
      id: new FormControl(
        { value: fertilizerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(fertilizerRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      bnName: new FormControl(fertilizerRawValue.bnName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      accountNo: new FormControl(fertilizerRawValue.accountNo, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      accountTitle: new FormControl(fertilizerRawValue.accountTitle, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
    });
  }

  getFertilizer(form: FertilizerFormGroup): IFertilizer | NewFertilizer {
    return this.convertFertilizerRawValueToFertilizer(form.getRawValue() as FertilizerFormRawValue | NewFertilizerFormRawValue);
  }

  resetForm(form: FertilizerFormGroup, fertilizer: FertilizerFormGroupInput): void {
    const fertilizerRawValue = this.convertFertilizerToFertilizerRawValue({ ...this.getFormDefaults(), ...fertilizer });
    form.reset(
      {
        ...fertilizerRawValue,
        id: { value: fertilizerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FertilizerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
    };
  }

  private convertFertilizerRawValueToFertilizer(
    rawFertilizer: FertilizerFormRawValue | NewFertilizerFormRawValue
  ): IFertilizer | NewFertilizer {
    return {
      ...rawFertilizer,
    };
  }

  private convertFertilizerToFertilizerRawValue(
    fertilizer: IFertilizer | (Partial<NewFertilizer> & FertilizerFormDefaults)
  ): FertilizerFormRawValue | PartialWithRequiredKeyOf<NewFertilizerFormRawValue> {
    return {
      ...fertilizer,
    };
  }
}
