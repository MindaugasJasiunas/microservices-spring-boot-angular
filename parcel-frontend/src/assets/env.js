(function (window) {
  window["env"] = window["env"] || {};  //make a special (global) variable __env available in our browser window containing the environment variables for our application.

  // Environment variables
  window["env"]["apiUrl"] = "http://localhost:9090/";
  window["env"]["apiLoginUrl"] = "http://localhost:9090/login";
  window["env"]["apiRegisterUrl"] = "http://localhost:9090/register";
  window["env"]["apiRefreshUrl"] = "http://localhost:9090/resettoken";
  window["env"]["apiPackagesUrl"] = "http://localhost:9090/parcels";
  window["env"]["apiPackageTrackingUrl"] = "http://localhost:9090/tracking/";
  window["env"]["apiCreatePackageUrl"] = "http://localhost:9090/new";
})(this);
