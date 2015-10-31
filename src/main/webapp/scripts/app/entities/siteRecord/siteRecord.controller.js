'use strict';

angular.module('iLoggerApp')
    .controller('SiteRecordController', function ($scope, SiteRecord) {
        $scope.siteRecords = [];
        $scope.loadAll = function() {
            SiteRecord.query(function(result) {
               $scope.siteRecords = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SiteRecord.get({id: id}, function(result) {
                $scope.siteRecord = result;
                $('#deleteSiteRecordConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SiteRecord.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSiteRecordConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.siteRecord = {
                device: null,
                destinationSite: null,
                date: null,
                id: null
            };
        };
    });
