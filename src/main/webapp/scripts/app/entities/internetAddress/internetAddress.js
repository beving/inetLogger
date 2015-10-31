'use strict';

angular.module('iLoggerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('internetAddress', {
                parent: 'entity',
                url: '/internetAddresss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'iLoggerApp.internetAddress.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/internetAddress/internetAddresss.html',
                        controller: 'InternetAddressController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('internetAddress');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('internetAddress.detail', {
                parent: 'entity',
                url: '/internetAddress/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'iLoggerApp.internetAddress.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/internetAddress/internetAddress-detail.html',
                        controller: 'InternetAddressDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('internetAddress');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'InternetAddress', function($stateParams, InternetAddress) {
                        return InternetAddress.get({id : $stateParams.id});
                    }]
                }
            })
            .state('internetAddress.new', {
                parent: 'internetAddress',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/internetAddress/internetAddress-dialog.html',
                        controller: 'InternetAddressDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    ipAddress: null,
                                    hostname: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('internetAddress', null, { reload: true });
                    }, function() {
                        $state.go('internetAddress');
                    })
                }]
            })
            .state('internetAddress.edit', {
                parent: 'internetAddress',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/internetAddress/internetAddress-dialog.html',
                        controller: 'InternetAddressDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['InternetAddress', function(InternetAddress) {
                                return InternetAddress.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('internetAddress', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
