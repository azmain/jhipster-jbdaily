import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { UpazilaFormService, UpazilaFormGroup } from './upazila-form.service';
import { IUpazila } from '../upazila.model';
import { UpazilaService } from '../service/upazila.service';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';

@Component({
  selector: 'jhi-upazila-update',
  templateUrl: './upazila-update.component.html',
})
export class UpazilaUpdateComponent implements OnInit {
  isSaving = false;
  upazila: IUpazila | null = null;

  districtsSharedCollection: IDistrict[] = [];

  editForm: UpazilaFormGroup = this.upazilaFormService.createUpazilaFormGroup();

  constructor(
    protected upazilaService: UpazilaService,
    protected upazilaFormService: UpazilaFormService,
    protected districtService: DistrictService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDistrict = (o1: IDistrict | null, o2: IDistrict | null): boolean => this.districtService.compareDistrict(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ upazila }) => {
      this.upazila = upazila;
      if (upazila) {
        this.updateForm(upazila);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const upazila = this.upazilaFormService.getUpazila(this.editForm);
    if (upazila.id !== null) {
      this.subscribeToSaveResponse(this.upazilaService.update(upazila));
    } else {
      this.subscribeToSaveResponse(this.upazilaService.create(upazila));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUpazila>>): void {
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

  protected updateForm(upazila: IUpazila): void {
    this.upazila = upazila;
    this.upazilaFormService.resetForm(this.editForm, upazila);

    this.districtsSharedCollection = this.districtService.addDistrictToCollectionIfMissing<IDistrict>(
      this.districtsSharedCollection,
      upazila.district
    );
  }

  protected loadRelationshipsOptions(): void {
    this.districtService
      .query()
      .pipe(map((res: HttpResponse<IDistrict[]>) => res.body ?? []))
      .pipe(
        map((districts: IDistrict[]) => this.districtService.addDistrictToCollectionIfMissing<IDistrict>(districts, this.upazila?.district))
      )
      .subscribe((districts: IDistrict[]) => (this.districtsSharedCollection = districts));
  }
}
