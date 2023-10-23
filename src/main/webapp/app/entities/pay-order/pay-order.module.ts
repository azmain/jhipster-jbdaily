import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PayOrderComponent } from './list/pay-order.component';
import { PayOrderDetailComponent } from './detail/pay-order-detail.component';
import { PayOrderUpdateComponent } from './update/pay-order-update.component';
import { PayOrderDeleteDialogComponent } from './delete/pay-order-delete-dialog.component';
import { PayOrderRoutingModule } from './route/pay-order-routing.module';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { TableModule } from 'primeng/table';
import { PayOrderMicrComponentComponent } from './micr/pay-order-micr-component.component';
import { PayOrderReportComponent } from './po-report/pay-order-report.component';

@NgModule({
  imports: [SharedModule, PayOrderRoutingModule, DropdownModule, CalendarModule, InputTextModule, InputNumberModule, TableModule],
  declarations: [
    PayOrderComponent,
    PayOrderDetailComponent,
    PayOrderUpdateComponent,
    PayOrderDeleteDialogComponent,
    PayOrderMicrComponentComponent,
    PayOrderReportComponent,
  ],
})
export class PayOrderModule {}
