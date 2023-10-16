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
type FormValueOf<T extends IDivision | NewDivision> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type DivisionFormRawValue = FormValueOf<IDivision>;

type NewDivisionFormRawValue = FormValueOf<NewDivision>;

type DivisionFormDefaults = Pick<NewDivision, 'id' | 'createdDate' | 'lastModifiedDate'>;

type DivisionFormGroupContent = {
  id: FormControl<DivisionFormRawValue['id'] | NewDivision['id']>;
  name: FormControl<DivisionFormRawValue['name']>;
  bnName: FormControl<DivisionFormRawValue['bnName']>;
  createdBy: FormControl<DivisionFormRawValue['createdBy']>;
  createdDate: FormControl<DivisionFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<DivisionFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<DivisionFormRawValue['lastModifiedDate']>;
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
      createdBy: new FormControl(divisionRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(divisionRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedBy: new FormControl(divisionRawValue.lastModifiedBy, {
        validators: [Validators.maxLength(50)],
      }),
      lastModifiedDate: new FormControl(divisionRawValue.lastModifiedDate),
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
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertDivisionRawValueToDivision(rawDivision: DivisionFormRawValue | NewDivisionFormRawValue): IDivision | NewDivision {
    return {
      ...rawDivision,
      createdDate: dayjs(rawDivision.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawDivision.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertDivisionToDivisionRawValue(
    division: IDivision | (Partial<NewDivision> & DivisionFormDefaults)
  ): DivisionFormRawValue | PartialWithRequiredKeyOf<NewDivisionFormRawValue> {
    return {
      ...division,
      createdDate: division.createdDate ? division.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: division.lastModifiedDate ? division.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
