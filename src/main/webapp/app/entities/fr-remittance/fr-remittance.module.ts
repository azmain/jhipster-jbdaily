import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FrRemittanceComponent } from './list/fr-remittance.component';
import { FrRemittanceDetailComponent } from './detail/fr-remittance-detail.component';
import { FrRemittanceUpdateComponent } from './update/fr-remittance-update.component';
import { FrRemittanceDeleteDialogComponent } from './delete/fr-remittance-delete-dialog.component';
import { FrRemittanceRoutingModule } from './route/fr-remittance-routing.module';

@NgModule({
  imports: [SharedModule, FrRemittanceRoutingModule],
  declarations: [FrRemittanceComponent, FrRemittanceDetailComponent, FrRemittanceUpdateComponent, FrRemittanceDeleteDialogComponent],
})
export class FrRemittanceModule {}
