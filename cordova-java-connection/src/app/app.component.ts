import angular from 'angular';
import 'angular-material';
import { Component } from 'angular-ts-decorators';
import { AppService } from './app.service';

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
    public showError(message:string) {
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