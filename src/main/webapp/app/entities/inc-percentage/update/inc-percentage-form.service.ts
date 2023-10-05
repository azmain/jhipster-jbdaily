import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type IncPercentageFormDefaults = Pick<NewIncPercentage, 'id'>;

type IncPercentageFormGroupContent = {
  id: FormControl<IIncPercentage['id'] | NewIncPercentage['id']>;
  name: FormControl<IIncPercentage['name']>;
};

export type IncPercentageFormGroup = FormGroup<IncPercentageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IncPercentageFormService {
  createIncPercentageFormGroup(incPercentage: IncPercentageFormGroupInput = { id: null }): IncPercentageFormGroup {
    const incPercentageRawValue = {
      ...this.getFormDefaults(),
      ...incPercentage,
    };
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
    return form.getRawValue() as IIncPercentage | NewIncPercentage;
  }

  resetForm(form: IncPercentageFormGroup, incPercentage: IncPercentageFormGroupInput): void {
    const incPercentageRawValue = { ...this.getFormDefaults(), ...incPercentage };
    form.reset(
      {
        ...incPercentageRawValue,
        id: { value: incPercentageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IncPercentageFormDefaults {
    return {
      id: null,
    };
  }
}
