import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserSettings, NewUserSettings } from '../user-settings.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserSettings for edit and NewUserSettingsFormGroupInput for create.
 */
type UserSettingsFormGroupInput = IUserSettings | PartialWithRequiredKeyOf<NewUserSettings>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserSettings | NewUserSettings> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type UserSettingsFormRawValue = FormValueOf<IUserSettings>;

type NewUserSettingsFormRawValue = FormValueOf<NewUserSettings>;

type UserSettingsFormDefaults = Pick<NewUserSettings, 'id' | 'createdDate' | 'lastModifiedDate'>;

type UserSettingsFormGroupContent = {
  id: FormControl<UserSettingsFormRawValue['id'] | NewUserSettings['id']>;
  name: FormControl<UserSettingsFormRawValue['name']>;
  payOrderNumSeq: FormControl<UserSettingsFormRawValue['payOrderNumSeq']>;
  payOrderControlNum: FormControl<UserSettingsFormRawValue['payOrderControlNum']>;
  createdBy: FormControl<UserSettingsFormRawValue['createdBy']>;
  createdDate: FormControl<UserSettingsFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<UserSettingsFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<UserSettingsFormRawValue['lastModifiedDate']>;
};

export type UserSettingsFormGroup = FormGroup<UserSettingsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserSettingsFormService {
  createUserSettingsFormGroup(userSettings: UserSettingsFormGroupInput = { id: null }): UserSettingsFormGroup {
    const userSettingsRawValue = this.convertUserSettingsToUserSettingsRawValue({
      ...this.getFormDefaults(),
      ...userSettings,
    });
    return new FormGroup<UserSettingsFormGroupContent>({
      id: new FormControl(
        { value: userSettingsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(userSettingsRawValue.name, {
        validators: [Validators.required, Validators.maxLength(256)],
      }),
      payOrderNumSeq: new FormControl(userSettingsRawValue.payOrderNumSeq, {
        validators: [Validators.required],
      }),
      payOrderControlNum: new FormControl(userSettingsRawValue.payOrderControlNum, {
        validators: [Validators.required],
      }),
      createdBy: new FormControl(userSettingsRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(userSettingsRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedBy: new FormControl(userSettingsRawValue.lastModifiedBy, {
        validators: [Validators.maxLength(50)],
      }),
      lastModifiedDate: new FormControl(userSettingsRawValue.lastModifiedDate),
    });
  }

  getUserSettings(form: UserSettingsFormGroup): IUserSettings | NewUserSettings {
    return this.convertUserSettingsRawValueToUserSettings(form.getRawValue() as UserSettingsFormRawValue | NewUserSettingsFormRawValue);
  }

  resetForm(form: UserSettingsFormGroup, userSettings: UserSettingsFormGroupInput): void {
    const userSettingsRawValue = this.convertUserSettingsToUserSettingsRawValue({ ...this.getFormDefaults(), ...userSettings });
    form.reset(
      {
        ...userSettingsRawValue,
        id: { value: userSettingsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UserSettingsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertUserSettingsRawValueToUserSettings(
    rawUserSettings: UserSettingsFormRawValue | NewUserSettingsFormRawValue
  ): IUserSettings | NewUserSettings {
    return {
      ...rawUserSettings,
      createdDate: dayjs(rawUserSettings.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawUserSettings.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertUserSettingsToUserSettingsRawValue(
    userSettings: IUserSettings | (Partial<NewUserSettings> & UserSettingsFormDefaults)
  ): UserSettingsFormRawValue | PartialWithRequiredKeyOf<NewUserSettingsFormRawValue> {
    return {
      ...userSettings,
      createdDate: userSettings.createdDate ? userSettings.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: userSettings.lastModifiedDate ? userSettings.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
