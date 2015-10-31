'use strict';

angular.module('iLoggerApp')
    .factory('DhcpClient', function ($resource, DateUtils) {
        return $resource('api/dhcpClients/:id', {}, {
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
