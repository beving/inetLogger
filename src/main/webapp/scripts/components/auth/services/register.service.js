'use strict';

angular.module('iLoggerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


