import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFertilizer } from '../fertilizer.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../fertilizer.test-samples';

import { FertilizerService } from './fertilizer.service';

const requireRestSample: IFertilizer = {
  ...sampleWithRequiredData,
};

describe('Fertilizer Service', () => {
  let service: FertilizerService;
  let httpMock: HttpTestingController;
  let expectedResult: IFertilizer | IFertilizer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FertilizerService);
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

    it('should create a Fertilizer', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const fertilizer = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(fertilizer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Fertilizer', () => {
      const fertilizer = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(fertilizer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Fertilizer', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Fertilizer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Fertilizer', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFertilizerToCollectionIfMissing', () => {
      it('should add a Fertilizer to an empty array', () => {
        const fertilizer: IFertilizer = sampleWithRequiredData;
        expectedResult = service.addFertilizerToCollectionIfMissing([], fertilizer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fertilizer);
      });

      it('should not add a Fertilizer to an array that contains it', () => {
        const fertilizer: IFertilizer = sampleWithRequiredData;
        const fertilizerCollection: IFertilizer[] = [
          {
            ...fertilizer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFertilizerToCollectionIfMissing(fertilizerCollection, fertilizer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Fertilizer to an array that doesn't contain it", () => {
        const fertilizer: IFertilizer = sampleWithRequiredData;
        const fertilizerCollection: IFertilizer[] = [sampleWithPartialData];
        expectedResult = service.addFertilizerToCollectionIfMissing(fertilizerCollection, fertilizer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fertilizer);
      });

      it('should add only unique Fertilizer to an array', () => {
        const fertilizerArray: IFertilizer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fertilizerCollection: IFertilizer[] = [sampleWithRequiredData];
        expectedResult = service.addFertilizerToCollectionIfMissing(fertilizerCollection, ...fertilizerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const fertilizer: IFertilizer = sampleWithRequiredData;
        const fertilizer2: IFertilizer = sampleWithPartialData;
        expectedResult = service.addFertilizerToCollectionIfMissing([], fertilizer, fertilizer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(fertilizer);
        expect(expectedResult).toContain(fertilizer2);
      });

      it('should accept null and undefined values', () => {
        const fertilizer: IFertilizer = sampleWithRequiredData;
        expectedResult = service.addFertilizerToCollectionIfMissing([], null, fertilizer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(fertilizer);
      });

      it('should return initial array if no Fertilizer is added', () => {
        const fertilizerCollection: IFertilizer[] = [sampleWithRequiredData];
        expectedResult = service.addFertilizerToCollectionIfMissing(fertilizerCollection, undefined, null);
        expect(expectedResult).toEqual(fertilizerCollection);
      });
    });

    describe('compareFertilizer', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFertilizer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFertilizer(entity1, entity2);
        const compareResult2 = service.compareFertilizer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFertilizer(entity1, entity2);
        const compareResult2 = service.compareFertilizer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFertilizer(entity1, entity2);
        const compareResult2 = service.compareFertilizer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
