;
(function() {
    'use strict';

    var config = {
        baseURL: './',
        defaultExtension: 'js',
        paths: {
            '/': '/'
        },
        map: {
            'angular': 'vendor/angular/angular.js',
            'angular-animate': 'vendor/angular-animate/angular-animate.js',
            'angular-material': 'vendor/angular-material/angular-material.js',
            'angular-material-icons': 'vendor/angular-material-icons/angular-material-icons.min.js',
            'material-icons.css': 'vendor/material-design-icons/material-icons.css',
            'angular-aria': 'vendor/angular-aria/angular-aria.js',
            'angular-ui-router': 'vendor/angular-ui-router/angular-ui-router.js',
            'angular-ts-decorators': 'vendor/angular-ts-decorators/angular-ts-decorators.js',
            'reflect-metadata': 'vendor/reflect-metadata/Reflect.js',
            'tslib': 'vendor/tslib/tslib.js',
            'css': 'vendor/plugin-css/css.js'
        },
        meta: {
            'vendor/angular/angular.js': {
                format: 'global',
                exports: 'angular'
            },
            'vendor/angular-material/angular-material.js': {
                deps: [
                    'angular',
                    'angular-animate',
                    'angular-aria'
                ]
            },
            'vendor/angular-material-icons/angular-material-icons.min.js': {
                deps: [
                    'angular-material',
                    'vendor/angular-material-icons/angular-material-icons.css'
                ]
            },
            '*.css': { loader: 'css' }
        },
        packages: {
            '/': {
                defaultExtension: 'js'
            }
        }
    };

    System.config(config);
}());