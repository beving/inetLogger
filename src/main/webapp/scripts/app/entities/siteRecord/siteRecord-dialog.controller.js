'use strict';

angular.module('iLoggerApp').controller('SiteRecordDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'SiteRecord',
        function($scope, $stateParams, $modalInstance, entity, SiteRecord) {

        $scope.siteRecord = entity;
        $scope.load = function(id) {
            SiteRecord.get({id : id}, function(result) {
                $scope.siteRecord = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('iLoggerApp:siteRecordUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.siteRecord.id != null) {
                SiteRecord.update($scope.siteRecord, onSaveFinished);
            } else {
                SiteRecord.save($scope.siteRecord, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
