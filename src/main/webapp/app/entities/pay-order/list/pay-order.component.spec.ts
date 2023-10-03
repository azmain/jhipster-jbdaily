import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PayOrderService } from '../service/pay-order.service';

import { PayOrderComponent } from './pay-order.component';

describe('PayOrder Management Component', () => {
  let comp: PayOrderComponent;
  let fixture: ComponentFixture<PayOrderComponent>;
  let service: PayOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'pay-order', component: PayOrderComponent }]), HttpClientTestingModule],
      declarations: [PayOrderComponent],
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
      .overrideTemplate(PayOrderComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PayOrderComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PayOrderService);

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
    expect(comp.payOrders?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to payOrderService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getPayOrderIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getPayOrderIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
