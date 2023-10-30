import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMoneyExchange, NewMoneyExchange } from '../money-exchange.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMoneyExchange for edit and NewMoneyExchangeFormGroupInput for create.
 */
type MoneyExchangeFormGroupInput = IMoneyExchange | PartialWithRequiredKeyOf<NewMoneyExchange>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMoneyExchange | NewMoneyExchange> = T & {};

type MoneyExchangeFormRawValue = FormValueOf<IMoneyExchange>;

type NewMoneyExchangeFormRawValue = FormValueOf<NewMoneyExchange>;

type MoneyExchangeFormDefaults = Pick<NewMoneyExchange, 'id' | 'createdDate' | 'lastModifiedDate'>;

type MoneyExchangeFormGroupContent = {
  id: FormControl<MoneyExchangeFormRawValue['id'] | NewMoneyExchange['id']>;
  name: FormControl<MoneyExchangeFormRawValue['name']>;
  digit: FormControl<MoneyExchangeFormRawValue['digit']>;
  link: FormControl<MoneyExchangeFormRawValue['link']>;
  shortName: FormControl<MoneyExchangeFormRawValue['shortName']>;
};

export type MoneyExchangeFormGroup = FormGroup<MoneyExchangeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MoneyExchangeFormService {
  createMoneyExchangeFormGroup(moneyExchange: MoneyExchangeFormGroupInput = { id: null }): MoneyExchangeFormGroup {
    const moneyExchangeRawValue = this.convertMoneyExchangeToMoneyExchangeRawValue({
      ...this.getFormDefaults(),
      ...moneyExchange,
    });
    return new FormGroup<MoneyExchangeFormGroupContent>({
      id: new FormControl(
        { value: moneyExchangeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(moneyExchangeRawValue.name, {
        validators: [Validators.required, Validators.maxLength(256)],
      }),
      digit: new FormControl(moneyExchangeRawValue.digit, {
        validators: [Validators.required, Validators.maxLength(26)],
      }),
      link: new FormControl(moneyExchangeRawValue.link, {
        validators: [Validators.maxLength(256)],
      }),
      shortName: new FormControl(moneyExchangeRawValue.shortName, {
        validators: [Validators.maxLength(256)],
      }),
    });
  }

  getMoneyExchange(form: MoneyExchangeFormGroup): IMoneyExchange | NewMoneyExchange {
    return this.convertMoneyExchangeRawValueToMoneyExchange(form.getRawValue() as MoneyExchangeFormRawValue | NewMoneyExchangeFormRawValue);
  }

  resetForm(form: MoneyExchangeFormGroup, moneyExchange: MoneyExchangeFormGroupInput): void {
    const moneyExchangeRawValue = this.convertMoneyExchangeToMoneyExchangeRawValue({ ...this.getFormDefaults(), ...moneyExchange });
    form.reset(
      {
        ...moneyExchangeRawValue,
        id: { value: moneyExchangeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MoneyExchangeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
    };
  }

  private convertMoneyExchangeRawValueToMoneyExchange(
    rawMoneyExchange: MoneyExchangeFormRawValue | NewMoneyExchangeFormRawValue
  ): IMoneyExchange | NewMoneyExchange {
    return {
      ...rawMoneyExchange,
    };
  }

  private convertMoneyExchangeToMoneyExchangeRawValue(
    moneyExchange: IMoneyExchange | (Partial<NewMoneyExchange> & MoneyExchangeFormDefaults)
  ): MoneyExchangeFormRawValue | PartialWithRequiredKeyOf<NewMoneyExchangeFormRawValue> {
    return {
      ...moneyExchange,
    };
  }
}
