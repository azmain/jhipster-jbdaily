import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMoneyExchange, NewMoneyExchange } from '../money-exchange.model';

export type PartialUpdateMoneyExchange = Partial<IMoneyExchange> & Pick<IMoneyExchange, 'id'>;

export type EntityResponseType = HttpResponse<IMoneyExchange>;
export type EntityArrayResponseType = HttpResponse<IMoneyExchange[]>;

@Injectable({ providedIn: 'root' })
export class MoneyExchangeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/money-exchanges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(moneyExchange: NewMoneyExchange): Observable<EntityResponseType> {
    return this.http.post<IMoneyExchange>(this.resourceUrl, moneyExchange, { observe: 'response' });
  }

  update(moneyExchange: IMoneyExchange): Observable<EntityResponseType> {
    return this.http.put<IMoneyExchange>(`${this.resourceUrl}/${this.getMoneyExchangeIdentifier(moneyExchange)}`, moneyExchange, {
      observe: 'response',
    });
  }

  partialUpdate(moneyExchange: PartialUpdateMoneyExchange): Observable<EntityResponseType> {
    return this.http.patch<IMoneyExchange>(`${this.resourceUrl}/${this.getMoneyExchangeIdentifier(moneyExchange)}`, moneyExchange, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMoneyExchange>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoneyExchange[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
