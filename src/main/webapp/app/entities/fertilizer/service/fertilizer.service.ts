import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFertilizer, NewFertilizer } from '../fertilizer.model';

export type PartialUpdateFertilizer = Partial<IFertilizer> & Pick<IFertilizer, 'id'>;

type RestOf<T extends IFertilizer | NewFertilizer> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestFertilizer = RestOf<IFertilizer>;

export type NewRestFertilizer = RestOf<NewFertilizer>;

export type PartialUpdateRestFertilizer = RestOf<PartialUpdateFertilizer>;

export type EntityResponseType = HttpResponse<IFertilizer>;
export type EntityArrayResponseType = HttpResponse<IFertilizer[]>;

@Injectable({ providedIn: 'root' })
export class FertilizerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fertilizers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fertilizer: NewFertilizer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fertilizer);
    return this.http
      .post<RestFertilizer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(fertilizer: IFertilizer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fertilizer);
    return this.http
      .put<RestFertilizer>(`${this.resourceUrl}/${this.getFertilizerIdentifier(fertilizer)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(fertilizer: PartialUpdateFertilizer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fertilizer);
    return this.http
      .patch<RestFertilizer>(`${this.resourceUrl}/${this.getFertilizerIdentifier(fertilizer)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFertilizer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFertilizer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
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

  protected convertDateFromClient<T extends IFertilizer | NewFertilizer | PartialUpdateFertilizer>(fertilizer: T): RestOf<T> {
    return {
      ...fertilizer,
      createdDate: fertilizer.createdDate?.toJSON() ?? null,
      lastModifiedDate: fertilizer.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFertilizer: RestFertilizer): IFertilizer {
    return {
      ...restFertilizer,
      createdDate: restFertilizer.createdDate ? dayjs(restFertilizer.createdDate) : undefined,
      lastModifiedDate: restFertilizer.lastModifiedDate ? dayjs(restFertilizer.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFertilizer>): HttpResponse<IFertilizer> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFertilizer[]>): HttpResponse<IFertilizer[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
