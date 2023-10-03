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

describe('Dealer e2e test', () => {
  const dealerPageUrl = '/dealer';
  const dealerPageUrlPattern = new RegExp('/dealer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const dealerSample = { name: 'Chair', bnName: 'haptic magenta Yuan', shortName: 'Business-focused Small' };

  let dealer;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/dealers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/dealers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/dealers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (dealer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/dealers/${dealer.id}`,
      }).then(() => {
        dealer = undefined;
      });
    }
  });

  it('Dealers menu should load Dealers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('dealer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Dealer').should('exist');
    cy.url().should('match', dealerPageUrlPattern);
  });

  describe('Dealer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(dealerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Dealer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/dealer/new$'));
        cy.getEntityCreateUpdateHeading('Dealer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dealerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/dealers',
          body: dealerSample,
        }).then(({ body }) => {
          dealer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/dealers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [dealer],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(dealerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Dealer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('dealer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dealerPageUrlPattern);
      });

      it('edit button click should load edit Dealer page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dealer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dealerPageUrlPattern);
      });

      it('edit button click should load edit Dealer page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Dealer');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dealerPageUrlPattern);
      });

      it('last delete button click should delete instance of Dealer', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('dealer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', dealerPageUrlPattern);

        dealer = undefined;
      });
    });
  });

  describe('new Dealer page', () => {
    beforeEach(() => {
      cy.visit(`${dealerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Dealer');
    });

    it('should create an instance of Dealer', () => {
      cy.get(`[data-cy="name"]`).type('calculating Rubber Mobility').should('have.value', 'calculating Rubber Mobility');

      cy.get(`[data-cy="bnName"]`).type('Robust Dakota Robust').should('have.value', 'Robust Dakota Robust');

      cy.get(`[data-cy="shortName"]`).type('Table Global').should('have.value', 'Table Global');

      cy.get(`[data-cy="mobile"]`).type('Yen Won Intelligent').should('have.value', 'Yen Won Intelligent');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        dealer = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', dealerPageUrlPattern);
    });
  });
});
