'use strict';

angular.module('iLoggerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dhcpClient', {
                parent: 'entity',
                url: '/dhcpClients',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'iLoggerApp.dhcpClient.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dhcpClient/dhcpClients.html',
                        controller: 'DhcpClientController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dhcpClient');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('dhcpClient.detail', {
                parent: 'entity',
                url: '/dhcpClient/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'iLoggerApp.dhcpClient.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dhcpClient/dhcpClient-detail.html',
                        controller: 'DhcpClientDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('dhcpClient');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'DhcpClient', function($stateParams, DhcpClient) {
                        return DhcpClient.get({id : $stateParams.id});
                    }]
                }
            })
            .state('dhcpClient.new', {
                parent: 'dhcpClient',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dhcpClient/dhcpClient-dialog.html',
                        controller: 'DhcpClientDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    macAddress: null,
                                    hostname: null,
                                    comment: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('dhcpClient', null, { reload: true });
                    }, function() {
                        $state.go('dhcpClient');
                    })
                }]
            })
            .state('dhcpClient.edit', {
                parent: 'dhcpClient',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dhcpClient/dhcpClient-dialog.html',
                        controller: 'DhcpClientDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['DhcpClient', function(DhcpClient) {
                                return DhcpClient.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('dhcpClient', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
