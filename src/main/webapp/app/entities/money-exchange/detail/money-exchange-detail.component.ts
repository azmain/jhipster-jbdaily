import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMoneyExchange } from '../money-exchange.model';

@Component({
  selector: 'jhi-money-exchange-detail',
  templateUrl: './money-exchange-detail.component.html',
})
export class MoneyExchangeDetailComponent implements OnInit {
  moneyExchange: IMoneyExchange | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ moneyExchange }) => {
      this.moneyExchange = moneyExchange;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
