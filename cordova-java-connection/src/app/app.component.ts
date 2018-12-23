import angular from 'angular';
import 'angular-material';
import { Component } from 'angular-ts-decorators';

@Component({
    selector: 'app',
    templateUrl: 'app/app.view.html',
})
export class AppComponent {
    public static $inject: string[] = [
        '$log'
    ];

    public name: string = '';

    constructor(
        private $log: angular.ILogService) {
        this.$log.debug(`${AppComponent.name}::ctor`);
    }
    
    public sayHello() {
        this.$log.debug(`${AppComponent.name}::sayHello`);
        const w: any = window;
        w.CordovaPluginJavaConnection.sayHello(this.name,
            (resp) => {
                this.$log.debug(`${AppComponent.name}::sayHello (Success) %o`, resp);
                alert(`Respuesta: ${resp}`)
            },
            () => {
                this.$log.debug(`${AppComponent.name}::sayHello (Error)`);
            })
    }
}