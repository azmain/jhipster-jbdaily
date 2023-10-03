import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FertilizerFormService, FertilizerFormGroup } from './fertilizer-form.service';
import { IFertilizer } from '../fertilizer.model';
import { FertilizerService } from '../service/fertilizer.service';

@Component({
  selector: 'jhi-fertilizer-update',
  templateUrl: './fertilizer-update.component.html',
})
export class FertilizerUpdateComponent implements OnInit {
  isSaving = false;
  fertilizer: IFertilizer | null = null;

  editForm: FertilizerFormGroup = this.fertilizerFormService.createFertilizerFormGroup();

  constructor(
    protected fertilizerService: FertilizerService,
    protected fertilizerFormService: FertilizerFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fertilizer }) => {
      this.fertilizer = fertilizer;
      if (fertilizer) {
        this.updateForm(fertilizer);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fertilizer = this.fertilizerFormService.getFertilizer(this.editForm);
    if (fertilizer.id !== null) {
      this.subscribeToSaveResponse(this.fertilizerService.update(fertilizer));
    } else {
      this.subscribeToSaveResponse(this.fertilizerService.create(fertilizer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFertilizer>>): void {
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

  protected updateForm(fertilizer: IFertilizer): void {
    this.fertilizer = fertilizer;
    this.fertilizerFormService.resetForm(this.editForm, fertilizer);
  }
}
