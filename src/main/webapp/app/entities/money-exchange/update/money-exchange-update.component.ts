import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MoneyExchangeFormService, MoneyExchangeFormGroup } from './money-exchange-form.service';
import { IMoneyExchange } from '../money-exchange.model';
import { MoneyExchangeService } from '../service/money-exchange.service';

@Component({
  selector: 'jhi-money-exchange-update',
  templateUrl: './money-exchange-update.component.html',
})
export class MoneyExchangeUpdateComponent implements OnInit {
  isSaving = false;
  moneyExchange: IMoneyExchange | null = null;

  editForm: MoneyExchangeFormGroup = this.moneyExchangeFormService.createMoneyExchangeFormGroup();

  constructor(
    protected moneyExchangeService: MoneyExchangeService,
    protected moneyExchangeFormService: MoneyExchangeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moneyExchange }) => {
      this.moneyExchange = moneyExchange;
      if (moneyExchange) {
        this.updateForm(moneyExchange);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const moneyExchange = this.moneyExchangeFormService.getMoneyExchange(this.editForm);
    if (moneyExchange.id !== null) {
      this.subscribeToSaveResponse(this.moneyExchangeService.update(moneyExchange));
    } else {
      this.subscribeToSaveResponse(this.moneyExchangeService.create(moneyExchange));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMoneyExchange>>): void {
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

  protected updateForm(moneyExchange: IMoneyExchange): void {
    this.moneyExchange = moneyExchange;
    this.moneyExchangeFormService.resetForm(this.editForm, moneyExchange);
  }
}
