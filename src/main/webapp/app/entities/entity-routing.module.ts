import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'division',
        data: { pageTitle: 'jbdailyApp.division.home.title' },
        loadChildren: () => import('./division/division.module').then(m => m.DivisionModule),
      },
      {
        path: 'district',
        data: { pageTitle: 'jbdailyApp.district.home.title' },
        loadChildren: () => import('./district/district.module').then(m => m.DistrictModule),
      },
      {
        path: 'upazila',
        data: { pageTitle: 'jbdailyApp.upazila.home.title' },
        loadChildren: () => import('./upazila/upazila.module').then(m => m.UpazilaModule),
      },
      {
        path: 'dealer',
        data: { pageTitle: 'jbdailyApp.dealer.home.title' },
        loadChildren: () => import('./dealer/dealer.module').then(m => m.DealerModule),
      },
      {
        path: 'fertilizer',
        data: { pageTitle: 'jbdailyApp.fertilizer.home.title' },
        loadChildren: () => import('./fertilizer/fertilizer.module').then(m => m.FertilizerModule),
      },
      {
        path: 'pay-order',
        data: { pageTitle: 'jbdailyApp.payOrder.home.title' },
        loadChildren: () => import('./pay-order/pay-order.module').then(m => m.PayOrderModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
