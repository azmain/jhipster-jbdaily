import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IncPercentageComponent } from './list/inc-percentage.component';
import { IncPercentageDetailComponent } from './detail/inc-percentage-detail.component';
import { IncPercentageUpdateComponent } from './update/inc-percentage-update.component';
import { IncPercentageDeleteDialogComponent } from './delete/inc-percentage-delete-dialog.component';
import { IncPercentageRoutingModule } from './route/inc-percentage-routing.module';

@NgModule({
  imports: [SharedModule, IncPercentageRoutingModule],
  declarations: [IncPercentageComponent, IncPercentageDetailComponent, IncPercentageUpdateComponent, IncPercentageDeleteDialogComponent],
})
export class IncPercentageModule {}
