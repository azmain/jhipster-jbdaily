import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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

type MoneyExchangeFormDefaults = Pick<NewMoneyExchange, 'id'>;

type MoneyExchangeFormGroupContent = {
  id: FormControl<IMoneyExchange['id'] | NewMoneyExchange['id']>;
  name: FormControl<IMoneyExchange['name']>;
  digit: FormControl<IMoneyExchange['digit']>;
  link: FormControl<IMoneyExchange['link']>;
  shortName: FormControl<IMoneyExchange['shortName']>;
};

export type MoneyExchangeFormGroup = FormGroup<MoneyExchangeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MoneyExchangeFormService {
  createMoneyExchangeFormGroup(moneyExchange: MoneyExchangeFormGroupInput = { id: null }): MoneyExchangeFormGroup {
    const moneyExchangeRawValue = {
      ...this.getFormDefaults(),
      ...moneyExchange,
    };
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
    return form.getRawValue() as IMoneyExchange | NewMoneyExchange;
  }

  resetForm(form: MoneyExchangeFormGroup, moneyExchange: MoneyExchangeFormGroupInput): void {
    const moneyExchangeRawValue = { ...this.getFormDefaults(), ...moneyExchange };
    form.reset(
      {
        ...moneyExchangeRawValue,
        id: { value: moneyExchangeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): MoneyExchangeFormDefaults {
    return {
      id: null,
    };
  }
}
