import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDivision, NewDivision } from '../division.model';

export type PartialUpdateDivision = Partial<IDivision> & Pick<IDivision, 'id'>;

type RestOf<T extends IDivision | NewDivision> = T & {};

export type RestDivision = RestOf<IDivision>;

export type NewRestDivision = RestOf<NewDivision>;

export type PartialUpdateRestDivision = RestOf<PartialUpdateDivision>;

export type EntityResponseType = HttpResponse<IDivision>;
export type EntityArrayResponseType = HttpResponse<IDivision[]>;

@Injectable({ providedIn: 'root' })
export class DivisionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/divisions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(division: NewDivision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(division);
    return this.http
      .post<RestDivision>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(division: IDivision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(division);
    return this.http
      .put<RestDivision>(`${this.resourceUrl}/${this.getDivisionIdentifier(division)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(division: PartialUpdateDivision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(division);
    return this.http
      .patch<RestDivision>(`${this.resourceUrl}/${this.getDivisionIdentifier(division)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDivision>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDivision[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDivisionIdentifier(division: Pick<IDivision, 'id'>): number {
    return division.id;
  }

  compareDivision(o1: Pick<IDivision, 'id'> | null, o2: Pick<IDivision, 'id'> | null): boolean {
    return o1 && o2 ? this.getDivisionIdentifier(o1) === this.getDivisionIdentifier(o2) : o1 === o2;
  }

  addDivisionToCollectionIfMissing<Type extends Pick<IDivision, 'id'>>(
    divisionCollection: Type[],
    ...divisionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const divisions: Type[] = divisionsToCheck.filter(isPresent);
    if (divisions.length > 0) {
      const divisionCollectionIdentifiers = divisionCollection.map(divisionItem => this.getDivisionIdentifier(divisionItem)!);
      const divisionsToAdd = divisions.filter(divisionItem => {
        const divisionIdentifier = this.getDivisionIdentifier(divisionItem);
        if (divisionCollectionIdentifiers.includes(divisionIdentifier)) {
          return false;
        }
        divisionCollectionIdentifiers.push(divisionIdentifier);
        return true;
      });
      return [...divisionsToAdd, ...divisionCollection];
    }
    return divisionCollection;
  }

  protected convertDateFromClient<T extends IDivision | NewDivision | PartialUpdateDivision>(division: T): RestOf<T> {
    return {
      ...division,
    };
  }

  protected convertDateFromServer(restDivision: RestDivision): IDivision {
    return {
      ...restDivision,
      createdDate: restDivision.createdDate ? dayjs(restDivision.createdDate) : undefined,
      lastModifiedDate: restDivision.lastModifiedDate ? dayjs(restDivision.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDivision>): HttpResponse<IDivision> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDivision[]>): HttpResponse<IDivision[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
