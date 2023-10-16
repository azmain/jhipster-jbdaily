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
  // const dealerSample = {"name":"Senior","bnName":"Brazil generate","shortName":"West full-range","createdBy":"Ecuador calculating Internal","createdDate":"2023-10-01T18:53:32.324Z"};

  let dealer;
  // let upazila;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/upazilas',
      body: {"name":"Solutions","bnName":"circuit SAS transmitting","createdBy":"Handcrafted Frozen","createdDate":"2023-10-01T21:01:02.174Z","lastModifiedBy":"transmitting","lastModifiedDate":"2023-10-02T05:55:30.590Z"},
    }).then(({ body }) => {
      upazila = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/dealers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/dealers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/dealers/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/pay-orders', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/upazilas', {
      statusCode: 200,
      body: [upazila],
    });

  });
   */

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

  /* Disabled due to incompatibility
  afterEach(() => {
    if (upazila) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/upazilas/${upazila.id}`,
      }).then(() => {
        upazila = undefined;
      });
    }
  });
   */

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
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/dealers',
          body: {
            ...dealerSample,
            upazila: upazila,
          },
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
              headers: {
                link: '<http://localhost/api/dealers?page=0&size=20>; rel="last",<http://localhost/api/dealers?page=0&size=20>; rel="first"',
              },
              body: [dealer],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(dealerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(dealerPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
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

      it.skip('last delete button click should delete instance of Dealer', () => {
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

    it.skip('should create an instance of Dealer', () => {
      cy.get(`[data-cy="name"]`).type('calculating Rubber Mobility').should('have.value', 'calculating Rubber Mobility');

      cy.get(`[data-cy="bnName"]`).type('Robust Dakota Robust').should('have.value', 'Robust Dakota Robust');

      cy.get(`[data-cy="shortName"]`).type('Table Global').should('have.value', 'Table Global');

      cy.get(`[data-cy="mobile"]`).type('Yen Won Intelligent').should('have.value', 'Yen Won Intelligent');

      cy.get(`[data-cy="createdBy"]`).type('Chair').should('have.value', 'Chair');

      cy.get(`[data-cy="createdDate"]`).type('2023-10-01T23:51').blur().should('have.value', '2023-10-01T23:51');

      cy.get(`[data-cy="lastModifiedBy"]`).type('Berkshire Rupee Table').should('have.value', 'Berkshire Rupee Table');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2023-10-02T12:14').blur().should('have.value', '2023-10-02T12:14');

      cy.get(`[data-cy="upazila"]`).select(1);

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
