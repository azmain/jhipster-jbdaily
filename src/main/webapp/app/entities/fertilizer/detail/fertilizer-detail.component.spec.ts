import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FertilizerDetailComponent } from './fertilizer-detail.component';

describe('Fertilizer Management Detail Component', () => {
  let comp: FertilizerDetailComponent;
  let fixture: ComponentFixture<FertilizerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FertilizerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ fertilizer: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FertilizerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FertilizerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load fertilizer on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.fertilizer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
