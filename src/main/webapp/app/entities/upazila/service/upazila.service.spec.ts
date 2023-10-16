import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUpazila } from '../upazila.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../upazila.test-samples';

import { UpazilaService, RestUpazila } from './upazila.service';

const requireRestSample: RestUpazila = {
  ...sampleWithRequiredData,
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('Upazila Service', () => {
  let service: UpazilaService;
  let httpMock: HttpTestingController;
  let expectedResult: IUpazila | IUpazila[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UpazilaService);
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

    it('should create a Upazila', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const upazila = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(upazila).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Upazila', () => {
      const upazila = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(upazila).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Upazila', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Upazila', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Upazila', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUpazilaToCollectionIfMissing', () => {
      it('should add a Upazila to an empty array', () => {
        const upazila: IUpazila = sampleWithRequiredData;
        expectedResult = service.addUpazilaToCollectionIfMissing([], upazila);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(upazila);
      });

      it('should not add a Upazila to an array that contains it', () => {
        const upazila: IUpazila = sampleWithRequiredData;
        const upazilaCollection: IUpazila[] = [
          {
            ...upazila,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUpazilaToCollectionIfMissing(upazilaCollection, upazila);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Upazila to an array that doesn't contain it", () => {
        const upazila: IUpazila = sampleWithRequiredData;
        const upazilaCollection: IUpazila[] = [sampleWithPartialData];
        expectedResult = service.addUpazilaToCollectionIfMissing(upazilaCollection, upazila);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(upazila);
      });

      it('should add only unique Upazila to an array', () => {
        const upazilaArray: IUpazila[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const upazilaCollection: IUpazila[] = [sampleWithRequiredData];
        expectedResult = service.addUpazilaToCollectionIfMissing(upazilaCollection, ...upazilaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const upazila: IUpazila = sampleWithRequiredData;
        const upazila2: IUpazila = sampleWithPartialData;
        expectedResult = service.addUpazilaToCollectionIfMissing([], upazila, upazila2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(upazila);
        expect(expectedResult).toContain(upazila2);
      });

      it('should accept null and undefined values', () => {
        const upazila: IUpazila = sampleWithRequiredData;
        expectedResult = service.addUpazilaToCollectionIfMissing([], null, upazila, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(upazila);
      });

      it('should return initial array if no Upazila is added', () => {
        const upazilaCollection: IUpazila[] = [sampleWithRequiredData];
        expectedResult = service.addUpazilaToCollectionIfMissing(upazilaCollection, undefined, null);
        expect(expectedResult).toEqual(upazilaCollection);
      });
    });

    describe('compareUpazila', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUpazila(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUpazila(entity1, entity2);
        const compareResult2 = service.compareUpazila(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUpazila(entity1, entity2);
        const compareResult2 = service.compareUpazila(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUpazila(entity1, entity2);
        const compareResult2 = service.compareUpazila(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
