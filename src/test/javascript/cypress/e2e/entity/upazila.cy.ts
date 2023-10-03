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

describe('Upazila e2e test', () => {
  const upazilaPageUrl = '/upazila';
  const upazilaPageUrlPattern = new RegExp('/upazila(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const upazilaSample = { name: 'invoice Keyboard Dynamic', bnName: 'Rubber innovate' };

  let upazila;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/upazilas+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/upazilas').as('postEntityRequest');
    cy.intercept('DELETE', '/api/upazilas/*').as('deleteEntityRequest');
  });

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

  it('Upazilas menu should load Upazilas page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('upazila');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Upazila').should('exist');
    cy.url().should('match', upazilaPageUrlPattern);
  });

  describe('Upazila page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(upazilaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Upazila page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/upazila/new$'));
        cy.getEntityCreateUpdateHeading('Upazila');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', upazilaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/upazilas',
          body: upazilaSample,
        }).then(({ body }) => {
          upazila = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/upazilas+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [upazila],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(upazilaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Upazila page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('upazila');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', upazilaPageUrlPattern);
      });

      it('edit button click should load edit Upazila page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Upazila');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', upazilaPageUrlPattern);
      });

      it('edit button click should load edit Upazila page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Upazila');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', upazilaPageUrlPattern);
      });

      it('last delete button click should delete instance of Upazila', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('upazila').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', upazilaPageUrlPattern);

        upazila = undefined;
      });
    });
  });

  describe('new Upazila page', () => {
    beforeEach(() => {
      cy.visit(`${upazilaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Upazila');
    });

    it('should create an instance of Upazila', () => {
      cy.get(`[data-cy="name"]`).type('Account').should('have.value', 'Account');

      cy.get(`[data-cy="bnName"]`).type('Bolivia parsing').should('have.value', 'Bolivia parsing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        upazila = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', upazilaPageUrlPattern);
    });
  });
});
