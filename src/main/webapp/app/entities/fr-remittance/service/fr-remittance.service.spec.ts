import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFrRemittance } from '../fr-remittance.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../fr-remittance.test-samples';

import { FrRemittanceService, RestFrRemittance } from './fr-remittance.service';

const requireRestSample: RestFrRemittance = {
  ...sampleWithRequiredData,
  paymentDate: sampleWithRequiredData.paymentDate?.format(DATE_FORMAT),
  incPaymentDate: sampleWithRequiredData.incPaymentDate?.format(DATE_FORMAT),
  remiSendingDate: sampleWithRequiredData.remiSendingDate?.format(DATE_FORMAT),
  amountReimDate: sampleWithRequiredData.amountReimDate?.format(DATE_FORMAT),
  incAmountReimDate: sampleWithRequiredData.incAmountReimDate?.format(DATE_FORMAT),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('FrRemittance Service', () => {
  let service: FrRemittanceService;
  let httpMock: HttpTestingController;
  let expectedResult: IFrRemittance | IFrRemittance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FrRemittanceService);
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

    it('should create a FrRemittance', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const frRemittance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(frRemittance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FrRemittance', () => {
      const frRemittance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(frRemittance).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FrRemittance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FrRemittance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FrRemittance', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFrRemittanceToCollectionIfMissing', () => {
      it('should add a FrRemittance to an empty array', () => {
        const frRemittance: IFrRemittance = sampleWithRequiredData;
        expectedResult = service.addFrRemittanceToCollectionIfMissing([], frRemittance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(frRemittance);
      });

      it('should not add a FrRemittance to an array that contains it', () => {
        const frRemittance: IFrRemittance = sampleWithRequiredData;
        const frRemittanceCollection: IFrRemittance[] = [
          {
            ...frRemittance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFrRemittanceToCollectionIfMissing(frRemittanceCollection, frRemittance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FrRemittance to an array that doesn't contain it", () => {
        const frRemittance: IFrRemittance = sampleWithRequiredData;
        const frRemittanceCollection: IFrRemittance[] = [sampleWithPartialData];
        expectedResult = service.addFrRemittanceToCollectionIfMissing(frRemittanceCollection, frRemittance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(frRemittance);
      });

      it('should add only unique FrRemittance to an array', () => {
        const frRemittanceArray: IFrRemittance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const frRemittanceCollection: IFrRemittance[] = [sampleWithRequiredData];
        expectedResult = service.addFrRemittanceToCollectionIfMissing(frRemittanceCollection, ...frRemittanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const frRemittance: IFrRemittance = sampleWithRequiredData;
        const frRemittance2: IFrRemittance = sampleWithPartialData;
        expectedResult = service.addFrRemittanceToCollectionIfMissing([], frRemittance, frRemittance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(frRemittance);
        expect(expectedResult).toContain(frRemittance2);
      });

      it('should accept null and undefined values', () => {
        const frRemittance: IFrRemittance = sampleWithRequiredData;
        expectedResult = service.addFrRemittanceToCollectionIfMissing([], null, frRemittance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(frRemittance);
      });

      it('should return initial array if no FrRemittance is added', () => {
        const frRemittanceCollection: IFrRemittance[] = [sampleWithRequiredData];
        expectedResult = service.addFrRemittanceToCollectionIfMissing(frRemittanceCollection, undefined, null);
        expect(expectedResult).toEqual(frRemittanceCollection);
      });
    });

    describe('compareFrRemittance', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFrRemittance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFrRemittance(entity1, entity2);
        const compareResult2 = service.compareFrRemittance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFrRemittance(entity1, entity2);
        const compareResult2 = service.compareFrRemittance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFrRemittance(entity1, entity2);
        const compareResult2 = service.compareFrRemittance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
