import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FrRemittanceDetailComponent } from './fr-remittance-detail.component';

describe('FrRemittance Management Detail Component', () => {
  let comp: FrRemittanceDetailComponent;
  let fixture: ComponentFixture<FrRemittanceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FrRemittanceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ frRemittance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FrRemittanceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FrRemittanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load frRemittance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.frRemittance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
