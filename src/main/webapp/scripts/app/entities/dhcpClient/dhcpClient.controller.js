'use strict';

angular.module('iLoggerApp')
    .controller('DhcpClientController', function ($scope, DhcpClient) {
        $scope.dhcpClients = [];
        $scope.loadAll = function() {
            DhcpClient.query(function(result) {
               $scope.dhcpClients = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            DhcpClient.get({id: id}, function(result) {
                $scope.dhcpClient = result;
                $('#deleteDhcpClientConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            DhcpClient.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDhcpClientConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.dhcpClient = {
                macAddress: null,
                hostname: null,
                comment: null,
                id: null
            };
        };
    });
