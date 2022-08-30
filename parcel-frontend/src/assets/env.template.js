(function (window) {
  window["env"] = window["env"] || {};

  // Environment variables
  window["env"]["apiUrl"] = "${API_URL}";
  window["env"]["apiLoginUrl"] = "${API_LOGIN_URL}";
  window["env"]["apiRegisterUrl"] = "${API_REGISTER_URL}";
  window["env"]["apiRefreshUrl"] = "${API_REFRESH_URL}";
  window["env"]["apiPackagesUrl"] = "${API_PACKAGES_URL}";
  window["env"]["apiPackageTrackingUrl"] = "${API_PACKAGE_TRACKING_URL}";
  window["env"]["apiCreatePackageUrl"] = "${API_CREATE_PACKAGE_URL}";
})(this);
