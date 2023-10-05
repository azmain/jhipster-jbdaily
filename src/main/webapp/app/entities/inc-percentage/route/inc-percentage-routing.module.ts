import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IncPercentageComponent } from '../list/inc-percentage.component';
import { IncPercentageDetailComponent } from '../detail/inc-percentage-detail.component';
import { IncPercentageUpdateComponent } from '../update/inc-percentage-update.component';
import { IncPercentageRoutingResolveService } from './inc-percentage-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const incPercentageRoute: Routes = [
  {
    path: '',
    component: IncPercentageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IncPercentageDetailComponent,
    resolve: {
      incPercentage: IncPercentageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IncPercentageUpdateComponent,
    resolve: {
      incPercentage: IncPercentageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IncPercentageUpdateComponent,
    resolve: {
      incPercentage: IncPercentageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(incPercentageRoute)],
  exports: [RouterModule],
})
export class IncPercentageRoutingModule {}
