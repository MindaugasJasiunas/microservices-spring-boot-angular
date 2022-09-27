describe('Login Page', () => {
  beforeEach(() => {
    cy.visit(Cypress.env('login_url')); // corresponds to cypress.json baseUrl // http://localhost:4200

    // mock login & reset Jwt token URL's responses
    cy.intercept('POST', 'http://localhost:9090/login', {
      statusCode: 200,
      headers: {
        Authorization:
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2NjQyNzY4NjksImV4cCI6MTY5NTgxMjg3MSwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIlJvbGUiOiJ1c2VyIn0.W8KCA_YF3nhqD1CHjeIMj4959CIBZCHqMvfl0Z-B3vY',
      },
    }).as('loginResponse');

    cy.intercept('POST', 'http://localhost:9090/resettoken', {
      statusCode: 200,
      headers: {
        Authorization:
          'Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2NjQyNzY4NjksImV4cCI6MTY5NTgxMjg3MSwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoianJvY2tldEBleGFtcGxlLmNvbSIsIlJvbGUiOiJ1c2VyIn0.W8KCA_YF3nhqD1CHjeIMj4959CIBZCHqMvfl0Z-B3vY',
      },
    }).as('resetTokenResponse');
  });

  it('should display login page', () => {
    cy.get('mat-card-title[class="mat-card-title"]').should('exist');
    cy.get('mat-card-title[class="mat-card-title"]').should(
      'have.text',
      'Login'
    );
    cy.get('input').should('have.length', 2);
    cy.url().should('eq', 'http://localhost:4200/login');
  });

  // it('should display error when username/password is incorrect - not working because always logging in', () => {
  //   // insert login data
  //   cy.get('input[formcontrolname="username"]').type('johndoe');
  //   cy.get('input[formcontrolname="password"]').type('password');
  //   // click login
  //   cy.get('span').contains('Login').parent().click();

  //   // check response page
  //   cy.url().should('eq', 'http://localhost:4200/login')
  //   cy.get('p[class="error"]').should('contain.text', 'Username or password invalid')
  // });

  it('should display main page when logged in', () => {
    // insert login data
    cy.get('input[formcontrolname="username"]').type('johndoe');
    cy.get('input[formcontrolname="password"]').type('password');
    // click login
    cy.get('span').contains('Login').parent().click();

    cy.wait('@loginResponse');
    cy.wait('@resetTokenResponse');

    // check response page
    cy.get('mat-toolbar').should('contain.text', 'Welcome back ');
    cy.get('h1').should('have.text', 'Package Tracking');
  });

  it('should NOT display my packages page - when NOT logged in - redirects to login page', () => {
    cy.visit('/packages');
    // check if redirected
    cy.url().should('eq', 'http://localhost:4200/login');
  });

  it('should display my packages page - when logged in', () => {
    // insert login data
    cy.get('input[formcontrolname="username"]').type('johndoe');
    cy.get('input[formcontrolname="password"]').type('password');
    // click login
    cy.get('span').contains('Login').parent().click();

    cy.wait('@loginResponse');
    cy.wait('@resetTokenResponse');

    // check response page
    cy.get('mat-icon').contains('inventory_2').click();
    cy.get('h2').should('have.text', 'My packages');
    cy.url().should('eq', 'http://localhost:4200/packages');
  });

  it('should NOT display send package page - when NOT logged in - redirects to login page', () => {
    cy.visit('/send');
    // check if redirected
    cy.url().should('eq', 'http://localhost:4200/login');
  });

  it('should display send package page - when logged in', () => {
    // insert login data
    cy.get('input[formcontrolname="username"]').type('johndoe');
    cy.get('input[formcontrolname="password"]').type('password');
    // click login
    cy.get('span').contains('Login').parent().click();

    cy.wait('@loginResponse');
    cy.wait('@resetTokenResponse');

    // check response page
    cy.get('mat-icon').contains('local_shipping').click();
    cy.get('h2').should('have.text', 'Send package');
    cy.url().should('eq', 'http://localhost:4200/send');
  });

  it('should be able to send a package - when logged in', () => {

    cy.intercept('POST', 'http://localhost:9090/new', {
      statusCode: 200
    }).as('postNewPackage');

    // insert login data
    cy.get('input[formcontrolname="username"]').type('johndoe');
    cy.get('input[formcontrolname="password"]').type('password');
    // click login
    cy.get('span').contains('Login').parent().click();

    cy.wait('@loginResponse');
    cy.wait('@resetTokenResponse');

    // check response page
    cy.visit('/send');
    cy.get('input[id="packageContentsDescriptionInput"]').type(
      'package/parcel description'
    );

    cy.get('input[id="packageWeightInput"]').type('16.2');
    cy.get('input[id="packageQtyInput"]').type('1');
    cy.get('mat-checkbox[id="fragileCheck"]').click();
    cy.get('input[id="senderFirstNameInput"]').type('Jane');
    cy.get('input[id="senderLastNameInput"]').type('Williams');
    cy.get('input[id="senderPhoneNumberInput"]').type('+44 05681541');
    cy.get('input[id="senderEmailInput"]').type('jane.williams@example.com');

    cy.get('input[id="senderAddress1Input"]').type('136 Tettenhall Rd');
    cy.get('input[id="senderHouseNumberInput"]').type('16');
    cy.get('input[id="senderApartmentNumberInput"]').type('136');
    cy.get('input[id="senderCityInput"]').type('Wolverhampton');
    cy.get('input[id="senderPostalCodeInput"]').type('WV6 0BQ');
    cy.get('input[id="senderStateInput"]').type('West Midlands');
    cy.get('input[id="senderCompanyInput"]').type('Willson co.');

    cy.get('input[id="receiverFirstNameInput"]').type('Tom');
    cy.get('input[id="receiverLastNameInput"]').type('Henninbur');
    cy.get('input[id="receiverPhoneNumberInput"]').type('+44 89461532');
    cy.get('input[id="receiverEmailInput"]').type('henninbuttom@example.com');

    cy.get('input[id="receiverAddress1Input"]').type('Woolsington');
    cy.get('input[id="receiverHouseNumberInput"]').type('1');
    cy.get('input[id="receiverApartmentNumberInput"]').type('1');
    cy.get('input[id="receiverCityInput"]').type('Newcastle upon Tyne');
    cy.get('input[id="receiverPostalCodeInput"]').type('NE13 8BZ');
    cy.get('input[id="receiverStateInput"]').type('Tyne and Wear');

    cy.get('button').contains('Send new package!').click();

    cy.wait('@postNewPackage');

    // should send & redirect
    cy.url().should('eq', 'http://localhost:4200/')

  });
});
