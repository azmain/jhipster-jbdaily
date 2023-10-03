import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUpazila, NewUpazila } from '../upazila.model';

export type PartialUpdateUpazila = Partial<IUpazila> & Pick<IUpazila, 'id'>;

export type EntityResponseType = HttpResponse<IUpazila>;
export type EntityArrayResponseType = HttpResponse<IUpazila[]>;

@Injectable({ providedIn: 'root' })
export class UpazilaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/upazilas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(upazila: NewUpazila): Observable<EntityResponseType> {
    return this.http.post<IUpazila>(this.resourceUrl, upazila, { observe: 'response' });
  }

  update(upazila: IUpazila): Observable<EntityResponseType> {
    return this.http.put<IUpazila>(`${this.resourceUrl}/${this.getUpazilaIdentifier(upazila)}`, upazila, { observe: 'response' });
  }

  partialUpdate(upazila: PartialUpdateUpazila): Observable<EntityResponseType> {
    return this.http.patch<IUpazila>(`${this.resourceUrl}/${this.getUpazilaIdentifier(upazila)}`, upazila, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUpazila>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUpazila[]>(this.resourceUrl, { params: options, observe: 'response' });
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
}
