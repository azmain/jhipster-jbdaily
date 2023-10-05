import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIncPercentage } from '../inc-percentage.model';
import { IncPercentageService } from '../service/inc-percentage.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './inc-percentage-delete-dialog.component.html',
})
export class IncPercentageDeleteDialogComponent {
  incPercentage?: IIncPercentage;

  constructor(protected incPercentageService: IncPercentageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.incPercentageService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
