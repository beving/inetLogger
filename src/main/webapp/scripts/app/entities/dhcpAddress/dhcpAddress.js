'use strict';

angular.module('iLoggerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dhcpAddress', {
                parent: 'entity',
                url: '/dhcpAddresss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'iLoggerApp.dhcpAddress.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dhcpAddress/dhcpAddresss.html',
                        controller: 'DhcpAddressController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dhcpAddress');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('dhcpAddress.detail', {
                parent: 'entity',
                url: '/dhcpAddress/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'iLoggerApp.dhcpAddress.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dhcpAddress/dhcpAddress-detail.html',
                        controller: 'DhcpAddressDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dhcpAddress');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DhcpAddress', function($stateParams, DhcpAddress) {
                        return DhcpAddress.get({id : $stateParams.id});
                    }]
                }
            })
            .state('dhcpAddress.new', {
                parent: 'dhcpAddress',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dhcpAddress/dhcpAddress-dialog.html',
                        controller: 'DhcpAddressDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    macAddress: null,
                                    ipAddress: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('dhcpAddress', null, { reload: true });
                    }, function() {
                        $state.go('dhcpAddress');
                    })
                }]
            })
            .state('dhcpAddress.edit', {
                parent: 'dhcpAddress',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dhcpAddress/dhcpAddress-dialog.html',
                        controller: 'DhcpAddressDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['DhcpAddress', function(DhcpAddress) {
                                return DhcpAddress.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('dhcpAddress', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
