import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FertilizerComponent } from '../list/fertilizer.component';
import { FertilizerDetailComponent } from '../detail/fertilizer-detail.component';
import { FertilizerUpdateComponent } from '../update/fertilizer-update.component';
import { FertilizerRoutingResolveService } from './fertilizer-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fertilizerRoute: Routes = [
  {
    path: '',
    component: FertilizerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FertilizerDetailComponent,
    resolve: {
      fertilizer: FertilizerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FertilizerUpdateComponent,
    resolve: {
      fertilizer: FertilizerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FertilizerUpdateComponent,
    resolve: {
      fertilizer: FertilizerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fertilizerRoute)],
  exports: [RouterModule],
})
export class FertilizerRoutingModule {}
