describe('Parcel Tracking Page', () => {
  beforeEach(() => {
    cy.visit('/'); // corresponds to cypress.json baseUrl // http://localhost:4200
  });

  it('should display main page', () => {
    cy.contains('Package Tracking'); //check that we have a page by checking page information
    cy.get('h1').should('have.text', 'Package Tracking');
  });

  it('should load package tracking page with info of invalid package', () => {
    cy.get('button[id="submitButton"]').should('be.disabled');

    cy.get('#trackingNumber').type('00000000000000000000000000000000').should('have.value', '00000000000000000000000000000000');

    cy.get('button[id="submitButton"]').should('be.enabled');

    cy.get('button[id="submitButton"]').click();

    cy.url().should('eq', 'http://localhost:4200/tracking/00000000000000000000000000000000')

    cy.get('div[class="alert alert-danger"]').should('have.text', ' Opps! Something went wrong. Please try again later ');
  });

  it('should load package tracking page with info of valid package', () => {
    cy.intercept('GET', 'http://localhost:9090/tracking/00000000000000000000000000012345', { fixture: 'tracking12345.json' }).as("getParcelTracking");

    cy.get('button[id="submitButton"]').should('be.disabled');

    cy.get('#trackingNumber').type('00000000000000000000000000012345').should('have.value', '00000000000000000000000000012345');

    cy.get('button[id="submitButton"]').should('be.enabled');

    cy.get('button[id="submitButton"]').click();

    cy.wait("@getParcelTracking");

    cy.url().should('eq', 'http://localhost:4200/tracking/00000000000000000000000000012345')

    // alert div should NOT exist
    cy.get('div[class="alert alert-danger"]').should('not.exist');

    // test package tracking page
    cy.get('mat-card-title[class="mat-card-title"]').should('exist');
    cy.get('mat-card-title[class="mat-card-title"]').should('contain.text', 'Package current status:');
    cy.get('mat-card-subtitle[class="mat-card-subtitle"]').should('have.text', 'Package tracking number: 00000000000000000000000000012345');
  });

});
