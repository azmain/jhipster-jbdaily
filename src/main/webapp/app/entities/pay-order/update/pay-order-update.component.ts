import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PayOrderFormService, PayOrderFormGroup } from './pay-order-form.service';
import { IPayOrder } from '../pay-order.model';
import { PayOrderService } from '../service/pay-order.service';
import { IFertilizer } from 'app/entities/fertilizer/fertilizer.model';
import { FertilizerService } from 'app/entities/fertilizer/service/fertilizer.service';
import { IDealer } from 'app/entities/dealer/dealer.model';
import { DealerService, RestDealer } from 'app/entities/dealer/service/dealer.service';
import { RestUserSettings, UserSettingsService } from 'app/entities/user-settings/service/user-settings.service';
import { AccountService } from 'app/core/auth/account.service';
import { IUserSettings } from 'app/entities/user-settings/user-settings.model';
import dayjs, { Dayjs } from 'dayjs';

@Component({
  selector: 'jhi-pay-order-update',
  templateUrl: './pay-order-update.component.html',
})
export class PayOrderUpdateComponent implements OnInit {
  isSaving = false;
  payOrder: IPayOrder | null = null;

  fertilizersSharedCollection: IFertilizer[] = [];
  dealersSharedCollection: IDealer[] = [];

  editForm: PayOrderFormGroup = this.payOrderFormService.createPayOrderFormGroup();

  loginUser: any;

  constructor(
    protected payOrderService: PayOrderService,
    protected payOrderFormService: PayOrderFormService,
    protected fertilizerService: FertilizerService,
    protected dealerService: DealerService,
    protected userSettingsService: UserSettingsService,
    protected accountService: AccountService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFertilizer = (o1: IFertilizer | null, o2: IFertilizer | null): boolean => this.fertilizerService.compareFertilizer(o1, o2);

  compareDealer = (o1: IDealer | null, o2: IDealer | null): boolean => this.dealerService.compareDealer(o1, o2);

  ngOnInit(): void {
    console.log('on init', this.editForm);

    this.accountService.getAuthenticationState().subscribe(account => {
      this.loginUser = account;
    });

    this.activatedRoute.data.subscribe(({ payOrder }) => {
      console.log('payOrder', payOrder);
      this.payOrder = payOrder;
      if (payOrder) {
        this.updateForm(payOrder);
      } else {
        this.loadUserSettings();
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    console.log('form ', this.editForm);
    const payOrder = this.payOrderFormService.getPayOrder(this.editForm);

    console.log('payorder ', payOrder);
    if (payOrder.id !== null) {
      this.subscribeToSaveResponse(this.payOrderService.partialUpdate(payOrder));
    } else {
      this.subscribeToSaveResponse(this.payOrderService.create(payOrder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPayOrder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(payOrder: IPayOrder): void {
    this.payOrder = payOrder;
    this.payOrderFormService.resetForm(this.editForm, payOrder);
    console.log('after update form', this.editForm);

    this.fertilizersSharedCollection = this.fertilizerService.addFertilizerToCollectionIfMissing<IFertilizer>(
      this.fertilizersSharedCollection,
      payOrder.fertilizer
    );
    this.dealersSharedCollection = this.dealerService.addDealerToCollectionIfMissing<IDealer>(
      this.dealersSharedCollection,
      payOrder.dealer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.fertilizerService
      .query()
      .pipe(
        map((res: HttpResponse<IDealer[]>) => {
          return res.body ? res.body.map(item => this.convertFertilizerOption(item)) : [];
        })
      )
      .pipe(
        map((fertilizers: IFertilizer[]) =>
          this.fertilizerService.addFertilizerToCollectionIfMissing<IFertilizer>(fertilizers, this.payOrder?.fertilizer)
        )
      )
      .subscribe((fertilizers: IFertilizer[]) => {
        this.fertilizersSharedCollection = fertilizers;
      });

    this.dealerService
      .query()
      .pipe(
        map((res: HttpResponse<IDealer[]>) => {
          return res.body ? res.body.map(item => this.convertDealerOption(item)) : [];
        })
      )
      .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing<IDealer>(dealers, this.payOrder?.dealer)))
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));
  }

  protected loadUserSettings(): void {
    const queryObject: any = {
      'createdBy.equals': this.loginUser.login,
    };
    this.userSettingsService
      .query(queryObject)
      .pipe(map((res: HttpResponse<IUserSettings[]>) => res.body ?? []))
      .subscribe(userSettings => {
        if (userSettings.length > 0) {
          this.editForm.patchValue({
            payOrderNumber: Number(userSettings[0].payOrderNumSeq),
            controllingNo: Number(userSettings[0].payOrderControlNum),
          });
          this.editForm.updateValueAndValidity();
        }

        console.log('settings', userSettings);
      });
  }

  convertDealerOption(dealer: IDealer): IDealer {
    return {
      id: dealer.id,
      name: dealer.name,
      shortName: dealer.shortName,
    };
  }
  convertFertilizerOption(fertilizer: IFertilizer): IDealer {
    return {
      id: fertilizer.id,
      name: fertilizer.name,
    };
  }
}
