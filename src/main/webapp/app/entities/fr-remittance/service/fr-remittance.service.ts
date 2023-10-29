import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFrRemittance, NewFrRemittance } from '../fr-remittance.model';

export type PartialUpdateFrRemittance = Partial<IFrRemittance> & Pick<IFrRemittance, 'id'>;

type RestOf<T extends IFrRemittance | NewFrRemittance> = Omit<
  T,
  'paymentDate' | 'incPaymentDate' | 'remiSendingDate' | 'amountReimDate' | 'incAmountReimDate'
> & {
  paymentDate?: string | null;
  incPaymentDate?: string | null;
  remiSendingDate?: string | null;
  amountReimDate?: string | null;
  incAmountReimDate?: string | null;
};

export type RestFrRemittance = RestOf<IFrRemittance>;

export type NewRestFrRemittance = RestOf<NewFrRemittance>;

export type PartialUpdateRestFrRemittance = RestOf<PartialUpdateFrRemittance>;

export type EntityResponseType = HttpResponse<IFrRemittance>;
export type EntityArrayResponseType = HttpResponse<IFrRemittance[]>;

@Injectable({ providedIn: 'root' })
export class FrRemittanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fr-remittances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(frRemittance: NewFrRemittance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frRemittance);
    return this.http
      .post<RestFrRemittance>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(frRemittance: IFrRemittance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frRemittance);
    return this.http
      .put<RestFrRemittance>(`${this.resourceUrl}/${this.getFrRemittanceIdentifier(frRemittance)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(frRemittance: PartialUpdateFrRemittance): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frRemittance);
    return this.http
      .patch<RestFrRemittance>(`${this.resourceUrl}/${this.getFrRemittanceIdentifier(frRemittance)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFrRemittance>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFrRemittance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFrRemittanceIdentifier(frRemittance: Pick<IFrRemittance, 'id'>): number {
    return frRemittance.id;
  }

  compareFrRemittance(o1: Pick<IFrRemittance, 'id'> | null, o2: Pick<IFrRemittance, 'id'> | null): boolean {
    return o1 && o2 ? this.getFrRemittanceIdentifier(o1) === this.getFrRemittanceIdentifier(o2) : o1 === o2;
  }

  addFrRemittanceToCollectionIfMissing<Type extends Pick<IFrRemittance, 'id'>>(
    frRemittanceCollection: Type[],
    ...frRemittancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const frRemittances: Type[] = frRemittancesToCheck.filter(isPresent);
    if (frRemittances.length > 0) {
      const frRemittanceCollectionIdentifiers = frRemittanceCollection.map(
        frRemittanceItem => this.getFrRemittanceIdentifier(frRemittanceItem)!
      );
      const frRemittancesToAdd = frRemittances.filter(frRemittanceItem => {
        const frRemittanceIdentifier = this.getFrRemittanceIdentifier(frRemittanceItem);
        if (frRemittanceCollectionIdentifiers.includes(frRemittanceIdentifier)) {
          return false;
        }
        frRemittanceCollectionIdentifiers.push(frRemittanceIdentifier);
        return true;
      });
      return [...frRemittancesToAdd, ...frRemittanceCollection];
    }
    return frRemittanceCollection;
  }

  protected convertDateFromClient<T extends IFrRemittance | NewFrRemittance | PartialUpdateFrRemittance>(frRemittance: T): RestOf<T> {
    return {
      ...frRemittance,
      paymentDate: frRemittance.paymentDate?.format(DATE_FORMAT) ?? null,
      incPaymentDate: frRemittance.incPaymentDate?.format(DATE_FORMAT) ?? null,
      remiSendingDate: frRemittance.remiSendingDate?.format(DATE_FORMAT) ?? null,
      amountReimDate: frRemittance.amountReimDate?.format(DATE_FORMAT) ?? null,
      incAmountReimDate: frRemittance.incAmountReimDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFrRemittance: RestFrRemittance): IFrRemittance {
    return {
      ...restFrRemittance,
      paymentDate: restFrRemittance.paymentDate ? dayjs(restFrRemittance.paymentDate) : undefined,
      incPaymentDate: restFrRemittance.incPaymentDate ? dayjs(restFrRemittance.incPaymentDate) : undefined,
      remiSendingDate: restFrRemittance.remiSendingDate ? dayjs(restFrRemittance.remiSendingDate) : undefined,
      amountReimDate: restFrRemittance.amountReimDate ? dayjs(restFrRemittance.amountReimDate) : undefined,
      incAmountReimDate: restFrRemittance.incAmountReimDate ? dayjs(restFrRemittance.incAmountReimDate) : undefined,
      createdDate: restFrRemittance.createdDate ? dayjs(restFrRemittance.createdDate) : undefined,
      lastModifiedDate: restFrRemittance.lastModifiedDate ? dayjs(restFrRemittance.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFrRemittance>): HttpResponse<IFrRemittance> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFrRemittance[]>): HttpResponse<IFrRemittance[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
