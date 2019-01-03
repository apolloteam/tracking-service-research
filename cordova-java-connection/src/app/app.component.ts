import angular from 'angular';
import 'angular-material';
import { Component } from 'angular-ts-decorators';
import { AppService } from './app.service';
import { PluginParameters } from './plugin-parameters.entity';

@Component({
    selector: 'app',
    templateUrl: 'app/app.view.html',
    styleUrls: 'app/app.css'
})
export class AppComponent {
    public static $inject: string[] = [
        '$log',
        '$mdDialog',
        '$mdToast',
        AppService.name
    ];

    /**
     * Toggle para renderizar el listado de logs.
     */
    public showLogToggle: boolean = false;

    /**
     * Indica si existe un proceso en progreso.
     */
    public inProgress: boolean = false;

    constructor(
        private $log: angular.ILogService,
        private $mdDialog: angular.material.IDialogService,
        private $mdToast: angular.material.IToastService,
        private appService: AppService) {
        this.$log.debug(`${AppComponent.name}::ctor`);
    }

    /**
     * Inicia el servicio del plugin.
     */
    public startService() {
        const methodName: string = `${AppComponent.name}::startService`;
        this.$log.debug(`${methodName}`);

        this.inProgress = true;

        // Invoca el metodo del servicio angular para conectar con el plugin.
        this.appService.startService()
            .then(() => {
                // Success.
                this.$log.debug(`${methodName} (then)`);

                // Muestra el toast.
                const msg = 'Started';
                this.showToast(msg);
            })
            .catch((err) => {
                // Error.
                this.$log.debug(`${methodName} (catch)`);
                this.showError(err.message);
            })
            .finally(() => {
                this.inProgress = false;
            });
    }

    /**
     * Detiene el servicio del plugin.
     */
    public stopService() {
        const methodName: string = `${AppComponent.name}::stopService`;
        this.$log.debug(`${methodName}`);
        this.inProgress = true;
        this.appService.stopService()
            .then(() => {
                // Success.
                this.$log.debug(`${methodName} (then)`);
                const msg = 'Logs were removed.';
                this.showToast(msg);
            })
            .catch((err) => {
                // Error.
                this.$log.debug(`${methodName} (catch)`);
                this.showError(err.message);
            })
            .finally(() => {
                this.inProgress = false;
            });
    }

    /**
     * Muestra/Oculta el listado de logs.
     */
    public showLog() {
        const methodName: string = `${AppComponent.name}::showLog`;
        this.$log.debug(`${methodName}`);
        this.inProgress = true;
        this.appService.getLogs()
            .finally(() => {
                this.inProgress = false;
            });
    }

    /**
     * Inicializa los parametros del plugin.
     */
    public initParams() {
        const methodName: string = `${AppComponent.name}::initParams`;
        this.$log.debug(`${methodName}`);

        const parameters: PluginParameters = {
            trackingApiBaseUrl: 'http://api.traslada.com.ar',
            logApiBaseUrl: 'http://api.traslada.com.ar',
            gpsInterval: 10
        }

        this.appService.initParameters(parameters)
            .then(() => {
                // Muestra el toast.
                const msg = 'Parametros inicializados.';
                this.showToast(msg);
            })
            .catch((error) => {
                this.showError(error);
            })
    }

    /**
     * Establece los parametros del plugin.
     */
    public setParams() {
        const methodName: string = `${AppComponent.name}::setParams`;
        this.$log.debug(`${methodName}`);

        const parameters: PluginParameters = {
            holderId: 778,
            activityId: 7885478,
            ownerId: 5159,
            holderStatus: '100',
            activityStatus: '500'
        }

        this.appService.setParameters(parameters)
            .then(() => {
                // Muestra el toast.
                const msg = 'Parametros establecidos';
                this.showToast(msg);
            })
            .catch((error) => {
                this.showError(error);
            })
    }

    /**
     * Obtiene los parametros establecidos en el plugin.
     */
    public getParams() {
        const methodName: string = `${AppComponent.name}::getParams`;
        this.$log.debug(`${methodName}`);
        this.appService.getParameters()
            .then((response:string) => {
                const confirm: angular.material.IConfirmDialog = this.$mdDialog.confirm();
                confirm
                    .title('Parametros')
                    .textContent(response)
                    .ok('ok')
                    .hasBackdrop(true)
                    .clickOutsideToClose(false)
                    .escapeToClose(false);
                this.$mdDialog.show(confirm);
            })
            .catch((error) => {
                this.showError(error);
            })
    }

    /**
     * Muestra el toast con el mensaje correspondiente.
     */
    private showToast(msg: string) {
        const toastConfig: angular.material.ISimpleToastPreset = this.$mdToast.simple()
            .hideDelay(2000)
            .position('bottom')
            .textContent(msg);

        this.$mdToast.show(toastConfig);
    }

    /**
     * Muestra el error.
     */
    public showError(message: string) {
        const title: string = 'Ha ocurrido un error.';
        const confirm: angular.material.IConfirmDialog = this.$mdDialog.confirm();
        confirm
            .title(title)
            .textContent(message)
            .ok('ok')
            .hasBackdrop(true)
            .clickOutsideToClose(false)
            .escapeToClose(false);
        this.$mdDialog.show(confirm);
    }
}