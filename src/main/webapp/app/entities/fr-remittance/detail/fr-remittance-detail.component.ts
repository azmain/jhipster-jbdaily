import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFrRemittance } from '../fr-remittance.model';

@Component({
  selector: 'jhi-fr-remittance-detail',
  templateUrl: './fr-remittance-detail.component.html',
})
export class FrRemittanceDetailComponent implements OnInit {
  frRemittance: IFrRemittance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ frRemittance }) => {
      this.frRemittance = frRemittance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
