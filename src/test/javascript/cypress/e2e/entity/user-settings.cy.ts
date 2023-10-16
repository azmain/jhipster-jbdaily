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

describe('UserSettings e2e test', () => {
  const userSettingsPageUrl = '/user-settings';
  const userSettingsPageUrlPattern = new RegExp('/user-settings(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userSettingsSample = {
    name: 'Fish protocol Generic',
    payOrderNumSeq: 'Group Producer',
    payOrderControlNum: 'National monetize',
    createdBy: 'Salad',
    createdDate: '2023-10-16T05:51:50.801Z',
  };

  let userSettings;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-settings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-settings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-settings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userSettings) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-settings/${userSettings.id}`,
      }).then(() => {
        userSettings = undefined;
      });
    }
  });

  it('UserSettings menu should load UserSettings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-settings');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserSettings').should('exist');
    cy.url().should('match', userSettingsPageUrlPattern);
  });

  describe('UserSettings page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userSettingsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserSettings page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-settings/new$'));
        cy.getEntityCreateUpdateHeading('UserSettings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userSettingsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-settings',
          body: userSettingsSample,
        }).then(({ body }) => {
          userSettings = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-settings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-settings?page=0&size=20>; rel="last",<http://localhost/api/user-settings?page=0&size=20>; rel="first"',
              },
              body: [userSettings],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(userSettingsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserSettings page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userSettings');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userSettingsPageUrlPattern);
      });

      it('edit button click should load edit UserSettings page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserSettings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userSettingsPageUrlPattern);
      });

      it('edit button click should load edit UserSettings page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserSettings');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userSettingsPageUrlPattern);
      });

      it('last delete button click should delete instance of UserSettings', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('userSettings').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userSettingsPageUrlPattern);

        userSettings = undefined;
      });
    });
  });

  describe('new UserSettings page', () => {
    beforeEach(() => {
      cy.visit(`${userSettingsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserSettings');
    });

    it('should create an instance of UserSettings', () => {
      cy.get(`[data-cy="name"]`).type('definition').should('have.value', 'definition');

      cy.get(`[data-cy="payOrderNumSeq"]`).type('Universal').should('have.value', 'Universal');

      cy.get(`[data-cy="payOrderControlNum"]`).type('payment purple').should('have.value', 'payment purple');

      cy.get(`[data-cy="createdBy"]`).type('transmit Incredible Ergonomic').should('have.value', 'transmit Incredible Ergonomic');

      cy.get(`[data-cy="createdDate"]`).type('2023-10-16T05:44').blur().should('have.value', '2023-10-16T05:44');

      cy.get(`[data-cy="lastModifiedBy"]`).type('indexing Cotton').should('have.value', 'indexing Cotton');

      cy.get(`[data-cy="lastModifiedDate"]`).type('2023-10-15T22:08').blur().should('have.value', '2023-10-15T22:08');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userSettings = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userSettingsPageUrlPattern);
    });
  });
});
