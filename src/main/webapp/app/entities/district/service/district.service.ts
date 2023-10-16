import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDistrict, NewDistrict } from '../district.model';

export type PartialUpdateDistrict = Partial<IDistrict> & Pick<IDistrict, 'id'>;

type RestOf<T extends IDistrict | NewDistrict> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestDistrict = RestOf<IDistrict>;

export type NewRestDistrict = RestOf<NewDistrict>;

export type PartialUpdateRestDistrict = RestOf<PartialUpdateDistrict>;

export type EntityResponseType = HttpResponse<IDistrict>;
export type EntityArrayResponseType = HttpResponse<IDistrict[]>;

@Injectable({ providedIn: 'root' })
export class DistrictService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/districts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(district: NewDistrict): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(district);
    return this.http
      .post<RestDistrict>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(district: IDistrict): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(district);
    return this.http
      .put<RestDistrict>(`${this.resourceUrl}/${this.getDistrictIdentifier(district)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(district: PartialUpdateDistrict): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(district);
    return this.http
      .patch<RestDistrict>(`${this.resourceUrl}/${this.getDistrictIdentifier(district)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDistrict>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDistrict[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDistrictIdentifier(district: Pick<IDistrict, 'id'>): number {
    return district.id;
  }

  compareDistrict(o1: Pick<IDistrict, 'id'> | null, o2: Pick<IDistrict, 'id'> | null): boolean {
    return o1 && o2 ? this.getDistrictIdentifier(o1) === this.getDistrictIdentifier(o2) : o1 === o2;
  }

  addDistrictToCollectionIfMissing<Type extends Pick<IDistrict, 'id'>>(
    districtCollection: Type[],
    ...districtsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const districts: Type[] = districtsToCheck.filter(isPresent);
    if (districts.length > 0) {
      const districtCollectionIdentifiers = districtCollection.map(districtItem => this.getDistrictIdentifier(districtItem)!);
      const districtsToAdd = districts.filter(districtItem => {
        const districtIdentifier = this.getDistrictIdentifier(districtItem);
        if (districtCollectionIdentifiers.includes(districtIdentifier)) {
          return false;
        }
        districtCollectionIdentifiers.push(districtIdentifier);
        return true;
      });
      return [...districtsToAdd, ...districtCollection];
    }
    return districtCollection;
  }

  protected convertDateFromClient<T extends IDistrict | NewDistrict | PartialUpdateDistrict>(district: T): RestOf<T> {
    return {
      ...district,
      createdDate: district.createdDate?.toJSON() ?? null,
      lastModifiedDate: district.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDistrict: RestDistrict): IDistrict {
    return {
      ...restDistrict,
      createdDate: restDistrict.createdDate ? dayjs(restDistrict.createdDate) : undefined,
      lastModifiedDate: restDistrict.lastModifiedDate ? dayjs(restDistrict.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDistrict>): HttpResponse<IDistrict> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDistrict[]>): HttpResponse<IDistrict[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
