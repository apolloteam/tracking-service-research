
var androidHelper = require('./lib/android-helper');
var utilities = require("./lib/utilities");

module.exports = function after_plugin_add(context) {
    try {
        var platforms = context.opts.cordova.platforms;

        // Modify the Gradle build file to add a task that will upload the debug symbols
        // at build time.
        if (platforms.indexOf("android") !== -1) {
            androidHelper.removeFabricBuildToolsFromGradle();
            androidHelper.addFabricBuildToolsGradle();
        }

    } catch (ex) {
        console.error('ERROR: after_plugin_add %o', arguments);
    }
};
