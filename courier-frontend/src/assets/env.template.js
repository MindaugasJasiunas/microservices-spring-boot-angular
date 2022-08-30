(function (window) {
  window["env"] = window["env"] || {}; //make a special (global) variable __env available in our browser window containing the environment variables for our application.

  // Environment variables
  window["env"]["apiUrl"] = "${API_URL}";
  window["env"]["apiLoginUrl"] = "${API_LOGIN_URL}";
  window["env"]["apiRefreshUrl"] = "${API_REFRESH_URL}";
  window["env"]["apiPackagesUrl"] = "${API_PACKAGES_URL}";
  window["env"]["apiPackagesCountUrl"] = "${API_PACKAGES_COUNT_URL}";
})(this);
