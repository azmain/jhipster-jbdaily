import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFrRemittance } from '../fr-remittance.model';
import { FrRemittanceService } from '../service/fr-remittance.service';

@Injectable({ providedIn: 'root' })
export class FrRemittanceRoutingResolveService implements Resolve<IFrRemittance | null> {
  constructor(protected service: FrRemittanceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFrRemittance | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((frRemittance: HttpResponse<IFrRemittance>) => {
          if (frRemittance.body) {
            return of(frRemittance.body);
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
