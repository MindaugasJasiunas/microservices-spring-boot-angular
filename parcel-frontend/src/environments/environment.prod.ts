export const environment = {
  production: true,
  // apiUrl: 'http://localhost:9090/', // Spring Cloud API Gateway entrypoint
  // apiLoginUrl: 'http://localhost:9090/login',
  // apiRegisterUrl: 'http://localhost:9090/register',
  // apiRefreshUrl: 'http://localhost:9090/resettoken',
  // apiPackagesUrl: 'http://localhost:9090/parcels',
  // apiPackageTrackingUrl: 'http://localhost:9090/tracking/',
  // apiCreatePackageUrl: 'http://localhost:9090/new'

  // Error: Element implicitly has an 'any' type because index expression is not of type 'number'.
  // apiUrl: window["env"]["apiUrl"] || 'http://localhost:9090/', // Spring Cloud API Gateway entrypoint
  // apiLoginUrl: window["env"]["apiLoginUrl"] || 'http://localhost:9090/login',
  // apiRegisterUrl: window["env"]["apiRegisterUrl"] || 'http://localhost:9090/register',
  // apiRefreshUrl: window["env"]["apiRefreshUrl"] || 'http://localhost:9090/resettoken',
  // apiPackagesUrl: window["env"]["apiPackagesUrl"] || 'http://localhost:9090/parcels',
  // apiPackageTrackingUrl: window["env"]["apiPackageTrackingUrl"] || 'http://localhost:9090/tracking/',
  // apiCreatePackageUrl: window["env"]["apiCreatePackageUrl"] || 'http://localhost:9090/new',

  apiUrl: (window as { [key: string]: any })["env"]["apiUrl"] as string,
  apiLoginUrl: (window as { [key: string]: any })["env"]["apiLoginUrl"] || 'http://localhost:9090/login',
  apiRegisterUrl: (window as { [key: string]: any })["env"]["apiRegisterUrl"] || 'http://localhost:9090/register',
  apiRefreshUrl: (window as { [key: string]: any })["env"]["apiRefreshUrl"] || 'http://localhost:9090/resettoken',
  apiPackagesUrl: (window as { [key: string]: any })["env"]["apiPackagesUrl"] || 'http://localhost:9090/parcels',
  apiPackageTrackingUrl: (window as { [key: string]: any })["env"]["apiPackageTrackingUrl"] || 'http://localhost:9090/tracking/',
  apiCreatePackageUrl: (window as { [key: string]: any })["env"]["apiCreatePackageUrl"] || 'http://localhost:9090/new',

};
