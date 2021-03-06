'use strict';

angular.module('iLoggerApp')
    .factory('DhcpAddress', function ($resource, DateUtils) {
        return $resource('api/dhcpAddresss/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
