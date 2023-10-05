import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IncPercentageFormService, IncPercentageFormGroup } from './inc-percentage-form.service';
import { IIncPercentage } from '../inc-percentage.model';
import { IncPercentageService } from '../service/inc-percentage.service';

@Component({
  selector: 'jhi-inc-percentage-update',
  templateUrl: './inc-percentage-update.component.html',
})
export class IncPercentageUpdateComponent implements OnInit {
  isSaving = false;
  incPercentage: IIncPercentage | null = null;

  editForm: IncPercentageFormGroup = this.incPercentageFormService.createIncPercentageFormGroup();

  constructor(
    protected incPercentageService: IncPercentageService,
    protected incPercentageFormService: IncPercentageFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incPercentage }) => {
      this.incPercentage = incPercentage;
      if (incPercentage) {
        this.updateForm(incPercentage);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const incPercentage = this.incPercentageFormService.getIncPercentage(this.editForm);
    if (incPercentage.id !== null) {
      this.subscribeToSaveResponse(this.incPercentageService.update(incPercentage));
    } else {
      this.subscribeToSaveResponse(this.incPercentageService.create(incPercentage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIncPercentage>>): void {
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

  protected updateForm(incPercentage: IIncPercentage): void {
    this.incPercentage = incPercentage;
    this.incPercentageFormService.resetForm(this.editForm, incPercentage);
  }
}
