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
type FormValueOf<T extends IIncPercentage | NewIncPercentage> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type IncPercentageFormRawValue = FormValueOf<IIncPercentage>;

type NewIncPercentageFormRawValue = FormValueOf<NewIncPercentage>;

type IncPercentageFormDefaults = Pick<NewIncPercentage, 'id' | 'createdDate' | 'lastModifiedDate'>;

type IncPercentageFormGroupContent = {
  id: FormControl<IncPercentageFormRawValue['id'] | NewIncPercentage['id']>;
  name: FormControl<IncPercentageFormRawValue['name']>;
  createdBy: FormControl<IncPercentageFormRawValue['createdBy']>;
  createdDate: FormControl<IncPercentageFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<IncPercentageFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<IncPercentageFormRawValue['lastModifiedDate']>;
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
      createdBy: new FormControl(incPercentageRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(incPercentageRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedBy: new FormControl(incPercentageRawValue.lastModifiedBy, {
        validators: [Validators.maxLength(50)],
      }),
      lastModifiedDate: new FormControl(incPercentageRawValue.lastModifiedDate),
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
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertIncPercentageRawValueToIncPercentage(
    rawIncPercentage: IncPercentageFormRawValue | NewIncPercentageFormRawValue
  ): IIncPercentage | NewIncPercentage {
    return {
      ...rawIncPercentage,
      createdDate: dayjs(rawIncPercentage.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawIncPercentage.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertIncPercentageToIncPercentageRawValue(
    incPercentage: IIncPercentage | (Partial<NewIncPercentage> & IncPercentageFormDefaults)
  ): IncPercentageFormRawValue | PartialWithRequiredKeyOf<NewIncPercentageFormRawValue> {
    return {
      ...incPercentage,
      createdDate: incPercentage.createdDate ? incPercentage.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: incPercentage.lastModifiedDate ? incPercentage.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
