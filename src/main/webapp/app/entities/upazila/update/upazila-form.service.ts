import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUpazila, NewUpazila } from '../upazila.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUpazila for edit and NewUpazilaFormGroupInput for create.
 */
type UpazilaFormGroupInput = IUpazila | PartialWithRequiredKeyOf<NewUpazila>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUpazila | NewUpazila> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type UpazilaFormRawValue = FormValueOf<IUpazila>;

type NewUpazilaFormRawValue = FormValueOf<NewUpazila>;

type UpazilaFormDefaults = Pick<NewUpazila, 'id' | 'createdDate' | 'lastModifiedDate'>;

type UpazilaFormGroupContent = {
  id: FormControl<UpazilaFormRawValue['id'] | NewUpazila['id']>;
  name: FormControl<UpazilaFormRawValue['name']>;
  bnName: FormControl<UpazilaFormRawValue['bnName']>;
  createdBy: FormControl<UpazilaFormRawValue['createdBy']>;
  createdDate: FormControl<UpazilaFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<UpazilaFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<UpazilaFormRawValue['lastModifiedDate']>;
  district: FormControl<UpazilaFormRawValue['district']>;
};

export type UpazilaFormGroup = FormGroup<UpazilaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UpazilaFormService {
  createUpazilaFormGroup(upazila: UpazilaFormGroupInput = { id: null }): UpazilaFormGroup {
    const upazilaRawValue = this.convertUpazilaToUpazilaRawValue({
      ...this.getFormDefaults(),
      ...upazila,
    });
    return new FormGroup<UpazilaFormGroupContent>({
      id: new FormControl(
        { value: upazilaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(upazilaRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      bnName: new FormControl(upazilaRawValue.bnName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      createdBy: new FormControl(upazilaRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(upazilaRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedBy: new FormControl(upazilaRawValue.lastModifiedBy, {
        validators: [Validators.maxLength(50)],
      }),
      lastModifiedDate: new FormControl(upazilaRawValue.lastModifiedDate),
      district: new FormControl(upazilaRawValue.district, {
        validators: [Validators.required],
      }),
    });
  }

  getUpazila(form: UpazilaFormGroup): IUpazila | NewUpazila {
    return this.convertUpazilaRawValueToUpazila(form.getRawValue() as UpazilaFormRawValue | NewUpazilaFormRawValue);
  }

  resetForm(form: UpazilaFormGroup, upazila: UpazilaFormGroupInput): void {
    const upazilaRawValue = this.convertUpazilaToUpazilaRawValue({ ...this.getFormDefaults(), ...upazila });
    form.reset(
      {
        ...upazilaRawValue,
        id: { value: upazilaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UpazilaFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertUpazilaRawValueToUpazila(rawUpazila: UpazilaFormRawValue | NewUpazilaFormRawValue): IUpazila | NewUpazila {
    return {
      ...rawUpazila,
      createdDate: dayjs(rawUpazila.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawUpazila.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertUpazilaToUpazilaRawValue(
    upazila: IUpazila | (Partial<NewUpazila> & UpazilaFormDefaults)
  ): UpazilaFormRawValue | PartialWithRequiredKeyOf<NewUpazilaFormRawValue> {
    return {
      ...upazila,
      createdDate: upazila.createdDate ? upazila.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: upazila.lastModifiedDate ? upazila.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
