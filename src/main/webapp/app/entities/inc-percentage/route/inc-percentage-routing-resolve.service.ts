import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIncPercentage } from '../inc-percentage.model';
import { IncPercentageService } from '../service/inc-percentage.service';

@Injectable({ providedIn: 'root' })
export class IncPercentageRoutingResolveService implements Resolve<IIncPercentage | null> {
  constructor(protected service: IncPercentageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIncPercentage | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((incPercentage: HttpResponse<IIncPercentage>) => {
          if (incPercentage.body) {
            return of(incPercentage.body);
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
