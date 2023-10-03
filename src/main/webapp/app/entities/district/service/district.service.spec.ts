import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDistrict } from '../district.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../district.test-samples';

import { DistrictService } from './district.service';

const requireRestSample: IDistrict = {
  ...sampleWithRequiredData,
};

describe('District Service', () => {
  let service: DistrictService;
  let httpMock: HttpTestingController;
  let expectedResult: IDistrict | IDistrict[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DistrictService);
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

    it('should create a District', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const district = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(district).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a District', () => {
      const district = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(district).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a District', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of District', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a District', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDistrictToCollectionIfMissing', () => {
      it('should add a District to an empty array', () => {
        const district: IDistrict = sampleWithRequiredData;
        expectedResult = service.addDistrictToCollectionIfMissing([], district);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(district);
      });

      it('should not add a District to an array that contains it', () => {
        const district: IDistrict = sampleWithRequiredData;
        const districtCollection: IDistrict[] = [
          {
            ...district,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDistrictToCollectionIfMissing(districtCollection, district);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a District to an array that doesn't contain it", () => {
        const district: IDistrict = sampleWithRequiredData;
        const districtCollection: IDistrict[] = [sampleWithPartialData];
        expectedResult = service.addDistrictToCollectionIfMissing(districtCollection, district);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(district);
      });

      it('should add only unique District to an array', () => {
        const districtArray: IDistrict[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const districtCollection: IDistrict[] = [sampleWithRequiredData];
        expectedResult = service.addDistrictToCollectionIfMissing(districtCollection, ...districtArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const district: IDistrict = sampleWithRequiredData;
        const district2: IDistrict = sampleWithPartialData;
        expectedResult = service.addDistrictToCollectionIfMissing([], district, district2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(district);
        expect(expectedResult).toContain(district2);
      });

      it('should accept null and undefined values', () => {
        const district: IDistrict = sampleWithRequiredData;
        expectedResult = service.addDistrictToCollectionIfMissing([], null, district, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(district);
      });

      it('should return initial array if no District is added', () => {
        const districtCollection: IDistrict[] = [sampleWithRequiredData];
        expectedResult = service.addDistrictToCollectionIfMissing(districtCollection, undefined, null);
        expect(expectedResult).toEqual(districtCollection);
      });
    });

    describe('compareDistrict', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDistrict(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDistrict(entity1, entity2);
        const compareResult2 = service.compareDistrict(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDistrict(entity1, entity2);
        const compareResult2 = service.compareDistrict(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDistrict(entity1, entity2);
        const compareResult2 = service.compareDistrict(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
