import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMoneyExchange } from '../money-exchange.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../money-exchange.test-samples';

import { MoneyExchangeService, RestMoneyExchange } from './money-exchange.service';

const requireRestSample: RestMoneyExchange = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('MoneyExchange Service', () => {
  let service: MoneyExchangeService;
  let httpMock: HttpTestingController;
  let expectedResult: IMoneyExchange | IMoneyExchange[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MoneyExchangeService);
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

    it('should create a MoneyExchange', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const moneyExchange = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(moneyExchange).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MoneyExchange', () => {
      const moneyExchange = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(moneyExchange).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MoneyExchange', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MoneyExchange', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MoneyExchange', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMoneyExchangeToCollectionIfMissing', () => {
      it('should add a MoneyExchange to an empty array', () => {
        const moneyExchange: IMoneyExchange = sampleWithRequiredData;
        expectedResult = service.addMoneyExchangeToCollectionIfMissing([], moneyExchange);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyExchange);
      });

      it('should not add a MoneyExchange to an array that contains it', () => {
        const moneyExchange: IMoneyExchange = sampleWithRequiredData;
        const moneyExchangeCollection: IMoneyExchange[] = [
          {
            ...moneyExchange,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMoneyExchangeToCollectionIfMissing(moneyExchangeCollection, moneyExchange);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MoneyExchange to an array that doesn't contain it", () => {
        const moneyExchange: IMoneyExchange = sampleWithRequiredData;
        const moneyExchangeCollection: IMoneyExchange[] = [sampleWithPartialData];
        expectedResult = service.addMoneyExchangeToCollectionIfMissing(moneyExchangeCollection, moneyExchange);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyExchange);
      });

      it('should add only unique MoneyExchange to an array', () => {
        const moneyExchangeArray: IMoneyExchange[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const moneyExchangeCollection: IMoneyExchange[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyExchangeToCollectionIfMissing(moneyExchangeCollection, ...moneyExchangeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const moneyExchange: IMoneyExchange = sampleWithRequiredData;
        const moneyExchange2: IMoneyExchange = sampleWithPartialData;
        expectedResult = service.addMoneyExchangeToCollectionIfMissing([], moneyExchange, moneyExchange2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(moneyExchange);
        expect(expectedResult).toContain(moneyExchange2);
      });

      it('should accept null and undefined values', () => {
        const moneyExchange: IMoneyExchange = sampleWithRequiredData;
        expectedResult = service.addMoneyExchangeToCollectionIfMissing([], null, moneyExchange, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(moneyExchange);
      });

      it('should return initial array if no MoneyExchange is added', () => {
        const moneyExchangeCollection: IMoneyExchange[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyExchangeToCollectionIfMissing(moneyExchangeCollection, undefined, null);
        expect(expectedResult).toEqual(moneyExchangeCollection);
      });
    });

    describe('compareMoneyExchange', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMoneyExchange(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMoneyExchange(entity1, entity2);
        const compareResult2 = service.compareMoneyExchange(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMoneyExchange(entity1, entity2);
        const compareResult2 = service.compareMoneyExchange(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMoneyExchange(entity1, entity2);
        const compareResult2 = service.compareMoneyExchange(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
