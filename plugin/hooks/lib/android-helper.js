
var fs = require("fs");
var path = require("path");
var utilities = require("./utilities");

module.exports = {

    addFabricBuildToolsGradle: function addFabricBuildToolsGradle() {

        var buildGradle = utilities.readBuildGradle();
        var script = [
            "",
            "",
            "// Cordova plugin prueba - Start Build Tools",
            "buildscript {",
            "    repositories {",
            "        google()",
            "        jcenter()",
            "    }",
            "    dependencies {",
            "        classpath 'com.google.protobuf:protobuf-gradle-plugin:0.8.6'",
            "    }",
            "}",
            "",
            "// Cordova Cordova plugin prueba - End Build Tools",
            "",
            ""
        ].join("\n");
        buildGradle += script;
        utilities.writeBuildGradle(buildGradle);
    },

    removeFabricBuildToolsFromGradle: function removeFabricBuildToolsFromGradle() {
        var buildGradle = utilities.readBuildGradle();
        buildGradle = buildGradle.replace(/\n\/\/ Cordova Plugin Prueba - Start Build Tools[\s\S]*\/\/ Cordova Plugin Prueba - End Build Tools/, "");
        utilities.writeBuildGradle(buildGradle);
    }
};
