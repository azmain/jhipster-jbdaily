import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMoneyExchange } from '../money-exchange.model';
import { MoneyExchangeService } from '../service/money-exchange.service';

@Injectable({ providedIn: 'root' })
export class MoneyExchangeRoutingResolveService implements Resolve<IMoneyExchange | null> {
  constructor(protected service: MoneyExchangeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMoneyExchange | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((moneyExchange: HttpResponse<IMoneyExchange>) => {
          if (moneyExchange.body) {
            return of(moneyExchange.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
