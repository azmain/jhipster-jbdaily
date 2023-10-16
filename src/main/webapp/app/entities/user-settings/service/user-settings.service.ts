import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserSettings, NewUserSettings } from '../user-settings.model';

export type PartialUpdateUserSettings = Partial<IUserSettings> & Pick<IUserSettings, 'id'>;

type RestOf<T extends IUserSettings | NewUserSettings> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestUserSettings = RestOf<IUserSettings>;

export type NewRestUserSettings = RestOf<NewUserSettings>;

export type PartialUpdateRestUserSettings = RestOf<PartialUpdateUserSettings>;

export type EntityResponseType = HttpResponse<IUserSettings>;
export type EntityArrayResponseType = HttpResponse<IUserSettings[]>;

@Injectable({ providedIn: 'root' })
export class UserSettingsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-settings');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userSettings: NewUserSettings): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userSettings);
    return this.http
      .post<RestUserSettings>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userSettings: IUserSettings): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userSettings);
    return this.http
      .put<RestUserSettings>(`${this.resourceUrl}/${this.getUserSettingsIdentifier(userSettings)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userSettings: PartialUpdateUserSettings): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userSettings);
    return this.http
      .patch<RestUserSettings>(`${this.resourceUrl}/${this.getUserSettingsIdentifier(userSettings)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserSettings>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserSettings[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserSettingsIdentifier(userSettings: Pick<IUserSettings, 'id'>): number {
    return userSettings.id;
  }

  compareUserSettings(o1: Pick<IUserSettings, 'id'> | null, o2: Pick<IUserSettings, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserSettingsIdentifier(o1) === this.getUserSettingsIdentifier(o2) : o1 === o2;
  }

  addUserSettingsToCollectionIfMissing<Type extends Pick<IUserSettings, 'id'>>(
    userSettingsCollection: Type[],
    ...userSettingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userSettings: Type[] = userSettingsToCheck.filter(isPresent);
    if (userSettings.length > 0) {
      const userSettingsCollectionIdentifiers = userSettingsCollection.map(
        userSettingsItem => this.getUserSettingsIdentifier(userSettingsItem)!
      );
      const userSettingsToAdd = userSettings.filter(userSettingsItem => {
        const userSettingsIdentifier = this.getUserSettingsIdentifier(userSettingsItem);
        if (userSettingsCollectionIdentifiers.includes(userSettingsIdentifier)) {
          return false;
        }
        userSettingsCollectionIdentifiers.push(userSettingsIdentifier);
        return true;
      });
      return [...userSettingsToAdd, ...userSettingsCollection];
    }
    return userSettingsCollection;
  }

  protected convertDateFromClient<T extends IUserSettings | NewUserSettings | PartialUpdateUserSettings>(userSettings: T): RestOf<T> {
    return {
      ...userSettings,
      createdDate: userSettings.createdDate?.toJSON() ?? null,
      lastModifiedDate: userSettings.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserSettings: RestUserSettings): IUserSettings {
    return {
      ...restUserSettings,
      createdDate: restUserSettings.createdDate ? dayjs(restUserSettings.createdDate) : undefined,
      lastModifiedDate: restUserSettings.lastModifiedDate ? dayjs(restUserSettings.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserSettings>): HttpResponse<IUserSettings> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserSettings[]>): HttpResponse<IUserSettings[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
