import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MoneyExchangeComponent } from '../list/money-exchange.component';
import { MoneyExchangeDetailComponent } from '../detail/money-exchange-detail.component';
import { MoneyExchangeUpdateComponent } from '../update/money-exchange-update.component';
import { MoneyExchangeRoutingResolveService } from './money-exchange-routing-resolve.service';
import { ASC, DESC } from 'app/config/navigation.constants';

const moneyExchangeRoute: Routes = [
  {
    path: '',
    component: MoneyExchangeComponent,
    data: {
      defaultSort: 'lastModifiedDate,' + DESC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MoneyExchangeDetailComponent,
    resolve: {
      moneyExchange: MoneyExchangeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MoneyExchangeUpdateComponent,
    resolve: {
      moneyExchange: MoneyExchangeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MoneyExchangeUpdateComponent,
    resolve: {
      moneyExchange: MoneyExchangeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(moneyExchangeRoute)],
  exports: [RouterModule],
})
export class MoneyExchangeRoutingModule {}
