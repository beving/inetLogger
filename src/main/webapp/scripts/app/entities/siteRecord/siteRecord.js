'use strict';

angular.module('iLoggerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('siteRecord', {
                parent: 'entity',
                url: '/siteRecords',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'iLoggerApp.siteRecord.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/siteRecord/siteRecords.html',
                        controller: 'SiteRecordController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('siteRecord');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('siteRecord.detail', {
                parent: 'entity',
                url: '/siteRecord/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'iLoggerApp.siteRecord.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/siteRecord/siteRecord-detail.html',
                        controller: 'SiteRecordDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('siteRecord');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SiteRecord', function($stateParams, SiteRecord) {
                        return SiteRecord.get({id : $stateParams.id});
                    }]
                }
            })
            .state('siteRecord.new', {
                parent: 'siteRecord',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/siteRecord/siteRecord-dialog.html',
                        controller: 'SiteRecordDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    device: null,
                                    destinationSite: null,
                                    date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('siteRecord', null, { reload: true });
                    }, function() {
                        $state.go('siteRecord');
                    })
                }]
            })
            .state('siteRecord.edit', {
                parent: 'siteRecord',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/siteRecord/siteRecord-dialog.html',
                        controller: 'SiteRecordDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SiteRecord', function(SiteRecord) {
                                return SiteRecord.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('siteRecord', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
