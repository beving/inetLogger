'use strict';

angular.module('iLoggerApp')
    .controller('SiteRecordDetailController', function ($scope, $rootScope, $stateParams, entity, SiteRecord) {
        $scope.siteRecord = entity;
        $scope.load = function (id) {
            SiteRecord.get({id: id}, function(result) {
                $scope.siteRecord = result;
            });
        };
        $rootScope.$on('iLoggerApp:siteRecordUpdate', function(event, result) {
            $scope.siteRecord = result;
        });
    });
