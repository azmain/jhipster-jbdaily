import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMoneyExchange, NewMoneyExchange } from '../money-exchange.model';

export type PartialUpdateMoneyExchange = Partial<IMoneyExchange> & Pick<IMoneyExchange, 'id'>;

type RestOf<T extends IMoneyExchange | NewMoneyExchange> = T & {};

export type RestMoneyExchange = RestOf<IMoneyExchange>;

export type NewRestMoneyExchange = RestOf<NewMoneyExchange>;

export type PartialUpdateRestMoneyExchange = RestOf<PartialUpdateMoneyExchange>;

export type EntityResponseType = HttpResponse<IMoneyExchange>;
export type EntityArrayResponseType = HttpResponse<IMoneyExchange[]>;

@Injectable({ providedIn: 'root' })
export class MoneyExchangeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/money-exchanges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(moneyExchange: NewMoneyExchange): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyExchange);
    return this.http
      .post<RestMoneyExchange>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(moneyExchange: IMoneyExchange): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyExchange);
    return this.http
      .put<RestMoneyExchange>(`${this.resourceUrl}/${this.getMoneyExchangeIdentifier(moneyExchange)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(moneyExchange: PartialUpdateMoneyExchange): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(moneyExchange);
    return this.http
      .patch<RestMoneyExchange>(`${this.resourceUrl}/${this.getMoneyExchangeIdentifier(moneyExchange)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMoneyExchange>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMoneyExchange[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMoneyExchangeIdentifier(moneyExchange: Pick<IMoneyExchange, 'id'>): number {
    return moneyExchange.id;
  }

  compareMoneyExchange(o1: Pick<IMoneyExchange, 'id'> | null, o2: Pick<IMoneyExchange, 'id'> | null): boolean {
    return o1 && o2 ? this.getMoneyExchangeIdentifier(o1) === this.getMoneyExchangeIdentifier(o2) : o1 === o2;
  }

  addMoneyExchangeToCollectionIfMissing<Type extends Pick<IMoneyExchange, 'id'>>(
    moneyExchangeCollection: Type[],
    ...moneyExchangesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const moneyExchanges: Type[] = moneyExchangesToCheck.filter(isPresent);
    if (moneyExchanges.length > 0) {
      const moneyExchangeCollectionIdentifiers = moneyExchangeCollection.map(
        moneyExchangeItem => this.getMoneyExchangeIdentifier(moneyExchangeItem)!
      );
      const moneyExchangesToAdd = moneyExchanges.filter(moneyExchangeItem => {
        const moneyExchangeIdentifier = this.getMoneyExchangeIdentifier(moneyExchangeItem);
        if (moneyExchangeCollectionIdentifiers.includes(moneyExchangeIdentifier)) {
          return false;
        }
        moneyExchangeCollectionIdentifiers.push(moneyExchangeIdentifier);
        return true;
      });
      return [...moneyExchangesToAdd, ...moneyExchangeCollection];
    }
    return moneyExchangeCollection;
  }

  protected convertDateFromClient<T extends IMoneyExchange | NewMoneyExchange | PartialUpdateMoneyExchange>(moneyExchange: T): RestOf<T> {
    return {
      ...moneyExchange,
    };
  }

  protected convertDateFromServer(restMoneyExchange: RestMoneyExchange): IMoneyExchange {
    return {
      ...restMoneyExchange,
      createdDate: restMoneyExchange.createdDate ? dayjs(restMoneyExchange.createdDate) : undefined,
      lastModifiedDate: restMoneyExchange.lastModifiedDate ? dayjs(restMoneyExchange.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMoneyExchange>): HttpResponse<IMoneyExchange> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMoneyExchange[]>): HttpResponse<IMoneyExchange[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
