'use strict';

angular.module('iLoggerApp')
    .controller('InternetAddressDetailController', function ($scope, $rootScope, $stateParams, entity, InternetAddress) {
        $scope.internetAddress = entity;
        $scope.load = function (id) {
            InternetAddress.get({id: id}, function(result) {
                $scope.internetAddress = result;
            });
        };
        $rootScope.$on('iLoggerApp:internetAddressUpdate', function(event, result) {
            $scope.internetAddress = result;
        });
    });
