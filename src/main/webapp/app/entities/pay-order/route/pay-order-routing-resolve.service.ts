import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPayOrder } from '../pay-order.model';
import { PayOrderService } from '../service/pay-order.service';

@Injectable({ providedIn: 'root' })
export class PayOrderRoutingResolveService implements Resolve<IPayOrder | null> {
  constructor(protected service: PayOrderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPayOrder | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((payOrder: HttpResponse<IPayOrder>) => {
          if (payOrder.body) {
            return of(payOrder.body);
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
