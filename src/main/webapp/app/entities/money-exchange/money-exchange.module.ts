import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MoneyExchangeComponent } from './list/money-exchange.component';
import { MoneyExchangeDetailComponent } from './detail/money-exchange-detail.component';
import { MoneyExchangeUpdateComponent } from './update/money-exchange-update.component';
import { MoneyExchangeDeleteDialogComponent } from './delete/money-exchange-delete-dialog.component';
import { MoneyExchangeRoutingModule } from './route/money-exchange-routing.module';

@NgModule({
  imports: [SharedModule, MoneyExchangeRoutingModule],
  declarations: [MoneyExchangeComponent, MoneyExchangeDetailComponent, MoneyExchangeUpdateComponent, MoneyExchangeDeleteDialogComponent],
})
export class MoneyExchangeModule {}
