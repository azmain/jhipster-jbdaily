import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIncPercentage, NewIncPercentage } from '../inc-percentage.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIncPercentage for edit and NewIncPercentageFormGroupInput for create.
 */
type IncPercentageFormGroupInput = IIncPercentage | PartialWithRequiredKeyOf<NewIncPercentage>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IIncPercentage | NewIncPercentage> = T & {};

type IncPercentageFormRawValue = FormValueOf<IIncPercentage>;

type NewIncPercentageFormRawValue = FormValueOf<NewIncPercentage>;

type IncPercentageFormDefaults = Pick<NewIncPercentage, 'id' | 'createdDate' | 'lastModifiedDate'>;

type IncPercentageFormGroupContent = {
  id: FormControl<IncPercentageFormRawValue['id'] | NewIncPercentage['id']>;
  name: FormControl<IncPercentageFormRawValue['name']>;
};

export type IncPercentageFormGroup = FormGroup<IncPercentageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IncPercentageFormService {
  createIncPercentageFormGroup(incPercentage: IncPercentageFormGroupInput = { id: null }): IncPercentageFormGroup {
    const incPercentageRawValue = this.convertIncPercentageToIncPercentageRawValue({
      ...this.getFormDefaults(),
      ...incPercentage,
    });
    return new FormGroup<IncPercentageFormGroupContent>({
      id: new FormControl(
        { value: incPercentageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(incPercentageRawValue.name, {
        validators: [Validators.required, Validators.maxLength(256)],
      }),
    });
  }

  getIncPercentage(form: IncPercentageFormGroup): IIncPercentage | NewIncPercentage {
    return this.convertIncPercentageRawValueToIncPercentage(form.getRawValue() as IncPercentageFormRawValue | NewIncPercentageFormRawValue);
  }

  resetForm(form: IncPercentageFormGroup, incPercentage: IncPercentageFormGroupInput): void {
    const incPercentageRawValue = this.convertIncPercentageToIncPercentageRawValue({ ...this.getFormDefaults(), ...incPercentage });
    form.reset(
      {
        ...incPercentageRawValue,
        id: { value: incPercentageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IncPercentageFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
    };
  }

  private convertIncPercentageRawValueToIncPercentage(
    rawIncPercentage: IncPercentageFormRawValue | NewIncPercentageFormRawValue
  ): IIncPercentage | NewIncPercentage {
    return {
      ...rawIncPercentage,
    };
  }

  private convertIncPercentageToIncPercentageRawValue(
    incPercentage: IIncPercentage | (Partial<NewIncPercentage> & IncPercentageFormDefaults)
  ): IncPercentageFormRawValue | PartialWithRequiredKeyOf<NewIncPercentageFormRawValue> {
    return {
      ...incPercentage,
    };
  }
}
