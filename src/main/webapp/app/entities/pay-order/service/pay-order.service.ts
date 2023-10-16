import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPayOrder, NewPayOrder } from '../pay-order.model';

export type PartialUpdatePayOrder = Partial<IPayOrder> & Pick<IPayOrder, 'id'>;

type RestOf<T extends IPayOrder | NewPayOrder> = Omit<T, 'payOrderDate' | 'createdDate' | 'lastModifiedDate'> & {
  payOrderDate?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestPayOrder = RestOf<IPayOrder>;

export type NewRestPayOrder = RestOf<NewPayOrder>;

export type PartialUpdateRestPayOrder = RestOf<PartialUpdatePayOrder>;

export type EntityResponseType = HttpResponse<IPayOrder>;
export type EntityArrayResponseType = HttpResponse<IPayOrder[]>;

@Injectable({ providedIn: 'root' })
export class PayOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pay-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(payOrder: NewPayOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payOrder);
    return this.http
      .post<RestPayOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(payOrder: IPayOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payOrder);
    return this.http
      .put<RestPayOrder>(`${this.resourceUrl}/${this.getPayOrderIdentifier(payOrder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(payOrder: PartialUpdatePayOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payOrder);
    return this.http
      .patch<RestPayOrder>(`${this.resourceUrl}/${this.getPayOrderIdentifier(payOrder)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPayOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPayOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPayOrderIdentifier(payOrder: Pick<IPayOrder, 'id'>): number {
    return payOrder.id;
  }

  comparePayOrder(o1: Pick<IPayOrder, 'id'> | null, o2: Pick<IPayOrder, 'id'> | null): boolean {
    return o1 && o2 ? this.getPayOrderIdentifier(o1) === this.getPayOrderIdentifier(o2) : o1 === o2;
  }

  addPayOrderToCollectionIfMissing<Type extends Pick<IPayOrder, 'id'>>(
    payOrderCollection: Type[],
    ...payOrdersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const payOrders: Type[] = payOrdersToCheck.filter(isPresent);
    if (payOrders.length > 0) {
      const payOrderCollectionIdentifiers = payOrderCollection.map(payOrderItem => this.getPayOrderIdentifier(payOrderItem)!);
      const payOrdersToAdd = payOrders.filter(payOrderItem => {
        const payOrderIdentifier = this.getPayOrderIdentifier(payOrderItem);
        if (payOrderCollectionIdentifiers.includes(payOrderIdentifier)) {
          return false;
        }
        payOrderCollectionIdentifiers.push(payOrderIdentifier);
        return true;
      });
      return [...payOrdersToAdd, ...payOrderCollection];
    }
    return payOrderCollection;
  }

  protected convertDateFromClient<T extends IPayOrder | NewPayOrder | PartialUpdatePayOrder>(payOrder: T): RestOf<T> {
    return {
      ...payOrder,
      payOrderDate: payOrder.payOrderDate?.format(DATE_FORMAT) ?? null,
      createdDate: payOrder.createdDate?.toJSON() ?? null,
      lastModifiedDate: payOrder.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPayOrder: RestPayOrder): IPayOrder {
    return {
      ...restPayOrder,
      payOrderDate: restPayOrder.payOrderDate ? dayjs(restPayOrder.payOrderDate) : undefined,
      createdDate: restPayOrder.createdDate ? dayjs(restPayOrder.createdDate) : undefined,
      lastModifiedDate: restPayOrder.lastModifiedDate ? dayjs(restPayOrder.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPayOrder>): HttpResponse<IPayOrder> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPayOrder[]>): HttpResponse<IPayOrder[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
