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
type FormValueOf<T extends IMoneyExchange | NewMoneyExchange> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type MoneyExchangeFormRawValue = FormValueOf<IMoneyExchange>;

type NewMoneyExchangeFormRawValue = FormValueOf<NewMoneyExchange>;

type MoneyExchangeFormDefaults = Pick<NewMoneyExchange, 'id' | 'createdDate' | 'lastModifiedDate'>;

type MoneyExchangeFormGroupContent = {
  id: FormControl<MoneyExchangeFormRawValue['id'] | NewMoneyExchange['id']>;
  name: FormControl<MoneyExchangeFormRawValue['name']>;
  digit: FormControl<MoneyExchangeFormRawValue['digit']>;
  link: FormControl<MoneyExchangeFormRawValue['link']>;
  shortName: FormControl<MoneyExchangeFormRawValue['shortName']>;
  createdBy: FormControl<MoneyExchangeFormRawValue['createdBy']>;
  createdDate: FormControl<MoneyExchangeFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<MoneyExchangeFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<MoneyExchangeFormRawValue['lastModifiedDate']>;
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
      createdBy: new FormControl(moneyExchangeRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(moneyExchangeRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedBy: new FormControl(moneyExchangeRawValue.lastModifiedBy, {
        validators: [Validators.maxLength(50)],
      }),
      lastModifiedDate: new FormControl(moneyExchangeRawValue.lastModifiedDate),
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
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertMoneyExchangeRawValueToMoneyExchange(
    rawMoneyExchange: MoneyExchangeFormRawValue | NewMoneyExchangeFormRawValue
  ): IMoneyExchange | NewMoneyExchange {
    return {
      ...rawMoneyExchange,
      createdDate: dayjs(rawMoneyExchange.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawMoneyExchange.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertMoneyExchangeToMoneyExchangeRawValue(
    moneyExchange: IMoneyExchange | (Partial<NewMoneyExchange> & MoneyExchangeFormDefaults)
  ): MoneyExchangeFormRawValue | PartialWithRequiredKeyOf<NewMoneyExchangeFormRawValue> {
    return {
      ...moneyExchange,
      createdDate: moneyExchange.createdDate ? moneyExchange.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: moneyExchange.lastModifiedDate ? moneyExchange.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
