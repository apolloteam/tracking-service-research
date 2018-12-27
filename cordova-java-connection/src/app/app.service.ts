'use strict';

import angular from 'angular';
import { Injectable } from 'angular-ts-decorators';

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
                    this.$log.debug(`${methodName} (error) reason %o`, reason);
                    deferred.reject(reason);
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
                    deferred.resolve();
                },
                (reason) => {
                    // Fail.
                    this.$log.debug(`${methodName} (error) reason %o`, reason);
                    deferred.reject(reason);
                });
        } else {
            deferred.resolve();
        }

        return deferred.promise;
    }

    /**
     * Optiene el listado de logs.
     * @memberof AppService
     */
    public getLogs(): void {
        const methodName: string = `${AppService.name}::getLogs`;
        this.$log.debug(`${methodName}`);
        if (this.isAvailable()) {
            this.$window.CordovaPluginJavaConnection.getLogs(
                (resp) => {
                    this.$log.debug(`${methodName} (success)  resp: %o`, resp);
                    const data = JSON.parse(resp);
                    this.insertLog(data);
                });
        }
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