 'use strict';

angular.module('iLoggerApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-iLoggerApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-iLoggerApp-params')});
                }
                return response;
            }
        };
    });
