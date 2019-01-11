var exec = require('cordova/exec');

// Metodos que se exponen a nivel de Javascript para su conexion con java. 

exports.sayHello = function (arg0, success, error) {
  exec(success, error, 'CordovaPluginJavaConnection', 'sayHello', [arg0]);
};

exports.startService = function (success, error) {
  exec(success, error, 'CordovaPluginJavaConnection', 'startService');
};

exports.stopService = function (success, error) {
  exec(success, error, 'CordovaPluginJavaConnection', 'stopService');
};

exports.getLogs = function (success, error) {
  exec(success, error, 'CordovaPluginJavaConnection', 'getLogs');
};

exports.initParameters = function (pluginParameters, success, error) {
  exec(success, error, 'CordovaPluginJavaConnection', 'initParameters', [pluginParameters]);
};

exports.setParameters = function (pluginParameters, success, error) {
  exec(success, error, 'CordovaPluginJavaConnection', 'setParameters', [pluginParameters]);
};

exports.getParameters = function (success, error) {
  exec(success, error, 'CordovaPluginJavaConnection', 'getParameters');
};

exports.getTrackingPositionsByActivity = function (activityId, success, error) {
  exec(success, error, 'CordovaPluginJavaConnection', 'getTrackingPositionsByActivity', [activityId]);
};

exports.deleteTrackingPositionsByActivity = function (activityId, success, error) {
  exec(success, error, 'CordovaPluginJavaConnection', 'deleteTrackingPositionsByActivity', [activityId]);
};