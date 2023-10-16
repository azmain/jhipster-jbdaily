import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../fr-remittance.test-samples';

import { FrRemittanceFormService } from './fr-remittance-form.service';

describe('FrRemittance Form Service', () => {
  let service: FrRemittanceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FrRemittanceFormService);
  });

  describe('Service methods', () => {
    describe('createFrRemittanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFrRemittanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pin: expect.any(Object),
            remitersName: expect.any(Object),
            amount: expect.any(Object),
            incentiveAmount: expect.any(Object),
            paymentDate: expect.any(Object),
            incPaymentDate: expect.any(Object),
            remiSendingDate: expect.any(Object),
            remiFrCurrency: expect.any(Object),
            currency: expect.any(Object),
            country: expect.any(Object),
            exchangeRate: expect.any(Object),
            transactionType: expect.any(Object),
            recvMobileNo: expect.any(Object),
            recvName: expect.any(Object),
            recvLegalId: expect.any(Object),
            moneyExchangeName: expect.any(Object),
            amountReimDate: expect.any(Object),
            incAmountReimDate: expect.any(Object),
            recvGender: expect.any(Object),
            remiGender: expect.any(Object),
            documentType: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            moneyExchange: expect.any(Object),
            incPercentage: expect.any(Object),
          })
        );
      });

      it('passing IFrRemittance should create a new form with FormGroup', () => {
        const formGroup = service.createFrRemittanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pin: expect.any(Object),
            remitersName: expect.any(Object),
            amount: expect.any(Object),
            incentiveAmount: expect.any(Object),
            paymentDate: expect.any(Object),
            incPaymentDate: expect.any(Object),
            remiSendingDate: expect.any(Object),
            remiFrCurrency: expect.any(Object),
            currency: expect.any(Object),
            country: expect.any(Object),
            exchangeRate: expect.any(Object),
            transactionType: expect.any(Object),
            recvMobileNo: expect.any(Object),
            recvName: expect.any(Object),
            recvLegalId: expect.any(Object),
            moneyExchangeName: expect.any(Object),
            amountReimDate: expect.any(Object),
            incAmountReimDate: expect.any(Object),
            recvGender: expect.any(Object),
            remiGender: expect.any(Object),
            documentType: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            moneyExchange: expect.any(Object),
            incPercentage: expect.any(Object),
          })
        );
      });
    });

    describe('getFrRemittance', () => {
      it('should return NewFrRemittance for default FrRemittance initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createFrRemittanceFormGroup(sampleWithNewData);

        const frRemittance = service.getFrRemittance(formGroup) as any;

        expect(frRemittance).toMatchObject(sampleWithNewData);
      });

      it('should return NewFrRemittance for empty FrRemittance initial value', () => {
        const formGroup = service.createFrRemittanceFormGroup();

        const frRemittance = service.getFrRemittance(formGroup) as any;

        expect(frRemittance).toMatchObject({});
      });

      it('should return IFrRemittance', () => {
        const formGroup = service.createFrRemittanceFormGroup(sampleWithRequiredData);

        const frRemittance = service.getFrRemittance(formGroup) as any;

        expect(frRemittance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFrRemittance should not enable id FormControl', () => {
        const formGroup = service.createFrRemittanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFrRemittance should disable id FormControl', () => {
        const formGroup = service.createFrRemittanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
