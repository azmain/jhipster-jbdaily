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

describe('District e2e test', () => {
  const districtPageUrl = '/district';
  const districtPageUrlPattern = new RegExp('/district(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const districtSample = {"name":"withdrawal payment","bnName":"Concrete (Slovak","createdBy":"Rubber Web","createdDate":"2023-10-02T08:15:16.959Z"};

  let district;
  // let division;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/divisions',
      body: {"name":"executive deposit","bnName":"Total Grocery","createdBy":"models AGP Card","createdDate":"2023-10-02T07:00:22.341Z","lastModifiedBy":"program Loan Cliffs","lastModifiedDate":"2023-10-01T23:49:02.149Z"},
    }).then(({ body }) => {
      division = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/districts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/districts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/districts/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/upazilas', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/divisions', {
      statusCode: 200,
      body: [division],
    });

  });
   */

  afterEach(() => {
    if (district) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/districts/${district.id}`,
      }).then(() => {
        district = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('Districts menu should load Districts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('district');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('District').should('exist');
    cy.url().should('match', districtPageUrlPattern);
  });

  describe('District page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(districtPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create District page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/district/new$'));
        cy.getEntityCreateUpdateHeading('District');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', districtPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/districts',
          body: {
            ...districtSample,
            division: division,
          },
        }).then(({ body }) => {
          district = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/districts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/districts?page=0&size=20>; rel="last",<http://localhost/api/districts?page=0&size=20>; rel="first"',
              },
              body: [district],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(districtPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(districtPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details District page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('district');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', districtPageUrlPattern);
      });

      it('edit button click should load edit District page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('District');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', districtPageUrlPattern);
      });

      it('edit button click should load edit District page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('District');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', districtPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of District', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('district').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', districtPageUrlPattern);

        district = undefined;
      });
    });
  });

  describe('new District page', () => {
    beforeEach(() => {
      cy.visit(`${districtPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('District');
    });

    it.skip('should create an instance of District', () => {
      cy.get(`[data-cy="name"]`).type('Extensions Tokelau').should('have.value', 'Extensions Tokelau');

      cy.get(`[data-cy="bnName"]`).type('deposit Senior').should('have.value', 'deposit Senior');

      cy.get(`[data-cy="createdBy"]`).type('Way bandwidth Burg').should('have.value', 'Way bandwidth Burg');

      cy.get(`[data-cy="createdDate"]`).type('2023-10-02T13:54').blur().should('have.value', '2023-10-02T13:54');

      cy.get(`[data-cy="lastModifiedBy"]`).type('Indiana').should('have.value', 'Indiana');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2023-10-02T16:59').blur().should('have.value', '2023-10-02T16:59');

      cy.get(`[data-cy="division"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        district = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', districtPageUrlPattern);
    });
  });
});
