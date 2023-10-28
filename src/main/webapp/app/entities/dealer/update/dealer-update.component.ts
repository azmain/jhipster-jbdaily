import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { DealerFormService, DealerFormGroup } from './dealer-form.service';
import { IDealer } from '../dealer.model';
import { DealerService } from '../service/dealer.service';
import { IUpazila } from 'app/entities/upazila/upazila.model';
import { UpazilaService } from 'app/entities/upazila/service/upazila.service';
import { ITEMS_FOR_DROPDOWN, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { FilterOptions, IFilterOptions } from 'app/shared/filter/filter.model';
import { ASC, DESC } from 'app/config/navigation.constants';

@Component({
  selector: 'jhi-dealer-update',
  templateUrl: './dealer-update.component.html',
})
export class DealerUpdateComponent implements OnInit {
  isSaving = false;
  dealer: IDealer | null = null;

  upazilasSharedCollection: IUpazila[] = [];

  predicate = 'createdDate';
  ascending = false;
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_FOR_DROPDOWN;
  totalItems = 0;
  page = 1;

  editForm: DealerFormGroup = this.dealerFormService.createDealerFormGroup();

  constructor(
    protected dealerService: DealerService,
    protected dealerFormService: DealerFormService,
    protected upazilaService: UpazilaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUpazila = (o1: IUpazila | null, o2: IUpazila | null): boolean => this.upazilaService.compareUpazila(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dealer }) => {
      this.dealer = dealer;
      if (dealer) {
        this.updateForm(dealer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    console.log('edit form', this.editForm);
    const dealer = this.dealerFormService.getDealer(this.editForm);
    console.log('dealer', this.dealer);
    if (dealer.id !== null) {
      this.subscribeToSaveResponse(this.dealerService.update(dealer));
    } else {
      this.subscribeToSaveResponse(this.dealerService.create(dealer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDealer>>): void {
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

  protected updateForm(dealer: IDealer): void {
    this.dealer = dealer;
    this.dealerFormService.resetForm(this.editForm, dealer);

    this.upazilasSharedCollection = this.upazilaService.addUpazilaToCollectionIfMissing<IUpazila>(
      this.upazilasSharedCollection,
      dealer.upazila
    );
  }

  protected loadRelationshipsOptions(): void {
    const queryObject = {
      page: this.page - 1,
      size: this.itemsPerPage,
      eagerload: false,
      sort: this.getSortQueryParam(this.predicate, this.ascending),
    };
    this.upazilaService
      .query(queryObject)
      .pipe(map((res: HttpResponse<IUpazila[]>) => (res.body ? res.body.map(item => this.convertUpazilaOption(item)) : [])))
      .pipe(map((upazilas: IUpazila[]) => this.upazilaService.addUpazilaToCollectionIfMissing<IUpazila>(upazilas, this.dealer?.upazila)))
      .subscribe((upazilas: IUpazila[]) => {
        console.log('upazila', upazilas);
        console.log('upazila collection', this.upazilasSharedCollection);
        this.upazilasSharedCollection = upazilas;
        console.log('upazila', upazilas);
        console.log('upazila collection', this.upazilasSharedCollection);
      });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }

  convertUpazilaOption(upazila: IUpazila): IUpazila {
    return {
      id: upazila.id,
      name: upazila.name,
      bnName: upazila.bnName,
    };
  }
}
