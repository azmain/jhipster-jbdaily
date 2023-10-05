import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFrRemittance } from '../fr-remittance.model';
import { FrRemittanceService } from '../service/fr-remittance.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './fr-remittance-delete-dialog.component.html',
})
export class FrRemittanceDeleteDialogComponent {
  frRemittance?: IFrRemittance;

  constructor(protected frRemittanceService: FrRemittanceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.frRemittanceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
