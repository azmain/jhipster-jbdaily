import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUpazila, NewUpazila } from '../upazila.model';

export type PartialUpdateUpazila = Partial<IUpazila> & Pick<IUpazila, 'id'>;

type RestOf<T extends IUpazila | NewUpazila> = T & {};

export type RestUpazila = RestOf<IUpazila>;

export type NewRestUpazila = RestOf<NewUpazila>;

export type PartialUpdateRestUpazila = RestOf<PartialUpdateUpazila>;

export type EntityResponseType = HttpResponse<IUpazila>;
export type EntityArrayResponseType = HttpResponse<IUpazila[]>;

@Injectable({ providedIn: 'root' })
export class UpazilaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/upazilas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(upazila: NewUpazila): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(upazila);
    return this.http
      .post<RestUpazila>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(upazila: IUpazila): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(upazila);
    return this.http
      .put<RestUpazila>(`${this.resourceUrl}/${this.getUpazilaIdentifier(upazila)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(upazila: PartialUpdateUpazila): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(upazila);
    return this.http
      .patch<RestUpazila>(`${this.resourceUrl}/${this.getUpazilaIdentifier(upazila)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUpazila>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUpazila[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUpazilaIdentifier(upazila: Pick<IUpazila, 'id'>): number {
    return upazila.id;
  }

  compareUpazila(o1: Pick<IUpazila, 'id'> | null, o2: Pick<IUpazila, 'id'> | null): boolean {
    return o1 && o2 ? this.getUpazilaIdentifier(o1) === this.getUpazilaIdentifier(o2) : o1 === o2;
  }

  addUpazilaToCollectionIfMissing<Type extends Pick<IUpazila, 'id'>>(
    upazilaCollection: Type[],
    ...upazilasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const upazilas: Type[] = upazilasToCheck.filter(isPresent);
    if (upazilas.length > 0) {
      const upazilaCollectionIdentifiers = upazilaCollection.map(upazilaItem => this.getUpazilaIdentifier(upazilaItem)!);
      const upazilasToAdd = upazilas.filter(upazilaItem => {
        const upazilaIdentifier = this.getUpazilaIdentifier(upazilaItem);
        if (upazilaCollectionIdentifiers.includes(upazilaIdentifier)) {
          return false;
        }
        upazilaCollectionIdentifiers.push(upazilaIdentifier);
        return true;
      });
      return [...upazilasToAdd, ...upazilaCollection];
    }
    return upazilaCollection;
  }

  protected convertDateFromClient<T extends IUpazila | NewUpazila | PartialUpdateUpazila>(upazila: T): RestOf<T> {
    return {
      ...upazila,
    };
  }

  protected convertDateFromServer(restUpazila: RestUpazila): IUpazila {
    return {
      ...restUpazila,
      createdDate: restUpazila.createdDate ? dayjs(restUpazila.createdDate) : undefined,
      lastModifiedDate: restUpazila.lastModifiedDate ? dayjs(restUpazila.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUpazila>): HttpResponse<IUpazila> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUpazila[]>): HttpResponse<IUpazila[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
