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
        '$mdToast',
        AppService.name
    ];

    /**
     * Toggle para renderizar el listado de logs.
     */
    public showLogToggle: boolean = false;

    constructor(
        private $log: angular.ILogService,
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

        // Invoca el metodo del servicio angular para conectar con el plugin.
        this.appService.startService()
            .then(() => {
                // Success.
                this.$log.debug(`${methodName} (then)`);

                // Muestra el toast.
                const msg = 'Started';
                this.showToast(msg);

                this.appService.getLogs();
            })
            .catch(() => {
                // Error.
                this.$log.debug(`${methodName} (catch)`);

                const msg = 'Ha ocurrido un error.';
                this.showToast(msg);
            })
    }

    /**
     * Detiene el servicio del plugin.
     */
    public stopService() {
        const methodName: string = `${AppComponent.name}::stopService`;
        this.$log.debug(`${methodName}`);

        this.appService.stopService()
            .then(() => {
                this.$log.debug(`${methodName} (then)`);
                const msg = 'Se detuvo el servicio.';
                this.showToast(msg);
            })
            .catch(() => {
                this.$log.debug(`${methodName} (catch)`);
                const msg = 'Ha ocurrido un error.';
                this.showToast(msg);
            })
    }

    /**
     * Muestra/Oculta el listado de logs.
     */
    public showLog() {
        const methodName: string = `${AppComponent.name}::showLog`;
        this.$log.debug(`${methodName}`);

        this.showLogToggle = !this.showLogToggle;
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
}