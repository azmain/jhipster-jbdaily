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

describe('Division e2e test', () => {
  const divisionPageUrl = '/division';
  const divisionPageUrlPattern = new RegExp('/division(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const divisionSample = {
    name: 'viral intuitive connecting',
    bnName: 'Account global blue',
    createdBy: 'Wooden cross-media digital',
    createdDate: '2023-10-01T19:53:46.488Z',
  };

  let division;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/divisions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/divisions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/divisions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (division) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/divisions/${division.id}`,
      }).then(() => {
        division = undefined;
      });
    }
  });

  it('Divisions menu should load Divisions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('division');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Division').should('exist');
    cy.url().should('match', divisionPageUrlPattern);
  });

  describe('Division page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(divisionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Division page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/division/new$'));
        cy.getEntityCreateUpdateHeading('Division');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', divisionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/divisions',
          body: divisionSample,
        }).then(({ body }) => {
          division = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/divisions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/divisions?page=0&size=20>; rel="last",<http://localhost/api/divisions?page=0&size=20>; rel="first"',
              },
              body: [division],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(divisionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Division page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('division');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', divisionPageUrlPattern);
      });

      it('edit button click should load edit Division page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Division');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', divisionPageUrlPattern);
      });

      it('edit button click should load edit Division page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Division');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', divisionPageUrlPattern);
      });

      it('last delete button click should delete instance of Division', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('division').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', divisionPageUrlPattern);

        division = undefined;
      });
    });
  });

  describe('new Division page', () => {
    beforeEach(() => {
      cy.visit(`${divisionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Division');
    });

    it('should create an instance of Division', () => {
      cy.get(`[data-cy="name"]`).type('calculate Tuna Credit').should('have.value', 'calculate Tuna Credit');

      cy.get(`[data-cy="bnName"]`).type('auxiliary Dynamic Isle').should('have.value', 'auxiliary Dynamic Isle');

      cy.get(`[data-cy="createdBy"]`).type('synthesize').should('have.value', 'synthesize');

      cy.get(`[data-cy="createdDate"]`).type('2023-10-02T12:00').blur().should('have.value', '2023-10-02T12:00');

      cy.get(`[data-cy="lastModifiedBy"]`).type('Plain SMTP').should('have.value', 'Plain SMTP');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2023-10-01T22:09').blur().should('have.value', '2023-10-01T22:09');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        division = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', divisionPageUrlPattern);
    });
  });
});
