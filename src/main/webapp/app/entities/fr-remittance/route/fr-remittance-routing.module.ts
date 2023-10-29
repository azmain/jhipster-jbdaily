import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FrRemittanceComponent } from '../list/fr-remittance.component';
import { FrRemittanceDetailComponent } from '../detail/fr-remittance-detail.component';
import { FrRemittanceUpdateComponent } from '../update/fr-remittance-update.component';
import { FrRemittanceRoutingResolveService } from './fr-remittance-routing-resolve.service';
import { ASC, DESC } from 'app/config/navigation.constants';
import { FrRegisterComponent } from '../register/fr-register.component';

const frRemittanceRoute: Routes = [
  {
    path: '',
    component: FrRemittanceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FrRemittanceDetailComponent,
    resolve: {
      frRemittance: FrRemittanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FrRemittanceUpdateComponent,
    resolve: {
      frRemittance: FrRemittanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FrRemittanceUpdateComponent,
    resolve: {
      frRemittance: FrRemittanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'fr-register',
    component: FrRegisterComponent,
    data: { defaultSort: 'createdDate,' + DESC },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(frRemittanceRoute)],
  exports: [RouterModule],
})
export class FrRemittanceRoutingModule {}
