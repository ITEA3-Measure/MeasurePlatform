(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('measure-view', {
            parent: 'entity',
            url: '/measure-view',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MeasureViews'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/measure-view/measure-views.html',
                    controller: 'MeasureViewController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('measure-view-detail', {
            parent: 'entity',
            url: '/measure-view/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MeasureView'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/measure-view/measure-view-detail.html',
                    controller: 'MeasureViewDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MeasureView', function($stateParams, MeasureView) {
                    return MeasureView.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'measure-view',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('measure-view-detail.edit', {
            parent: 'measure-view-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-view/measure-view-dialog.html',
                    controller: 'MeasureViewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeasureView', function(MeasureView) {
                            return MeasureView.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('measure-view.new', {
            parent: 'measure-view',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-view/measure-view-dialog.html',
                    controller: 'MeasureViewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                viewData: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('measure-view', null, { reload: 'measure-view' });
                }, function() {
                    $state.go('measure-view');
                });
            }]
        })
        .state('measure-view.edit', {
            parent: 'measure-view',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-view/measure-view-dialog.html',
                    controller: 'MeasureViewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeasureView', function(MeasureView) {
                            return MeasureView.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('measure-view', null, { reload: 'measure-view' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('measure-view.delete', {
            parent: 'measure-view',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-view/measure-view-delete-dialog.html',
                    controller: 'MeasureViewDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MeasureView', function(MeasureView) {
                            return MeasureView.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('measure-view', null, { reload: 'measure-view' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
