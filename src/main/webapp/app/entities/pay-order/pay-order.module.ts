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
import { PayOrderMicrComponentComponent } from './micr/pay-order-micr-component.component';

@NgModule({
  imports: [SharedModule, PayOrderRoutingModule, DropdownModule, CalendarModule, InputTextModule],
  declarations: [
    PayOrderComponent,
    PayOrderDetailComponent,
    PayOrderUpdateComponent,
    PayOrderDeleteDialogComponent,
    PayOrderMicrComponentComponent,
  ],
})
export class PayOrderModule {}
