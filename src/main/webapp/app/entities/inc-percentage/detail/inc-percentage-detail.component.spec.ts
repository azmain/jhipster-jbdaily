import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IncPercentageDetailComponent } from './inc-percentage-detail.component';

describe('IncPercentage Management Detail Component', () => {
  let comp: IncPercentageDetailComponent;
  let fixture: ComponentFixture<IncPercentageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IncPercentageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ incPercentage: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(IncPercentageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(IncPercentageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load incPercentage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.incPercentage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
