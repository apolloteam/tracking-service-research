'use strict';
import angular from 'angular';

export class ModuleHelper {
  public static getLogger(): angular.ILogService {
    const injector: angular.auto.IInjectorService = angular.injector(['ng']);
    const $log: angular.ILogService = injector.get('$log');
    return $log;
  }

  public static decorate(func: () => void, dependencies: string[]): () => void {
    const $log: angular.ILogService = ModuleHelper.getLogger();
    $log.debug(`${ModuleHelper.name}::decorate %o`, func.name);
    func.$inject = dependencies || [];
    return func;
  }
}