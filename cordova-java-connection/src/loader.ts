; (function (System) {
  'use strict';
  console && console.log('SystemJS.import: boot');
  System.import('boot')
    .then(function () {
      console && console.info('System.import (then) %o', arguments);
    })
    .catch(function () {
      console && console.error('System.import (catch) %o', arguments);
    });
})(System);
