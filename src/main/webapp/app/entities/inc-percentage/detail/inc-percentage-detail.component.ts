import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIncPercentage } from '../inc-percentage.model';

@Component({
  selector: 'jhi-inc-percentage-detail',
  templateUrl: './inc-percentage-detail.component.html',
})
export class IncPercentageDetailComponent implements OnInit {
  incPercentage: IIncPercentage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incPercentage }) => {
      this.incPercentage = incPercentage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
