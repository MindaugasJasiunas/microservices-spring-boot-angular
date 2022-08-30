(function (window) {
  window["env"] = window["env"] || {};  //make a special (global) variable __env available in our browser window containing the environment variables for our application.

  // Environment variables
  window["env"]["apiUrl"] = "http://localhost:9090/";
  window["env"]["apiLoginUrl"] = "http://localhost:9090/login";
  window["env"]["apiRefreshUrl"] = "http://localhost:9090/resettoken";
  window["env"]["apiPackagesUrl"] = "http://localhost:9090/parcels";
  window["env"]["apiPackagesCountUrl"] = "http://localhost:9090/parcels/count";
})(this);
