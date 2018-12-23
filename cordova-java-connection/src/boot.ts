'use strict';
import 'tslib';
import angular from 'angular';
import { AppModule } from './app/app.module';
import { ModuleHelper } from './core/module.helper';
import 'material-icons.css';

// Inicia la aplicación angular.
angular.element(document).ready(function () {
    const $log: angular.ILogService = ModuleHelper.getLogger();
    $log.debug('angular.bootstrap %o', AppModule.name)
    const htmlNode: HTMLElement = document.querySelector('html')
    angular.bootstrap(htmlNode, [AppModule.name]);
});