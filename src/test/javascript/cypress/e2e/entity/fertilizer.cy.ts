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

describe('Fertilizer e2e test', () => {
  const fertilizerPageUrl = '/fertilizer';
  const fertilizerPageUrlPattern = new RegExp('/fertilizer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const fertilizerSample = {
    name: 'well-modulated XSS Operative',
    bnName: 'quantifying Exclusive 1080p',
    accountNo: 'generating Cotton',
    accountTitle: 'COM Officer Shoes',
  };

  let fertilizer;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fertilizers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fertilizers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fertilizers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (fertilizer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fertilizers/${fertilizer.id}`,
      }).then(() => {
        fertilizer = undefined;
      });
    }
  });

  it('Fertilizers menu should load Fertilizers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fertilizer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Fertilizer').should('exist');
    cy.url().should('match', fertilizerPageUrlPattern);
  });

  describe('Fertilizer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fertilizerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Fertilizer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/fertilizer/new$'));
        cy.getEntityCreateUpdateHeading('Fertilizer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fertilizerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fertilizers',
          body: fertilizerSample,
        }).then(({ body }) => {
          fertilizer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fertilizers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/fertilizers?page=0&size=20>; rel="last",<http://localhost/api/fertilizers?page=0&size=20>; rel="first"',
              },
              body: [fertilizer],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(fertilizerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Fertilizer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fertilizer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fertilizerPageUrlPattern);
      });

      it('edit button click should load edit Fertilizer page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Fertilizer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fertilizerPageUrlPattern);
      });

      it('edit button click should load edit Fertilizer page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Fertilizer');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fertilizerPageUrlPattern);
      });

      it('last delete button click should delete instance of Fertilizer', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fertilizer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fertilizerPageUrlPattern);

        fertilizer = undefined;
      });
    });
  });

  describe('new Fertilizer page', () => {
    beforeEach(() => {
      cy.visit(`${fertilizerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Fertilizer');
    });

    it('should create an instance of Fertilizer', () => {
      cy.get(`[data-cy="name"]`).type('Licensed User-centric').should('have.value', 'Licensed User-centric');

      cy.get(`[data-cy="bnName"]`).type('Avon Books').should('have.value', 'Avon Books');

      cy.get(`[data-cy="accountNo"]`).type('USB e-commerce Bypass').should('have.value', 'USB e-commerce Bypass');

      cy.get(`[data-cy="accountTitle"]`).type('Stravenue quantify blue').should('have.value', 'Stravenue quantify blue');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        fertilizer = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', fertilizerPageUrlPattern);
    });
  });
});
