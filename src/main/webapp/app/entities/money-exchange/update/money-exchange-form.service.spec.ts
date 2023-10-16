import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../money-exchange.test-samples';

import { MoneyExchangeFormService } from './money-exchange-form.service';

describe('MoneyExchange Form Service', () => {
  let service: MoneyExchangeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MoneyExchangeFormService);
  });

  describe('Service methods', () => {
    describe('createMoneyExchangeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMoneyExchangeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            digit: expect.any(Object),
            link: expect.any(Object),
            shortName: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          })
        );
      });

      it('passing IMoneyExchange should create a new form with FormGroup', () => {
        const formGroup = service.createMoneyExchangeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            digit: expect.any(Object),
            link: expect.any(Object),
            shortName: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
          })
        );
      });
    });

    describe('getMoneyExchange', () => {
      it('should return NewMoneyExchange for default MoneyExchange initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMoneyExchangeFormGroup(sampleWithNewData);

        const moneyExchange = service.getMoneyExchange(formGroup) as any;

        expect(moneyExchange).toMatchObject(sampleWithNewData);
      });

      it('should return NewMoneyExchange for empty MoneyExchange initial value', () => {
        const formGroup = service.createMoneyExchangeFormGroup();

        const moneyExchange = service.getMoneyExchange(formGroup) as any;

        expect(moneyExchange).toMatchObject({});
      });

      it('should return IMoneyExchange', () => {
        const formGroup = service.createMoneyExchangeFormGroup(sampleWithRequiredData);

        const moneyExchange = service.getMoneyExchange(formGroup) as any;

        expect(moneyExchange).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMoneyExchange should not enable id FormControl', () => {
        const formGroup = service.createMoneyExchangeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMoneyExchange should disable id FormControl', () => {
        const formGroup = service.createMoneyExchangeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
