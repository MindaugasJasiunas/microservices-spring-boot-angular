export const environment = {
  production: true,
  apiUrl: (window as { [key: string]: any })["env"]["apiUrl"] || 'http://localhost:9090/',
  apiLoginUrl: (window as { [key: string]: any })["env"]["apiLoginUrl"] || 'http://localhost:9090/login',
  apiRefreshUrl: (window as { [key: string]: any })["env"]["apiRefreshUrl"] || 'http://localhost:9090/resettoken',
  apiPackagesUrl: (window as { [key: string]: any })["env"]["apiPackagesUrl"] || 'http://localhost:9090/parcels',
  apiPackagesCountUrl: (window as { [key: string]: any })["env"]["apiPackagesCountUrl"] || 'http://localhost:9090/parcels/count',
};
