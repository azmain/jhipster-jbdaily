import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FertilizerService } from '../service/fertilizer.service';

import { FertilizerComponent } from './fertilizer.component';

describe('Fertilizer Management Component', () => {
  let comp: FertilizerComponent;
  let fixture: ComponentFixture<FertilizerComponent>;
  let service: FertilizerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'fertilizer', component: FertilizerComponent }]), HttpClientTestingModule],
      declarations: [FertilizerComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(FertilizerComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FertilizerComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(FertilizerService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.fertilizers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to fertilizerService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getFertilizerIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getFertilizerIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
