'use strict';

angular.module('iLoggerApp')
    .controller('InternetAddressController', function ($scope, InternetAddress) {
        $scope.internetAddresss = [];
        $scope.loadAll = function() {
            InternetAddress.query(function(result) {
               $scope.internetAddresss = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            InternetAddress.get({id: id}, function(result) {
                $scope.internetAddress = result;
                $('#deleteInternetAddressConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            InternetAddress.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteInternetAddressConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.internetAddress = {
                ipAddress: null,
                hostname: null,
                id: null
            };
        };
    });
