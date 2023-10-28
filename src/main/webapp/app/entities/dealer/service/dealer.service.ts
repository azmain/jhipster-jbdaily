import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDealer, NewDealer } from '../dealer.model';

export type PartialUpdateDealer = Partial<IDealer> & Pick<IDealer, 'id'>;

type RestOf<T extends IDealer | NewDealer> = T & {};

export type RestDealer = RestOf<IDealer>;

export type NewRestDealer = RestOf<NewDealer>;

export type PartialUpdateRestDealer = RestOf<PartialUpdateDealer>;

export type EntityResponseType = HttpResponse<IDealer>;
export type EntityArrayResponseType = HttpResponse<IDealer[]>;

@Injectable({ providedIn: 'root' })
export class DealerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dealers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dealer: NewDealer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dealer);
    return this.http
      .post<RestDealer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(dealer: IDealer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dealer);
    return this.http
      .put<RestDealer>(`${this.resourceUrl}/${this.getDealerIdentifier(dealer)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(dealer: PartialUpdateDealer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dealer);
    return this.http
      .patch<RestDealer>(`${this.resourceUrl}/${this.getDealerIdentifier(dealer)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDealer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDealer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDealerIdentifier(dealer: Pick<IDealer, 'id'>): number {
    return dealer.id;
  }

  compareDealer(o1: Pick<IDealer, 'id'> | null, o2: Pick<IDealer, 'id'> | null): boolean {
    return o1 && o2 ? this.getDealerIdentifier(o1) === this.getDealerIdentifier(o2) : o1 === o2;
  }

  addDealerToCollectionIfMissing<Type extends Pick<IDealer, 'id'>>(
    dealerCollection: Type[],
    ...dealersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dealers: Type[] = dealersToCheck.filter(isPresent);
    if (dealers.length > 0) {
      const dealerCollectionIdentifiers = dealerCollection.map(dealerItem => this.getDealerIdentifier(dealerItem)!);
      const dealersToAdd = dealers.filter(dealerItem => {
        const dealerIdentifier = this.getDealerIdentifier(dealerItem);
        if (dealerCollectionIdentifiers.includes(dealerIdentifier)) {
          return false;
        }
        dealerCollectionIdentifiers.push(dealerIdentifier);
        return true;
      });
      return [...dealersToAdd, ...dealerCollection];
    }
    return dealerCollection;
  }

  protected convertDateFromClient<T extends IDealer | NewDealer | PartialUpdateDealer>(dealer: T): RestOf<T> {
    return {
      ...dealer,
    };
  }

  protected convertDateFromServer(restDealer: RestDealer): IDealer {
    return {
      ...restDealer,
      createdDate: restDealer.createdDate ? dayjs(restDealer.createdDate) : undefined,
      lastModifiedDate: restDealer.lastModifiedDate ? dayjs(restDealer.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDealer>): HttpResponse<IDealer> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDealer[]>): HttpResponse<IDealer[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
