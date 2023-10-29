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
import { FilterOptions, IFilterOptions } from 'app/shared/filter/filter.model';
import { ITEMS_FOR_DROPDOWN } from 'app/config/pagination.constants';
import { ASC, DESC } from 'app/config/navigation.constants';

@Component({
  selector: 'jhi-fr-remittance-update',
  templateUrl: './fr-remittance-update.component.html',
})
export class FrRemittanceUpdateComponent implements OnInit {
  isSaving = false;
  frRemittance: IFrRemittance | null = null;
  // transactionTypeValues = Object.keys(TransactionType);
  transactionTypeValues = TransactionType;

  genderValues = Gender;
  documentTypeValues = DocumentType;

  moneyExchangesSharedCollection: IMoneyExchange[] = [];
  incPercentagesSharedCollection: IIncPercentage[] = [];

  predicate = 'createdDate';
  ascending = false;
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_FOR_DROPDOWN;
  totalItems = 0;
  page = 1;

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

      console.log('tr type', this.transactionTypeValues);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;

    console.log('form', this.editForm);
    const frRemittance = this.frRemittanceFormService.getFrRemittance(this.editForm);
    console.log('after make from form', frRemittance);
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
    console.log('after update form', this.editForm);

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
    const queryObject = {
      page: this.page - 1,
      size: this.itemsPerPage,
      eagerload: false,
      sort: this.getSortQueryParam(this.predicate, this.ascending),
    };
    this.moneyExchangeService
      .query(queryObject)
      .pipe(map((res: HttpResponse<IMoneyExchange[]>) => (res.body ? res.body.map(item => this.convertMoneyExchangeOption(item)) : [])))
      .pipe(
        map((moneyExchanges: IMoneyExchange[]) =>
          this.moneyExchangeService.addMoneyExchangeToCollectionIfMissing<IMoneyExchange>(moneyExchanges, this.frRemittance?.moneyExchange)
        )
      )
      .subscribe((moneyExchanges: IMoneyExchange[]) => (this.moneyExchangesSharedCollection = moneyExchanges));

    this.incPercentageService
      .query(queryObject)
      .pipe(
        map((res: HttpResponse<IIncPercentage[]>) => (res.body ? res.body.map(item => this.convertIncentivePercentageOption(item)) : []))
      )
      .pipe(
        map((incPercentages: IIncPercentage[]) =>
          this.incPercentageService.addIncPercentageToCollectionIfMissing<IIncPercentage>(incPercentages, this.frRemittance?.incPercentage)
        )
      )
      .subscribe((incPercentages: IIncPercentage[]) => (this.incPercentagesSharedCollection = incPercentages));
  }

  convertMoneyExchangeOption(moneyExchange: IMoneyExchange): IMoneyExchange {
    return {
      id: moneyExchange.id,
      name: moneyExchange.name,
      shortName: moneyExchange.shortName,
    };
  }

  convertIncentivePercentageOption(incPercentage: IIncPercentage): IIncPercentage {
    return {
      id: incPercentage.id,
      name: incPercentage.name,
    };
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
