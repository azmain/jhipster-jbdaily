import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UpazilaComponent } from './list/upazila.component';
import { UpazilaDetailComponent } from './detail/upazila-detail.component';
import { UpazilaUpdateComponent } from './update/upazila-update.component';
import { UpazilaDeleteDialogComponent } from './delete/upazila-delete-dialog.component';
import { UpazilaRoutingModule } from './route/upazila-routing.module';
import { DropdownModule } from 'primeng/dropdown';

@NgModule({
  imports: [SharedModule, UpazilaRoutingModule, DropdownModule],
  declarations: [UpazilaComponent, UpazilaDetailComponent, UpazilaUpdateComponent, UpazilaDeleteDialogComponent],
})
export class UpazilaModule {}
