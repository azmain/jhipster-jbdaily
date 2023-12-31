import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPayOrder } from '../pay-order.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pay-order.test-samples';

import { PayOrderService, RestPayOrder } from './pay-order.service';

const requireRestSample: RestPayOrder = {
  ...sampleWithRequiredData,
  payOrderDate: sampleWithRequiredData.payOrderDate?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('PayOrder Service', () => {
  let service: PayOrderService;
  let httpMock: HttpTestingController;
  let expectedResult: IPayOrder | IPayOrder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PayOrderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PayOrder', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const payOrder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(payOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PayOrder', () => {
      const payOrder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(payOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PayOrder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PayOrder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PayOrder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPayOrderToCollectionIfMissing', () => {
      it('should add a PayOrder to an empty array', () => {
        const payOrder: IPayOrder = sampleWithRequiredData;
        expectedResult = service.addPayOrderToCollectionIfMissing([], payOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(payOrder);
      });

      it('should not add a PayOrder to an array that contains it', () => {
        const payOrder: IPayOrder = sampleWithRequiredData;
        const payOrderCollection: IPayOrder[] = [
          {
            ...payOrder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPayOrderToCollectionIfMissing(payOrderCollection, payOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PayOrder to an array that doesn't contain it", () => {
        const payOrder: IPayOrder = sampleWithRequiredData;
        const payOrderCollection: IPayOrder[] = [sampleWithPartialData];
        expectedResult = service.addPayOrderToCollectionIfMissing(payOrderCollection, payOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(payOrder);
      });

      it('should add only unique PayOrder to an array', () => {
        const payOrderArray: IPayOrder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const payOrderCollection: IPayOrder[] = [sampleWithRequiredData];
        expectedResult = service.addPayOrderToCollectionIfMissing(payOrderCollection, ...payOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const payOrder: IPayOrder = sampleWithRequiredData;
        const payOrder2: IPayOrder = sampleWithPartialData;
        expectedResult = service.addPayOrderToCollectionIfMissing([], payOrder, payOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(payOrder);
        expect(expectedResult).toContain(payOrder2);
      });

      it('should accept null and undefined values', () => {
        const payOrder: IPayOrder = sampleWithRequiredData;
        expectedResult = service.addPayOrderToCollectionIfMissing([], null, payOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(payOrder);
      });

      it('should return initial array if no PayOrder is added', () => {
        const payOrderCollection: IPayOrder[] = [sampleWithRequiredData];
        expectedResult = service.addPayOrderToCollectionIfMissing(payOrderCollection, undefined, null);
        expect(expectedResult).toEqual(payOrderCollection);
      });
    });

    describe('comparePayOrder', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePayOrder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePayOrder(entity1, entity2);
        const compareResult2 = service.comparePayOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePayOrder(entity1, entity2);
        const compareResult2 = service.comparePayOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePayOrder(entity1, entity2);
        const compareResult2 = service.comparePayOrder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
