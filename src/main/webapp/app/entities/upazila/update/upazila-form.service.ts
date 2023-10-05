import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type UpazilaFormDefaults = Pick<NewUpazila, 'id'>;

type UpazilaFormGroupContent = {
  id: FormControl<IUpazila['id'] | NewUpazila['id']>;
  name: FormControl<IUpazila['name']>;
  bnName: FormControl<IUpazila['bnName']>;
  district: FormControl<IUpazila['district']>;
};

export type UpazilaFormGroup = FormGroup<UpazilaFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UpazilaFormService {
  createUpazilaFormGroup(upazila: UpazilaFormGroupInput = { id: null }): UpazilaFormGroup {
    const upazilaRawValue = {
      ...this.getFormDefaults(),
      ...upazila,
    };
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
      district: new FormControl(upazilaRawValue.district),
    });
  }

  getUpazila(form: UpazilaFormGroup): IUpazila | NewUpazila {
    return form.getRawValue() as IUpazila | NewUpazila;
  }

  resetForm(form: UpazilaFormGroup, upazila: UpazilaFormGroupInput): void {
    const upazilaRawValue = { ...this.getFormDefaults(), ...upazila };
    form.reset(
      {
        ...upazilaRawValue,
        id: { value: upazilaRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UpazilaFormDefaults {
    return {
      id: null,
    };
  }
}
