import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FertilizerComponent } from './list/fertilizer.component';
import { FertilizerDetailComponent } from './detail/fertilizer-detail.component';
import { FertilizerUpdateComponent } from './update/fertilizer-update.component';
import { FertilizerDeleteDialogComponent } from './delete/fertilizer-delete-dialog.component';
import { FertilizerRoutingModule } from './route/fertilizer-routing.module';

@NgModule({
  imports: [SharedModule, FertilizerRoutingModule],
  declarations: [FertilizerComponent, FertilizerDetailComponent, FertilizerUpdateComponent, FertilizerDeleteDialogComponent],
})
export class FertilizerModule {}
