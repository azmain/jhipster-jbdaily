import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIncPercentage, NewIncPercentage } from '../inc-percentage.model';

export type PartialUpdateIncPercentage = Partial<IIncPercentage> & Pick<IIncPercentage, 'id'>;

export type EntityResponseType = HttpResponse<IIncPercentage>;
export type EntityArrayResponseType = HttpResponse<IIncPercentage[]>;

@Injectable({ providedIn: 'root' })
export class IncPercentageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inc-percentages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(incPercentage: NewIncPercentage): Observable<EntityResponseType> {
    return this.http.post<IIncPercentage>(this.resourceUrl, incPercentage, { observe: 'response' });
  }

  update(incPercentage: IIncPercentage): Observable<EntityResponseType> {
    return this.http.put<IIncPercentage>(`${this.resourceUrl}/${this.getIncPercentageIdentifier(incPercentage)}`, incPercentage, {
      observe: 'response',
    });
  }

  partialUpdate(incPercentage: PartialUpdateIncPercentage): Observable<EntityResponseType> {
    return this.http.patch<IIncPercentage>(`${this.resourceUrl}/${this.getIncPercentageIdentifier(incPercentage)}`, incPercentage, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIncPercentage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIncPercentage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIncPercentageIdentifier(incPercentage: Pick<IIncPercentage, 'id'>): number {
    return incPercentage.id;
  }

  compareIncPercentage(o1: Pick<IIncPercentage, 'id'> | null, o2: Pick<IIncPercentage, 'id'> | null): boolean {
    return o1 && o2 ? this.getIncPercentageIdentifier(o1) === this.getIncPercentageIdentifier(o2) : o1 === o2;
  }

  addIncPercentageToCollectionIfMissing<Type extends Pick<IIncPercentage, 'id'>>(
    incPercentageCollection: Type[],
    ...incPercentagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const incPercentages: Type[] = incPercentagesToCheck.filter(isPresent);
    if (incPercentages.length > 0) {
      const incPercentageCollectionIdentifiers = incPercentageCollection.map(
        incPercentageItem => this.getIncPercentageIdentifier(incPercentageItem)!
      );
      const incPercentagesToAdd = incPercentages.filter(incPercentageItem => {
        const incPercentageIdentifier = this.getIncPercentageIdentifier(incPercentageItem);
        if (incPercentageCollectionIdentifiers.includes(incPercentageIdentifier)) {
          return false;
        }
        incPercentageCollectionIdentifiers.push(incPercentageIdentifier);
        return true;
      });
      return [...incPercentagesToAdd, ...incPercentageCollection];
    }
    return incPercentageCollection;
  }
}
