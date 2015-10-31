'use strict';

angular.module('iLoggerApp').controller('DhcpAddressDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'DhcpAddress',
        function($scope, $stateParams, $modalInstance, entity, DhcpAddress) {

        $scope.dhcpAddress = entity;
        $scope.load = function(id) {
            DhcpAddress.get({id : id}, function(result) {
                $scope.dhcpAddress = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('iLoggerApp:dhcpAddressUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.dhcpAddress.id != null) {
                DhcpAddress.update($scope.dhcpAddress, onSaveFinished);
            } else {
                DhcpAddress.save($scope.dhcpAddress, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
