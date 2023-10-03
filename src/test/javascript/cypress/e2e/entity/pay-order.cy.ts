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

describe('PayOrder e2e test', () => {
  const payOrderPageUrl = '/pay-order';
  const payOrderPageUrlPattern = new RegExp('/pay-order(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const payOrderSample = { payOrderNumber: 59039, payOrderDate: '2023-10-02', amount: 50298, slipNo: 1771, controllingNo: 78804 };

  let payOrder;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/pay-orders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pay-orders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pay-orders/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (payOrder) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pay-orders/${payOrder.id}`,
      }).then(() => {
        payOrder = undefined;
      });
    }
  });

  it('PayOrders menu should load PayOrders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('pay-order');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PayOrder').should('exist');
    cy.url().should('match', payOrderPageUrlPattern);
  });

  describe('PayOrder page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(payOrderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PayOrder page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/pay-order/new$'));
        cy.getEntityCreateUpdateHeading('PayOrder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', payOrderPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pay-orders',
          body: payOrderSample,
        }).then(({ body }) => {
          payOrder = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pay-orders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pay-orders?page=0&size=20>; rel="last",<http://localhost/api/pay-orders?page=0&size=20>; rel="first"',
              },
              body: [payOrder],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(payOrderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PayOrder page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('payOrder');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', payOrderPageUrlPattern);
      });

      it('edit button click should load edit PayOrder page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PayOrder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', payOrderPageUrlPattern);
      });

      it.skip('edit button click should load edit PayOrder page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PayOrder');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', payOrderPageUrlPattern);
      });

      it('last delete button click should delete instance of PayOrder', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('payOrder').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', payOrderPageUrlPattern);

        payOrder = undefined;
      });
    });
  });

  describe('new PayOrder page', () => {
    beforeEach(() => {
      cy.visit(`${payOrderPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PayOrder');
    });

    it('should create an instance of PayOrder', () => {
      cy.get(`[data-cy="payOrderNumber"]`).type('78526').should('have.value', '78526');

      cy.get(`[data-cy="payOrderDate"]`).type('2023-10-02').blur().should('have.value', '2023-10-02');

      cy.get(`[data-cy="amount"]`).type('20905').should('have.value', '20905');

      cy.get(`[data-cy="slipNo"]`).type('29444').should('have.value', '29444');

      cy.get(`[data-cy="controllingNo"]`).type('88664').should('have.value', '88664');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        payOrder = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', payOrderPageUrlPattern);
    });
  });
});
