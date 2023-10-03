import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPayOrder } from '../pay-order.model';

@Component({
  selector: 'jhi-pay-order-detail',
  templateUrl: './pay-order-detail.component.html',
})
export class PayOrderDetailComponent implements OnInit {
  payOrder: IPayOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ payOrder }) => {
      this.payOrder = payOrder;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
