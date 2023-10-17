import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PayOrderComponent } from '../list/pay-order.component';
import { PayOrderDetailComponent } from '../detail/pay-order-detail.component';
import { PayOrderUpdateComponent } from '../update/pay-order-update.component';
import { PayOrderRoutingResolveService } from './pay-order-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { PayOrderMicrComponentComponent } from '../micr/pay-order-micr-component.component';

const payOrderRoute: Routes = [
  {
    path: '',
    component: PayOrderComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PayOrderDetailComponent,
    resolve: {
      payOrder: PayOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/micr',
    component: PayOrderMicrComponentComponent,
    resolve: {
      payOrder: PayOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PayOrderUpdateComponent,
    resolve: {
      payOrder: PayOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PayOrderUpdateComponent,
    resolve: {
      payOrder: PayOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(payOrderRoute)],
  exports: [RouterModule],
})
export class PayOrderRoutingModule {}
