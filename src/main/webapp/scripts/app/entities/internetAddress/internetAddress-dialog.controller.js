'use strict';

angular.module('iLoggerApp').controller('InternetAddressDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'InternetAddress',
        function($scope, $stateParams, $modalInstance, entity, InternetAddress) {

        $scope.internetAddress = entity;
        $scope.load = function(id) {
            InternetAddress.get({id : id}, function(result) {
                $scope.internetAddress = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('iLoggerApp:internetAddressUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.internetAddress.id != null) {
                InternetAddress.update($scope.internetAddress, onSaveFinished);
            } else {
                InternetAddress.save($scope.internetAddress, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
