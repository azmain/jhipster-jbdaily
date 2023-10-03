import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPayOrder } from '../pay-order.model';
import { PayOrderService } from '../service/pay-order.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './pay-order-delete-dialog.component.html',
})
export class PayOrderDeleteDialogComponent {
  payOrder?: IPayOrder;

  constructor(protected payOrderService: PayOrderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.payOrderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
