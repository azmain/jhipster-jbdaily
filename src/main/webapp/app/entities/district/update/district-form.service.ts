import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type DistrictFormDefaults = Pick<NewDistrict, 'id'>;

type DistrictFormGroupContent = {
  id: FormControl<IDistrict['id'] | NewDistrict['id']>;
  name: FormControl<IDistrict['name']>;
  bnName: FormControl<IDistrict['bnName']>;
  division: FormControl<IDistrict['division']>;
};

export type DistrictFormGroup = FormGroup<DistrictFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DistrictFormService {
  createDistrictFormGroup(district: DistrictFormGroupInput = { id: null }): DistrictFormGroup {
    const districtRawValue = {
      ...this.getFormDefaults(),
      ...district,
    };
    return new FormGroup<DistrictFormGroupContent>({
      id: new FormControl(
        { value: districtRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(districtRawValue.name, {
        validators: [Validators.required],
      }),
      bnName: new FormControl(districtRawValue.bnName, {
        validators: [Validators.required],
      }),
      division: new FormControl(districtRawValue.division),
    });
  }

  getDistrict(form: DistrictFormGroup): IDistrict | NewDistrict {
    return form.getRawValue() as IDistrict | NewDistrict;
  }

  resetForm(form: DistrictFormGroup, district: DistrictFormGroupInput): void {
    const districtRawValue = { ...this.getFormDefaults(), ...district };
    form.reset(
      {
        ...districtRawValue,
        id: { value: districtRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DistrictFormDefaults {
    return {
      id: null,
    };
  }
}
