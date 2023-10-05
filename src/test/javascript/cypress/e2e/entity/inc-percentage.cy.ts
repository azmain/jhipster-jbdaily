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

describe('IncPercentage e2e test', () => {
  const incPercentagePageUrl = '/inc-percentage';
  const incPercentagePageUrlPattern = new RegExp('/inc-percentage(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const incPercentageSample = { name: 'program RAM California' };

  let incPercentage;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/inc-percentages+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inc-percentages').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inc-percentages/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (incPercentage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inc-percentages/${incPercentage.id}`,
      }).then(() => {
        incPercentage = undefined;
      });
    }
  });

  it('IncPercentages menu should load IncPercentages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inc-percentage');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IncPercentage').should('exist');
    cy.url().should('match', incPercentagePageUrlPattern);
  });

  describe('IncPercentage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(incPercentagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IncPercentage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/inc-percentage/new$'));
        cy.getEntityCreateUpdateHeading('IncPercentage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', incPercentagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inc-percentages',
          body: incPercentageSample,
        }).then(({ body }) => {
          incPercentage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inc-percentages+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/inc-percentages?page=0&size=20>; rel="last",<http://localhost/api/inc-percentages?page=0&size=20>; rel="first"',
              },
              body: [incPercentage],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(incPercentagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IncPercentage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('incPercentage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', incPercentagePageUrlPattern);
      });

      it('edit button click should load edit IncPercentage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IncPercentage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', incPercentagePageUrlPattern);
      });

      it('edit button click should load edit IncPercentage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IncPercentage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', incPercentagePageUrlPattern);
      });

      it('last delete button click should delete instance of IncPercentage', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('incPercentage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', incPercentagePageUrlPattern);

        incPercentage = undefined;
      });
    });
  });

  describe('new IncPercentage page', () => {
    beforeEach(() => {
      cy.visit(`${incPercentagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IncPercentage');
    });

    it('should create an instance of IncPercentage', () => {
      cy.get(`[data-cy="name"]`).type('back-end').should('have.value', 'back-end');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        incPercentage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', incPercentagePageUrlPattern);
    });
  });
});
