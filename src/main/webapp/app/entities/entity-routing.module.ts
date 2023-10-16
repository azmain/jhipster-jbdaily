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
      {
        path: 'fr-remittance',
        data: { pageTitle: 'jbdailyApp.frRemittance.home.title' },
        loadChildren: () => import('./fr-remittance/fr-remittance.module').then(m => m.FrRemittanceModule),
      },
      {
        path: 'inc-percentage',
        data: { pageTitle: 'jbdailyApp.incPercentage.home.title' },
        loadChildren: () => import('./inc-percentage/inc-percentage.module').then(m => m.IncPercentageModule),
      },
      {
        path: 'money-exchange',
        data: { pageTitle: 'jbdailyApp.moneyExchange.home.title' },
        loadChildren: () => import('./money-exchange/money-exchange.module').then(m => m.MoneyExchangeModule),
      },
      {
        path: 'user-settings',
        data: { pageTitle: 'jbdailyApp.userSettings.home.title' },
        loadChildren: () => import('./user-settings/user-settings.module').then(m => m.UserSettingsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
