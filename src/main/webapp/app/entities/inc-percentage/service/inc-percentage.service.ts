import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIncPercentage, NewIncPercentage } from '../inc-percentage.model';

export type PartialUpdateIncPercentage = Partial<IIncPercentage> & Pick<IIncPercentage, 'id'>;

type RestOf<T extends IIncPercentage | NewIncPercentage> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestIncPercentage = RestOf<IIncPercentage>;

export type NewRestIncPercentage = RestOf<NewIncPercentage>;

export type PartialUpdateRestIncPercentage = RestOf<PartialUpdateIncPercentage>;

export type EntityResponseType = HttpResponse<IIncPercentage>;
export type EntityArrayResponseType = HttpResponse<IIncPercentage[]>;

@Injectable({ providedIn: 'root' })
export class IncPercentageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inc-percentages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(incPercentage: NewIncPercentage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incPercentage);
    return this.http
      .post<RestIncPercentage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(incPercentage: IIncPercentage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incPercentage);
    return this.http
      .put<RestIncPercentage>(`${this.resourceUrl}/${this.getIncPercentageIdentifier(incPercentage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(incPercentage: PartialUpdateIncPercentage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(incPercentage);
    return this.http
      .patch<RestIncPercentage>(`${this.resourceUrl}/${this.getIncPercentageIdentifier(incPercentage)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIncPercentage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIncPercentage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
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

  protected convertDateFromClient<T extends IIncPercentage | NewIncPercentage | PartialUpdateIncPercentage>(incPercentage: T): RestOf<T> {
    return {
      ...incPercentage,
      createdDate: incPercentage.createdDate?.toJSON() ?? null,
      lastModifiedDate: incPercentage.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restIncPercentage: RestIncPercentage): IIncPercentage {
    return {
      ...restIncPercentage,
      createdDate: restIncPercentage.createdDate ? dayjs(restIncPercentage.createdDate) : undefined,
      lastModifiedDate: restIncPercentage.lastModifiedDate ? dayjs(restIncPercentage.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIncPercentage>): HttpResponse<IIncPercentage> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIncPercentage[]>): HttpResponse<IIncPercentage[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
