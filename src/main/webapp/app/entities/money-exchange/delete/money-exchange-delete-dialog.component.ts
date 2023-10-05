import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMoneyExchange } from '../money-exchange.model';
import { MoneyExchangeService } from '../service/money-exchange.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './money-exchange-delete-dialog.component.html',
})
export class MoneyExchangeDeleteDialogComponent {
  moneyExchange?: IMoneyExchange;

  constructor(protected moneyExchangeService: MoneyExchangeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.moneyExchangeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
