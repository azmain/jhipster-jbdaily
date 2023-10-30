import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDivision, NewDivision } from '../division.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDivision for edit and NewDivisionFormGroupInput for create.
 */
type DivisionFormGroupInput = IDivision | PartialWithRequiredKeyOf<NewDivision>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDivision | NewDivision> = T & {};

type DivisionFormRawValue = FormValueOf<IDivision>;

type NewDivisionFormRawValue = FormValueOf<NewDivision>;

type DivisionFormDefaults = Pick<NewDivision, 'id' | 'createdDate' | 'lastModifiedDate'>;

type DivisionFormGroupContent = {
  id: FormControl<DivisionFormRawValue['id'] | NewDivision['id']>;
  name: FormControl<DivisionFormRawValue['name']>;
  bnName: FormControl<DivisionFormRawValue['bnName']>;
};

export type DivisionFormGroup = FormGroup<DivisionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DivisionFormService {
  createDivisionFormGroup(division: DivisionFormGroupInput = { id: null }): DivisionFormGroup {
    const divisionRawValue = this.convertDivisionToDivisionRawValue({
      ...this.getFormDefaults(),
      ...division,
    });
    return new FormGroup<DivisionFormGroupContent>({
      id: new FormControl(
        { value: divisionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(divisionRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      bnName: new FormControl(divisionRawValue.bnName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
    });
  }

  getDivision(form: DivisionFormGroup): IDivision | NewDivision {
    return this.convertDivisionRawValueToDivision(form.getRawValue() as DivisionFormRawValue | NewDivisionFormRawValue);
  }

  resetForm(form: DivisionFormGroup, division: DivisionFormGroupInput): void {
    const divisionRawValue = this.convertDivisionToDivisionRawValue({ ...this.getFormDefaults(), ...division });
    form.reset(
      {
        ...divisionRawValue,
        id: { value: divisionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DivisionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
    };
  }

  private convertDivisionRawValueToDivision(rawDivision: DivisionFormRawValue | NewDivisionFormRawValue): IDivision | NewDivision {
    return {
      ...rawDivision,
    };
  }

  private convertDivisionToDivisionRawValue(
    division: IDivision | (Partial<NewDivision> & DivisionFormDefaults)
  ): DivisionFormRawValue | PartialWithRequiredKeyOf<NewDivisionFormRawValue> {
    return {
      ...division,
    };
  }
}
