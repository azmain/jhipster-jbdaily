import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFertilizer } from '../fertilizer.model';
import { FertilizerService } from '../service/fertilizer.service';

@Injectable({ providedIn: 'root' })
export class FertilizerRoutingResolveService implements Resolve<IFertilizer | null> {
  constructor(protected service: FertilizerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFertilizer | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fertilizer: HttpResponse<IFertilizer>) => {
          if (fertilizer.body) {
            return of(fertilizer.body);
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
