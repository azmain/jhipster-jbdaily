import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIncPercentage } from '../inc-percentage.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../inc-percentage.test-samples';

import { IncPercentageService } from './inc-percentage.service';

const requireRestSample: IIncPercentage = {
  ...sampleWithRequiredData,
};

describe('IncPercentage Service', () => {
  let service: IncPercentageService;
  let httpMock: HttpTestingController;
  let expectedResult: IIncPercentage | IIncPercentage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IncPercentageService);
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

    it('should create a IncPercentage', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const incPercentage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(incPercentage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a IncPercentage', () => {
      const incPercentage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(incPercentage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a IncPercentage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of IncPercentage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a IncPercentage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIncPercentageToCollectionIfMissing', () => {
      it('should add a IncPercentage to an empty array', () => {
        const incPercentage: IIncPercentage = sampleWithRequiredData;
        expectedResult = service.addIncPercentageToCollectionIfMissing([], incPercentage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incPercentage);
      });

      it('should not add a IncPercentage to an array that contains it', () => {
        const incPercentage: IIncPercentage = sampleWithRequiredData;
        const incPercentageCollection: IIncPercentage[] = [
          {
            ...incPercentage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIncPercentageToCollectionIfMissing(incPercentageCollection, incPercentage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a IncPercentage to an array that doesn't contain it", () => {
        const incPercentage: IIncPercentage = sampleWithRequiredData;
        const incPercentageCollection: IIncPercentage[] = [sampleWithPartialData];
        expectedResult = service.addIncPercentageToCollectionIfMissing(incPercentageCollection, incPercentage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incPercentage);
      });

      it('should add only unique IncPercentage to an array', () => {
        const incPercentageArray: IIncPercentage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const incPercentageCollection: IIncPercentage[] = [sampleWithRequiredData];
        expectedResult = service.addIncPercentageToCollectionIfMissing(incPercentageCollection, ...incPercentageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const incPercentage: IIncPercentage = sampleWithRequiredData;
        const incPercentage2: IIncPercentage = sampleWithPartialData;
        expectedResult = service.addIncPercentageToCollectionIfMissing([], incPercentage, incPercentage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(incPercentage);
        expect(expectedResult).toContain(incPercentage2);
      });

      it('should accept null and undefined values', () => {
        const incPercentage: IIncPercentage = sampleWithRequiredData;
        expectedResult = service.addIncPercentageToCollectionIfMissing([], null, incPercentage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(incPercentage);
      });

      it('should return initial array if no IncPercentage is added', () => {
        const incPercentageCollection: IIncPercentage[] = [sampleWithRequiredData];
        expectedResult = service.addIncPercentageToCollectionIfMissing(incPercentageCollection, undefined, null);
        expect(expectedResult).toEqual(incPercentageCollection);
      });
    });

    describe('compareIncPercentage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIncPercentage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIncPercentage(entity1, entity2);
        const compareResult2 = service.compareIncPercentage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIncPercentage(entity1, entity2);
        const compareResult2 = service.compareIncPercentage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIncPercentage(entity1, entity2);
        const compareResult2 = service.compareIncPercentage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
