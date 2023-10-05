import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('FrRemittance e2e test', () => {
  const frRemittancePageUrl = '/fr-remittance';
  const frRemittancePageUrlPattern = new RegExp('/fr-remittance(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const frRemittanceSample = {
    pin: 'Cambridgeshire',
    remitersName: 'connecting matrix fault-tolerant',
    amount: 'Mississippi encryption',
    incentiveAmount: 'bypass Tasty',
    paymentDate: '2023-10-04',
    incPaymentDate: '2023-10-04',
    transactionType: 'ACCOUNT',
    recvMobileNo: 'Illinois Usability',
    recvName: 'analyzing white pink',
    recvLegalId: 'strategy',
    recvGender: 'OTHERS',
    remiGender: 'MALE',
    documentType: 'PASSPORT',
  };

  let frRemittance;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fr-remittances+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fr-remittances').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fr-remittances/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (frRemittance) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fr-remittances/${frRemittance.id}`,
      }).then(() => {
        frRemittance = undefined;
      });
    }
  });

  it('FrRemittances menu should load FrRemittances page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fr-remittance');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FrRemittance').should('exist');
    cy.url().should('match', frRemittancePageUrlPattern);
  });

  describe('FrRemittance page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(frRemittancePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FrRemittance page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/fr-remittance/new$'));
        cy.getEntityCreateUpdateHeading('FrRemittance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', frRemittancePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fr-remittances',
          body: frRemittanceSample,
        }).then(({ body }) => {
          frRemittance = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fr-remittances+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/fr-remittances?page=0&size=20>; rel="last",<http://localhost/api/fr-remittances?page=0&size=20>; rel="first"',
              },
              body: [frRemittance],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(frRemittancePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FrRemittance page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('frRemittance');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', frRemittancePageUrlPattern);
      });

      it('edit button click should load edit FrRemittance page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FrRemittance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', frRemittancePageUrlPattern);
      });

      it.skip('edit button click should load edit FrRemittance page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FrRemittance');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', frRemittancePageUrlPattern);
      });

      it('last delete button click should delete instance of FrRemittance', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('frRemittance').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', frRemittancePageUrlPattern);

        frRemittance = undefined;
      });
    });
  });

  describe('new FrRemittance page', () => {
    beforeEach(() => {
      cy.visit(`${frRemittancePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FrRemittance');
    });

    it('should create an instance of FrRemittance', () => {
      cy.get(`[data-cy="pin"]`).type('Loan Strategist Dollar').should('have.value', 'Loan Strategist Dollar');

      cy.get(`[data-cy="remitersName"]`).type('Credit SDD').should('have.value', 'Credit SDD');

      cy.get(`[data-cy="amount"]`).type('Borders Jersey Handmade').should('have.value', 'Borders Jersey Handmade');

      cy.get(`[data-cy="incentiveAmount"]`).type('Distributed disintermediate').should('have.value', 'Distributed disintermediate');

      cy.get(`[data-cy="paymentDate"]`).type('2023-10-03').blur().should('have.value', '2023-10-03');

      cy.get(`[data-cy="incPaymentDate"]`).type('2023-10-04').blur().should('have.value', '2023-10-04');

      cy.get(`[data-cy="remiSendingDate"]`).type('2023-10-04').blur().should('have.value', '2023-10-04');

      cy.get(`[data-cy="remiFrCurrency"]`).type('RSS').should('have.value', 'RSS');

      cy.get(`[data-cy="currency"]`).type('conglomeration leverage').should('have.value', 'conglomeration leverage');

      cy.get(`[data-cy="country"]`).type('Oman').should('have.value', 'Oman');

      cy.get(`[data-cy="exchangeRate"]`).type('Towels Investment').should('have.value', 'Towels Investment');

      cy.get(`[data-cy="transactionType"]`).select('SPOT');

      cy.get(`[data-cy="recvMobileNo"]`).type('Principal Baby Fresh').should('have.value', 'Principal Baby Fresh');

      cy.get(`[data-cy="recvName"]`).type('Metal').should('have.value', 'Metal');

      cy.get(`[data-cy="recvLegalId"]`).type('Loan Sports e-business').should('have.value', 'Loan Sports e-business');

      cy.get(`[data-cy="moneyExchangeName"]`).type('enterprise maroon Handcrafted').should('have.value', 'enterprise maroon Handcrafted');

      cy.get(`[data-cy="amountReimDate"]`).type('2023-10-04').blur().should('have.value', '2023-10-04');

      cy.get(`[data-cy="incAmountReimDate"]`).type('2023-10-04').blur().should('have.value', '2023-10-04');

      cy.get(`[data-cy="recvGender"]`).select('FEMALE');

      cy.get(`[data-cy="remiGender"]`).select('OTHERS');

      cy.get(`[data-cy="documentType"]`).select('PASSPORT');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        frRemittance = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', frRemittancePageUrlPattern);
    });
  });
});
