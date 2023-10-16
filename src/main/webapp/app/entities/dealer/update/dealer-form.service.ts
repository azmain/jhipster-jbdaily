import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDealer, NewDealer } from '../dealer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDealer for edit and NewDealerFormGroupInput for create.
 */
type DealerFormGroupInput = IDealer | PartialWithRequiredKeyOf<NewDealer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDealer | NewDealer> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type DealerFormRawValue = FormValueOf<IDealer>;

type NewDealerFormRawValue = FormValueOf<NewDealer>;

type DealerFormDefaults = Pick<NewDealer, 'id' | 'createdDate' | 'lastModifiedDate'>;

type DealerFormGroupContent = {
  id: FormControl<DealerFormRawValue['id'] | NewDealer['id']>;
  name: FormControl<DealerFormRawValue['name']>;
  bnName: FormControl<DealerFormRawValue['bnName']>;
  shortName: FormControl<DealerFormRawValue['shortName']>;
  mobile: FormControl<DealerFormRawValue['mobile']>;
  createdBy: FormControl<DealerFormRawValue['createdBy']>;
  createdDate: FormControl<DealerFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<DealerFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<DealerFormRawValue['lastModifiedDate']>;
  upazila: FormControl<DealerFormRawValue['upazila']>;
};

export type DealerFormGroup = FormGroup<DealerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DealerFormService {
  createDealerFormGroup(dealer: DealerFormGroupInput = { id: null }): DealerFormGroup {
    const dealerRawValue = this.convertDealerToDealerRawValue({
      ...this.getFormDefaults(),
      ...dealer,
    });
    return new FormGroup<DealerFormGroupContent>({
      id: new FormControl(
        { value: dealerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(dealerRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      bnName: new FormControl(dealerRawValue.bnName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      shortName: new FormControl(dealerRawValue.shortName, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      mobile: new FormControl(dealerRawValue.mobile, {
        validators: [Validators.maxLength(255)],
      }),
      createdBy: new FormControl(dealerRawValue.createdBy, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      createdDate: new FormControl(dealerRawValue.createdDate, {
        validators: [Validators.required],
      }),
      lastModifiedBy: new FormControl(dealerRawValue.lastModifiedBy, {
        validators: [Validators.maxLength(50)],
      }),
      lastModifiedDate: new FormControl(dealerRawValue.lastModifiedDate),
      upazila: new FormControl(dealerRawValue.upazila, {
        validators: [Validators.required],
      }),
    });
  }

  getDealer(form: DealerFormGroup): IDealer | NewDealer {
    return this.convertDealerRawValueToDealer(form.getRawValue() as DealerFormRawValue | NewDealerFormRawValue);
  }

  resetForm(form: DealerFormGroup, dealer: DealerFormGroupInput): void {
    const dealerRawValue = this.convertDealerToDealerRawValue({ ...this.getFormDefaults(), ...dealer });
    form.reset(
      {
        ...dealerRawValue,
        id: { value: dealerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DealerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertDealerRawValueToDealer(rawDealer: DealerFormRawValue | NewDealerFormRawValue): IDealer | NewDealer {
    return {
      ...rawDealer,
      createdDate: dayjs(rawDealer.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawDealer.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertDealerToDealerRawValue(
    dealer: IDealer | (Partial<NewDealer> & DealerFormDefaults)
  ): DealerFormRawValue | PartialWithRequiredKeyOf<NewDealerFormRawValue> {
    return {
      ...dealer,
      createdDate: dealer.createdDate ? dealer.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: dealer.lastModifiedDate ? dealer.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
