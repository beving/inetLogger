'use strict';

angular.module('iLoggerApp')
    .controller('DhcpAddressController', function ($scope, DhcpAddress) {
        $scope.dhcpAddresss = [];
        $scope.loadAll = function() {
            DhcpAddress.query(function(result) {
               $scope.dhcpAddresss = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            DhcpAddress.get({id: id}, function(result) {
                $scope.dhcpAddress = result;
                $('#deleteDhcpAddressConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            DhcpAddress.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDhcpAddressConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.dhcpAddress = {
                macAddress: null,
                ipAddress: null,
                id: null
            };
        };
    });
