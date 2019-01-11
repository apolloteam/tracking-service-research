'use strict';

import angular from 'angular';
import { Injectable } from 'angular-ts-decorators';
import { PluginParameters } from './plugin-parameters.entity';

/**
 * Servicio Angular que se conecta con el plugin.
 * @export
 * @class AppService
 */
@Injectable()
export class AppService {

    //#region Declarations

    /**
     * Inyeccion de dependencias.
     * @static
     * @memberof AppService
     */
    public static $inject = [
        '$log',
        '$q',
        '$window'
    ];

    /**
     * Listado de logs del lado de la aplicacion.
     * @type {any[]}
     * @memberof AppService
     */
    public logList: any[] = [];

    //#endregion 

    //#region Constructor

    constructor(
        private $log: angular.ILogService,
        private $q: angular.IQService,
        private $window: angular.IWindowService) {
        this.$log.debug(`${AppService.name}::ctor`);
    }

    //#endregion

    //#region Methods

    /**
     * Inicia el servicio del plugin.
     * @returns {angular.IPromise<void>}
     * @memberof AppService
     */
    public startService(): angular.IPromise<void> {
        const methodName: string = `${AppService.name}::startService`;
        this.$log.debug(`${methodName}`);

        const deferred: angular.IDeferred<void> = this.$q.defer<void>();
        if (this.isAvailable()) {
            this.$window.CordovaPluginJavaConnection.startService(
                () => {
                    // Success.
                    this.$log.debug(`${methodName} (success)`);
                    deferred.resolve();
                },
                (reason) => {
                    // Fail.
                    const error = JSON.parse(reason);
                    this.$log.debug(`${methodName} (error) reason %o`, error);
                    deferred.reject(error);
                });
        } else {
            deferred.resolve();
        }

        return deferred.promise;
    }

    /**
     * Detiene el servicio del plugin.
     * @returns {angular.IPromise<void>}
     * @memberof AppService
     */
    public stopService(): angular.IPromise<void> {
        const methodName: string = `${AppService.name}::stopService`;
        this.$log.debug(`${methodName}`);

        const deferred: angular.IDeferred<void> = this.$q.defer<void>();
        if (this.isAvailable()) {
            this.$window.CordovaPluginJavaConnection.stopService(
                () => {
                    // Success.
                    this.$log.debug(`${methodName} (success)`);
                    this.logList = [];
                    deferred.resolve();
                },
                (reason) => {
                    // Fail.
                    const error = JSON.parse(reason);
                    this.$log.debug(`${methodName} (error) error %o`, error);
                    deferred.reject(error);
                });
        } else {
            deferred.resolve();
        }

        return deferred.promise;
    }

    /**
     * Inicializa los parametros del plugin.
     * @param {PluginParameters} pluginParameters Parametros de la aplicación para transferir al plugin.
     * @returns {angular.IPromise<void>}
     * @memberof AppService
     */
    public initParameters(pluginParameters: PluginParameters): angular.IPromise<void> {
        const methodName: string = `${AppService.name}::initParameters`;
        this.$log.debug(`${methodName}`);

        const deferred: angular.IDeferred<void> = this.$q.defer<void>();
        if (this.isAvailable()) {
            this.$window.CordovaPluginJavaConnection.initParameters(
                pluginParameters,
                (response) => {
                    // Success.
                    this.$log.debug(`${methodName} (success) %o`, response);
                    deferred.resolve();
                },
                (reason) => {
                    // Fail.
                    const error = JSON.parse(reason);
                    this.$log.debug(`${methodName} (error) error %o`, error);
                    deferred.reject(error);
                });
        } else {
            deferred.resolve();
        }

        return deferred.promise;
    }

    /**
     * Establece los parametros del plugin.
     * @param {PluginParameters} pluginParameters Parametros de la aplicación para transferir al plugin.
     * @returns {angular.IPromise<void>}
     * @memberof AppService
     */
    public setParameters(pluginParameters: PluginParameters): angular.IPromise<void> {
        const methodName: string = `${AppService.name}::setParameters`;
        this.$log.debug(`${methodName}`);

        const deferred: angular.IDeferred<void> = this.$q.defer<void>();
        if (this.isAvailable()) {
            this.$window.CordovaPluginJavaConnection.setParameters(
                pluginParameters,
                (response) => {
                    // Success.
                    this.$log.debug(`${methodName} (success) response %o`, response);
                    deferred.resolve();
                },
                (reason) => {
                    // Fail.
                    const error = JSON.parse(reason);
                    this.$log.debug(`${methodName} (error) error %o`, error);
                    deferred.reject(error);
                });
        } else {
            deferred.resolve();
        }

        return deferred.promise;
    }

    /**
     * Obtiene los parametros establecidos en el plugin.
     * @returns {angular.IPromise<void>}
     * @memberof AppService
     */
    public getParameters(): angular.IPromise<string> {
        const methodName: string = `${AppService.name}::getParameters`;
        this.$log.debug(`${methodName}`);

        const deferred: angular.IDeferred<string> = this.$q.defer<string>();
        if (this.isAvailable()) {
            this.$window.CordovaPluginJavaConnection.getParameters(
                (response: string) => {
                    // Success.
                    this.$log.debug(`${methodName} (success) response %o`, response);
                    deferred.resolve(response);
                },
                (reason) => {
                    // Fail.
                    const error = JSON.parse(reason);
                    this.$log.debug(`${methodName} (error) error %o`, error);
                    deferred.reject(error);
                });
        } else {
            deferred.resolve();
        }

        return deferred.promise;
    }

    /**
     * Obtiene el listado de logs.
     * @memberof AppService
     */
    public getLogs(): angular.IPromise<void> {
        const methodName: string = `${AppService.name}::getLogs`;
        this.$log.debug(`${methodName}`);
        const deferred: angular.IDeferred<void> = this.$q.defer<void>();
        if (this.isAvailable()) {
            this.$window.CordovaPluginJavaConnection.getLogs(
                (resp) => {
                    this.$log.debug(`${methodName} (success)  resp: %o`, resp);
                    const data = JSON.parse(resp);
                    this.insertLog(data);
                    deferred.resolve();
                });
        }

        return deferred.promise;
    }

    /**
     * Obtiene el trackeo de puntos de una actividad.
     * @param {string} activityId Id de la actividad.
     * @returns {angular.IPromise<string[]>} Promesa con los puntos de la actividad.
     * @memberof AppService
     */
    public getTrackingPositionsByActivity(activityId: string): angular.IPromise<string[]> {
        const methodName: string = `${AppService.name}::getTrackingPositionsByActivity`;
        this.$log.debug(`${methodName}`);

        const deferred: angular.IDeferred<string[]> = this.$q.defer();

        if (this.isAvailable()) {
            this.$window.CordovaPluginJavaConnection.getTrackingPositionsByActivity(
                activityId,
                (resp) => {
                    this.$log.debug(`${methodName} (success)  resp: %o`, resp);
                    const data: string[] = resp
                    deferred.resolve(data);
                },
                (reason) => {
                    // Fail.
                    const error = JSON.parse(reason);
                    this.$log.debug(`${methodName} (error) error %o`, error);
                    deferred.reject(error);
                });
        }

        return deferred.promise;
    }

    /**
     * Elimina el trackeo de puntos de una actividad.
     * @param {string} activityId Id de la actividad.
     * @returns {angular.IPromise<void>} Promesa de confirmación.
     * @memberof AppService
     */
    public deleteTrackingPositionsByActivity(activityId: string): angular.IPromise<void> {
        const methodName: string = `${AppService.name}::deleteTrackingPositionsByActivity`;
        this.$log.debug(`${methodName}`);

        const deferred: angular.IDeferred<void> = this.$q.defer();

        if (this.isAvailable()) {
            this.$window.CordovaPluginJavaConnection.deleteTrackingPositionsByActivity(
                activityId,
                (resp) => { // Success.
                    this.$log.debug(`${methodName} (success) response %o`, resp);
                    deferred.resolve();
                },
                (reason) => {
                    // Fail.
                    const error = JSON.parse(reason);
                    this.$log.debug(`${methodName} (error) error %o`, error);
                    deferred.reject(error);
                });
        }

        return deferred.promise;
    }


    /**
     * Proceso de almacenado de log.
     * @private
     * @param {LogInterface} log Log proveniente del plugin.
     * @memberof AppService
     */
    private insertLog(logs: string[]): void {
        this.logList = [...logs]
    }

    /**
     * Devuelve el listado de mensajes.
     * @returns {any[]} Listado de mensajes.
     * @memberof AppService
     */
    public getConsoleMessages(): any[] {
        return this.logList;
    }

    /**
     * Limpia el listado de mensajes.
     * @memberof AppService
     */
    public resetConsoleMessages(): void {
        this.$log.debug(`${AppService.name}::resetConsoleMessages`);
        this.logList = [];
    }

    /**
     * Devuelve true si el plugin está listo para ser usado. False de lo contrario.
     * @private
     * @returns {boolean} True si el plugin está listo para ser usado. False de lo contrario.
     * @memberof AppService
     */
    private isAvailable(): boolean {
        // tslint:disable-next-line:no-unnecessary-local-variable
        const ret: boolean = angular.isDefined(this.$window.CordovaPluginJavaConnection) ? true : false;
        return ret;
    }

    //#endregion
}