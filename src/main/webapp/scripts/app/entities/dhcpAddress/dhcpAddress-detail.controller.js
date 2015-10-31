'use strict';

angular.module('iLoggerApp')
    .controller('DhcpAddressDetailController', function ($scope, $rootScope, $stateParams, entity, DhcpAddress) {
        $scope.dhcpAddress = entity;
        $scope.load = function (id) {
            DhcpAddress.get({id: id}, function(result) {
                $scope.dhcpAddress = result;
            });
        };
        $rootScope.$on('iLoggerApp:dhcpAddressUpdate', function(event, result) {
            $scope.dhcpAddress = result;
        });
    });
