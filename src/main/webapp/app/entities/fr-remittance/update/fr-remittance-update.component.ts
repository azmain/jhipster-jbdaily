import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FrRemittanceFormService, FrRemittanceFormGroup } from './fr-remittance-form.service';
import { IFrRemittance } from '../fr-remittance.model';
import { FrRemittanceService } from '../service/fr-remittance.service';
import { IMoneyExchange } from 'app/entities/money-exchange/money-exchange.model';
import { MoneyExchangeService } from 'app/entities/money-exchange/service/money-exchange.service';
import { IIncPercentage } from 'app/entities/inc-percentage/inc-percentage.model';
import { IncPercentageService } from 'app/entities/inc-percentage/service/inc-percentage.service';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { DocumentType } from 'app/entities/enumerations/document-type.model';

@Component({
  selector: 'jhi-fr-remittance-update',
  templateUrl: './fr-remittance-update.component.html',
})
export class FrRemittanceUpdateComponent implements OnInit {
  isSaving = false;
  frRemittance: IFrRemittance | null = null;
  transactionTypeValues = Object.keys(TransactionType);
  genderValues = Object.keys(Gender);
  documentTypeValues = Object.keys(DocumentType);

  moneyExchangesSharedCollection: IMoneyExchange[] = [];
  incPercentagesSharedCollection: IIncPercentage[] = [];

  editForm: FrRemittanceFormGroup = this.frRemittanceFormService.createFrRemittanceFormGroup();

  constructor(
    protected frRemittanceService: FrRemittanceService,
    protected frRemittanceFormService: FrRemittanceFormService,
    protected moneyExchangeService: MoneyExchangeService,
    protected incPercentageService: IncPercentageService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMoneyExchange = (o1: IMoneyExchange | null, o2: IMoneyExchange | null): boolean =>
    this.moneyExchangeService.compareMoneyExchange(o1, o2);

  compareIncPercentage = (o1: IIncPercentage | null, o2: IIncPercentage | null): boolean =>
    this.incPercentageService.compareIncPercentage(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frRemittance }) => {
      this.frRemittance = frRemittance;
      if (frRemittance) {
        this.updateForm(frRemittance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const frRemittance = this.frRemittanceFormService.getFrRemittance(this.editForm);
    if (frRemittance.id !== null) {
      this.subscribeToSaveResponse(this.frRemittanceService.update(frRemittance));
    } else {
      this.subscribeToSaveResponse(this.frRemittanceService.create(frRemittance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFrRemittance>>): void {
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

  protected updateForm(frRemittance: IFrRemittance): void {
    this.frRemittance = frRemittance;
    this.frRemittanceFormService.resetForm(this.editForm, frRemittance);

    this.moneyExchangesSharedCollection = this.moneyExchangeService.addMoneyExchangeToCollectionIfMissing<IMoneyExchange>(
      this.moneyExchangesSharedCollection,
      frRemittance.moneyExchange
    );
    this.incPercentagesSharedCollection = this.incPercentageService.addIncPercentageToCollectionIfMissing<IIncPercentage>(
      this.incPercentagesSharedCollection,
      frRemittance.incPercentage
    );
  }

  protected loadRelationshipsOptions(): void {
    this.moneyExchangeService
      .query()
      .pipe(map((res: HttpResponse<IMoneyExchange[]>) => res.body ?? []))
      .pipe(
        map((moneyExchanges: IMoneyExchange[]) =>
          this.moneyExchangeService.addMoneyExchangeToCollectionIfMissing<IMoneyExchange>(moneyExchanges, this.frRemittance?.moneyExchange)
        )
      )
      .subscribe((moneyExchanges: IMoneyExchange[]) => (this.moneyExchangesSharedCollection = moneyExchanges));

    this.incPercentageService
      .query()
      .pipe(map((res: HttpResponse<IIncPercentage[]>) => res.body ?? []))
      .pipe(
        map((incPercentages: IIncPercentage[]) =>
          this.incPercentageService.addIncPercentageToCollectionIfMissing<IIncPercentage>(incPercentages, this.frRemittance?.incPercentage)
        )
      )
      .subscribe((incPercentages: IIncPercentage[]) => (this.incPercentagesSharedCollection = incPercentages));
  }
}
