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
import { DealerService } from 'app/entities/dealer/service/dealer.service';

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

  constructor(
    protected payOrderService: PayOrderService,
    protected payOrderFormService: PayOrderFormService,
    protected fertilizerService: FertilizerService,
    protected dealerService: DealerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFertilizer = (o1: IFertilizer | null, o2: IFertilizer | null): boolean => this.fertilizerService.compareFertilizer(o1, o2);

  compareDealer = (o1: IDealer | null, o2: IDealer | null): boolean => this.dealerService.compareDealer(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payOrder }) => {
      this.payOrder = payOrder;
      if (payOrder) {
        this.updateForm(payOrder);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const payOrder = this.payOrderFormService.getPayOrder(this.editForm);
    if (payOrder.id !== null) {
      this.subscribeToSaveResponse(this.payOrderService.update(payOrder));
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
      .pipe(map((res: HttpResponse<IFertilizer[]>) => res.body ?? []))
      .pipe(
        map((fertilizers: IFertilizer[]) =>
          this.fertilizerService.addFertilizerToCollectionIfMissing<IFertilizer>(fertilizers, this.payOrder?.fertilizer)
        )
      )
      .subscribe((fertilizers: IFertilizer[]) => (this.fertilizersSharedCollection = fertilizers));

    this.dealerService
      .query()
      .pipe(map((res: HttpResponse<IDealer[]>) => res.body ?? []))
      .pipe(map((dealers: IDealer[]) => this.dealerService.addDealerToCollectionIfMissing<IDealer>(dealers, this.payOrder?.dealer)))
      .subscribe((dealers: IDealer[]) => (this.dealersSharedCollection = dealers));
  }
}
