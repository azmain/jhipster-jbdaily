import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type FertilizerFormDefaults = Pick<NewFertilizer, 'id'>;

type FertilizerFormGroupContent = {
  id: FormControl<IFertilizer['id'] | NewFertilizer['id']>;
  name: FormControl<IFertilizer['name']>;
  bnName: FormControl<IFertilizer['bnName']>;
  accountNo: FormControl<IFertilizer['accountNo']>;
  accountTitle: FormControl<IFertilizer['accountTitle']>;
};

export type FertilizerFormGroup = FormGroup<FertilizerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FertilizerFormService {
  createFertilizerFormGroup(fertilizer: FertilizerFormGroupInput = { id: null }): FertilizerFormGroup {
    const fertilizerRawValue = {
      ...this.getFormDefaults(),
      ...fertilizer,
    };
    return new FormGroup<FertilizerFormGroupContent>({
      id: new FormControl(
        { value: fertilizerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(fertilizerRawValue.name, {
        validators: [Validators.required],
      }),
      bnName: new FormControl(fertilizerRawValue.bnName, {
        validators: [Validators.required],
      }),
      accountNo: new FormControl(fertilizerRawValue.accountNo, {
        validators: [Validators.required],
      }),
      accountTitle: new FormControl(fertilizerRawValue.accountTitle, {
        validators: [Validators.required],
      }),
    });
  }

  getFertilizer(form: FertilizerFormGroup): IFertilizer | NewFertilizer {
    return form.getRawValue() as IFertilizer | NewFertilizer;
  }

  resetForm(form: FertilizerFormGroup, fertilizer: FertilizerFormGroupInput): void {
    const fertilizerRawValue = { ...this.getFormDefaults(), ...fertilizer };
    form.reset(
      {
        ...fertilizerRawValue,
        id: { value: fertilizerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FertilizerFormDefaults {
    return {
      id: null,
    };
  }
}
