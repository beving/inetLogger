'use strict';

angular.module('iLoggerApp')
    .controller('DhcpClientDetailController', function ($scope, $rootScope, $stateParams, entity, DhcpClient) {
        $scope.dhcpClient = entity;
        $scope.load = function (id) {
            DhcpClient.get({id: id}, function(result) {
                $scope.dhcpClient = result;
            });
        };
        $rootScope.$on('iLoggerApp:dhcpClientUpdate', function(event, result) {
            $scope.dhcpClient = result;
        });
    });
