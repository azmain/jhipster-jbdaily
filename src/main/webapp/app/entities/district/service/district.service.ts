import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDistrict, NewDistrict } from '../district.model';

export type PartialUpdateDistrict = Partial<IDistrict> & Pick<IDistrict, 'id'>;

export type EntityResponseType = HttpResponse<IDistrict>;
export type EntityArrayResponseType = HttpResponse<IDistrict[]>;

@Injectable({ providedIn: 'root' })
export class DistrictService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/districts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(district: NewDistrict): Observable<EntityResponseType> {
    return this.http.post<IDistrict>(this.resourceUrl, district, { observe: 'response' });
  }

  update(district: IDistrict): Observable<EntityResponseType> {
    return this.http.put<IDistrict>(`${this.resourceUrl}/${this.getDistrictIdentifier(district)}`, district, { observe: 'response' });
  }

  partialUpdate(district: PartialUpdateDistrict): Observable<EntityResponseType> {
    return this.http.patch<IDistrict>(`${this.resourceUrl}/${this.getDistrictIdentifier(district)}`, district, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDistrict>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDistrict[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
