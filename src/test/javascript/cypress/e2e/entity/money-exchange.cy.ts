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

describe('MoneyExchange e2e test', () => {
  const moneyExchangePageUrl = '/money-exchange';
  const moneyExchangePageUrlPattern = new RegExp('/money-exchange(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const moneyExchangeSample = { name: 'Sleek Universal Principal', digit: 'needs-based overriding Fun' };

  let moneyExchange;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/money-exchanges+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/money-exchanges').as('postEntityRequest');
    cy.intercept('DELETE', '/api/money-exchanges/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (moneyExchange) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/money-exchanges/${moneyExchange.id}`,
      }).then(() => {
        moneyExchange = undefined;
      });
    }
  });

  it('MoneyExchanges menu should load MoneyExchanges page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('money-exchange');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MoneyExchange').should('exist');
    cy.url().should('match', moneyExchangePageUrlPattern);
  });

  describe('MoneyExchange page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(moneyExchangePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MoneyExchange page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/money-exchange/new$'));
        cy.getEntityCreateUpdateHeading('MoneyExchange');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyExchangePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/money-exchanges',
          body: moneyExchangeSample,
        }).then(({ body }) => {
          moneyExchange = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/money-exchanges+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/money-exchanges?page=0&size=20>; rel="last",<http://localhost/api/money-exchanges?page=0&size=20>; rel="first"',
              },
              body: [moneyExchange],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(moneyExchangePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MoneyExchange page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('moneyExchange');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyExchangePageUrlPattern);
      });

      it('edit button click should load edit MoneyExchange page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MoneyExchange');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyExchangePageUrlPattern);
      });

      it('edit button click should load edit MoneyExchange page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MoneyExchange');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyExchangePageUrlPattern);
      });

      it('last delete button click should delete instance of MoneyExchange', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('moneyExchange').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', moneyExchangePageUrlPattern);

        moneyExchange = undefined;
      });
    });
  });

  describe('new MoneyExchange page', () => {
    beforeEach(() => {
      cy.visit(`${moneyExchangePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MoneyExchange');
    });

    it('should create an instance of MoneyExchange', () => {
      cy.get(`[data-cy="name"]`).type('Reverse-engineered up Future').should('have.value', 'Reverse-engineered up Future');

      cy.get(`[data-cy="digit"]`).type('generating Clothing').should('have.value', 'generating Clothing');

      cy.get(`[data-cy="link"]`).type('Avon Wooden e-tailers').should('have.value', 'Avon Wooden e-tailers');

      cy.get(`[data-cy="shortName"]`).type('calculate').should('have.value', 'calculate');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        moneyExchange = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', moneyExchangePageUrlPattern);
    });
  });
});
