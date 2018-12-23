
var androidHelper = require('./lib/android-helper');
var utilities = require("./lib/utilities");

module.exports = function before_plugin_rm(context) {
    try {
        var platforms = context.opts.cordova.platforms;

        // Remove the Gradle modifications that were added when the plugin was installed.
        if (platforms.indexOf("android") !== -1) {
            androidHelper.removeFabricBuildToolsFromGradle();
        }
    } catch (ex) {
        console.error('ERROR: before_plugin_rm %o', arguments);
    }
};
