import { defineConfig } from 'cypress';

export default defineConfig({
  e2e: {
    baseUrl: 'http://localhost:4200',
    env: {
      login_url: '/login',
      register_url: '/register',
      parcels_url: '/parcels',
    },
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});
