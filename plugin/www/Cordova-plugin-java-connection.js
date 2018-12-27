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
