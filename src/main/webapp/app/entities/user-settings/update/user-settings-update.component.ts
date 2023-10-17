import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { UserSettingsFormService, UserSettingsFormGroup } from './user-settings-form.service';
import { IUserSettings } from '../user-settings.model';
import { UserSettingsService } from '../service/user-settings.service';

@Component({
  selector: 'jhi-user-settings-update',
  templateUrl: './user-settings-update.component.html',
})
export class UserSettingsUpdateComponent implements OnInit {
  isSaving = false;
  userSettings: IUserSettings | null = null;

  editForm: UserSettingsFormGroup = this.userSettingsFormService.createUserSettingsFormGroup();

  constructor(
    protected userSettingsService: UserSettingsService,
    protected userSettingsFormService: UserSettingsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    console.log('init form', this.editForm);
    this.activatedRoute.data.subscribe(({ userSettings }) => {
      this.userSettings = userSettings;
      if (userSettings) {
        this.updateForm(userSettings);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userSettings = this.userSettingsFormService.getUserSettings(this.editForm);
    if (userSettings.id !== null) {
      this.subscribeToSaveResponse(this.userSettingsService.update(userSettings));
    } else {
      this.subscribeToSaveResponse(this.userSettingsService.create(userSettings));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserSettings>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(userSettings: IUserSettings): void {
    this.userSettings = userSettings;
    this.userSettingsFormService.resetForm(this.editForm, userSettings);
  }
}
