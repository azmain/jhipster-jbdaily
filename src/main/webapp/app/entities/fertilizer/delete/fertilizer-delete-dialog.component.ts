import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFertilizer } from '../fertilizer.model';
import { FertilizerService } from '../service/fertilizer.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './fertilizer-delete-dialog.component.html',
})
export class FertilizerDeleteDialogComponent {
  fertilizer?: IFertilizer;

  constructor(protected fertilizerService: FertilizerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fertilizerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
