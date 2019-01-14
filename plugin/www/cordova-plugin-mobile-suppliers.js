var exec = require('cordova/exec');

// Metodos que se exponen a nivel de Javascript para su conexion con java. 

exports.sayHello = function (arg0, success, error) {
  exec(success, error, 'CordovaPluginMobileSuppliers', 'sayHello', [arg0]);
};

exports.startService = function (success, error) {
  exec(success, error, 'CordovaPluginMobileSuppliers', 'startService');
};

exports.stopService = function (success, error) {
  exec(success, error, 'CordovaPluginMobileSuppliers', 'stopService');
};

exports.getLogs = function (success, error) {
  exec(success, error, 'CordovaPluginMobileSuppliers', 'getLogs');
};

exports.initParameters = function (pluginParameters, success, error) {
  exec(success, error, 'CordovaPluginMobileSuppliers', 'initParameters', [pluginParameters]);
};

exports.setParameters = function (pluginParameters, success, error) {
  exec(success, error, 'CordovaPluginMobileSuppliers', 'setParameters', [pluginParameters]);
};

exports.getParameters = function (success, error) {
  exec(success, error, 'CordovaPluginMobileSuppliers', 'getParameters');
};

exports.getTrackingPositionsByActivity = function (activityId, success, error) {
  exec(success, error, 'CordovaPluginMobileSuppliers', 'getTrackingPositionsByActivity', [activityId]);
};

exports.deleteTrackingPositionsByActivity = function (activityId, success, error) {
  exec(success, error, 'CordovaPluginMobileSuppliers', 'deleteTrackingPositionsByActivity', [activityId]);
};