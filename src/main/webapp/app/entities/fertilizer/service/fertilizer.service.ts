import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFertilizer, NewFertilizer } from '../fertilizer.model';

export type PartialUpdateFertilizer = Partial<IFertilizer> & Pick<IFertilizer, 'id'>;

export type EntityResponseType = HttpResponse<IFertilizer>;
export type EntityArrayResponseType = HttpResponse<IFertilizer[]>;

@Injectable({ providedIn: 'root' })
export class FertilizerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fertilizers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fertilizer: NewFertilizer): Observable<EntityResponseType> {
    return this.http.post<IFertilizer>(this.resourceUrl, fertilizer, { observe: 'response' });
  }

  update(fertilizer: IFertilizer): Observable<EntityResponseType> {
    return this.http.put<IFertilizer>(`${this.resourceUrl}/${this.getFertilizerIdentifier(fertilizer)}`, fertilizer, {
      observe: 'response',
    });
  }

  partialUpdate(fertilizer: PartialUpdateFertilizer): Observable<EntityResponseType> {
    return this.http.patch<IFertilizer>(`${this.resourceUrl}/${this.getFertilizerIdentifier(fertilizer)}`, fertilizer, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFertilizer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFertilizer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFertilizerIdentifier(fertilizer: Pick<IFertilizer, 'id'>): number {
    return fertilizer.id;
  }

  compareFertilizer(o1: Pick<IFertilizer, 'id'> | null, o2: Pick<IFertilizer, 'id'> | null): boolean {
    return o1 && o2 ? this.getFertilizerIdentifier(o1) === this.getFertilizerIdentifier(o2) : o1 === o2;
  }

  addFertilizerToCollectionIfMissing<Type extends Pick<IFertilizer, 'id'>>(
    fertilizerCollection: Type[],
    ...fertilizersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fertilizers: Type[] = fertilizersToCheck.filter(isPresent);
    if (fertilizers.length > 0) {
      const fertilizerCollectionIdentifiers = fertilizerCollection.map(fertilizerItem => this.getFertilizerIdentifier(fertilizerItem)!);
      const fertilizersToAdd = fertilizers.filter(fertilizerItem => {
        const fertilizerIdentifier = this.getFertilizerIdentifier(fertilizerItem);
        if (fertilizerCollectionIdentifiers.includes(fertilizerIdentifier)) {
          return false;
        }
        fertilizerCollectionIdentifiers.push(fertilizerIdentifier);
        return true;
      });
      return [...fertilizersToAdd, ...fertilizerCollection];
    }
    return fertilizerCollection;
  }
}
