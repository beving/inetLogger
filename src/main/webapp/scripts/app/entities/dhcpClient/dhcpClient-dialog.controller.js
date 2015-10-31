'use strict';

angular.module('iLoggerApp').controller('DhcpClientDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'DhcpClient',
        function($scope, $stateParams, $modalInstance, entity, DhcpClient) {

        $scope.dhcpClient = entity;
        $scope.load = function(id) {
            DhcpClient.get({id : id}, function(result) {
                $scope.dhcpClient = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('iLoggerApp:dhcpClientUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.dhcpClient.id != null) {
                DhcpClient.update($scope.dhcpClient, onSaveFinished);
            } else {
                DhcpClient.save($scope.dhcpClient, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
