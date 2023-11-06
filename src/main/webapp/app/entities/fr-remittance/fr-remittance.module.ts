import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FrRemittanceComponent } from './list/fr-remittance.component';
import { FrRemittanceDetailComponent } from './detail/fr-remittance-detail.component';
import { FrRemittanceUpdateComponent } from './update/fr-remittance-update.component';
import { FrRemittanceDeleteDialogComponent } from './delete/fr-remittance-delete-dialog.component';
import { FrRemittanceRoutingModule } from './route/fr-remittance-routing.module';
import { CalendarModule } from 'primeng/calendar';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { FrRegisterComponent } from './register/fr-register.component';
import { TableModule } from 'primeng/table';
import { FrBBReportComponent } from './bb-report/fr-bbreport.component';

@NgModule({
  imports: [SharedModule, FrRemittanceRoutingModule, DropdownModule, CalendarModule, InputTextModule, TableModule],
  declarations: [
    FrRemittanceComponent,
    FrRemittanceDetailComponent,
    FrRemittanceUpdateComponent,
    FrRemittanceDeleteDialogComponent,
    FrRegisterComponent,
    FrBBReportComponent,
  ],
})
export class FrRemittanceModule {}
