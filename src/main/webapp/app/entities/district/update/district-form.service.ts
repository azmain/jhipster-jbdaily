import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDistrict, NewDistrict } from '../district.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDistrict for edit and NewDistrictFormGroupInput for create.
 */
type DistrictFormGroupInput = IDistrict | PartialWithRequiredKeyOf<NewDistrict>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDistrict | NewDistrict> = T & {};

type DistrictFormRawValue = FormValueOf<IDistrict>;

type NewDistrictFormRawValue = FormValueOf<NewDistrict>;

type DistrictFormDefaults = Pick<NewDistrict, 'id' | 'createdDate' | 'lastModifiedDate'>;

type DistrictFormGroupContent = {
  id: FormControl<DistrictFormRawValue['id'] | NewDistrict['id']>;
  name: FormControl<DistrictFormRawValue['name']>;
  bnName: FormControl<DistrictFormRawValue['bnName']>;
  division: FormControl<DistrictFormRawValue['division']>;
};

export type DistrictFormGroup = FormGroup<DistrictFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DistrictFormService {
  createDistrictFormGroup(district: DistrictFormGroupInput = { id: null }): DistrictFormGroup {
    const districtRawValue = this.convertDistrictToDistrictRawValue({
      ...this.getFormDefaults(),
      ...district,
    });
    return new FormGroup<DistrictFormGroupContent>({
      id: new FormControl(
        { value: districtRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(districtRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      bnName: new FormControl(districtRawValue.bnName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      division: new FormControl(districtRawValue.division, {
        validators: [Validators.required],
      }),
    });
  }

  getDistrict(form: DistrictFormGroup): IDistrict | NewDistrict {
    return this.convertDistrictRawValueToDistrict(form.getRawValue() as DistrictFormRawValue | NewDistrictFormRawValue);
  }

  resetForm(form: DistrictFormGroup, district: DistrictFormGroupInput): void {
    const districtRawValue = this.convertDistrictToDistrictRawValue({ ...this.getFormDefaults(), ...district });
    form.reset(
      {
        ...districtRawValue,
        id: { value: districtRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DistrictFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
    };
  }

  private convertDistrictRawValueToDistrict(rawDistrict: DistrictFormRawValue | NewDistrictFormRawValue): IDistrict | NewDistrict {
    return {
      ...rawDistrict,
    };
  }

  private convertDistrictToDistrictRawValue(
    district: IDistrict | (Partial<NewDistrict> & DistrictFormDefaults)
  ): DistrictFormRawValue | PartialWithRequiredKeyOf<NewDistrictFormRawValue> {
    return {
      ...district,
      division: district.division ? { id: district.division.id, name: district.division.name, bnName: district.division.bnName } : null,
    };
  }
}
