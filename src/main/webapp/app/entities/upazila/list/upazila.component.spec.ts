import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UpazilaService } from '../service/upazila.service';

import { UpazilaComponent } from './upazila.component';

describe('Upazila Management Component', () => {
  let comp: UpazilaComponent;
  let fixture: ComponentFixture<UpazilaComponent>;
  let service: UpazilaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'upazila', component: UpazilaComponent }]), HttpClientTestingModule],
      declarations: [UpazilaComponent],
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
      .overrideTemplate(UpazilaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UpazilaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(UpazilaService);

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
    expect(comp.upazilas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to upazilaService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getUpazilaIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getUpazilaIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
