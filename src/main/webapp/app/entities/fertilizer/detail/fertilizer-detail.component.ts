import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFertilizer } from '../fertilizer.model';

@Component({
  selector: 'jhi-fertilizer-detail',
  templateUrl: './fertilizer-detail.component.html',
})
export class FertilizerDetailComponent implements OnInit {
  fertilizer: IFertilizer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fertilizer }) => {
      this.fertilizer = fertilizer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
