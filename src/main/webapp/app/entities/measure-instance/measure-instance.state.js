(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('measure-instance', {
            parent: 'entity',
            url: '/measure-instance',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MeasureInstances'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/measure-instance/measure-instances.html',
                    controller: 'MeasureInstanceController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('measure-instance-detail', {
            parent: 'entity',
            url: '/measure-instance/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MeasureInstance'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/measure-instance/measure-instance-detail.html',
                    controller: 'MeasureInstanceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MeasureInstance', function($stateParams, MeasureInstance) {
                    return MeasureInstance.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'measure-instance',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('measure-instance-detail.edit', {
            parent: 'measure-instance-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-instance/measure-instance-dialog.html',
                    controller: 'MeasureInstanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeasureInstance', function(MeasureInstance) {
                            return MeasureInstance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('measure-instance.new', {
            parent: 'measure-instance',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-instance/measure-instance-dialog.html',
                    controller: 'MeasureInstanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                instanceName: null,
                                instanceDescription: null,
                                measureName: null,
                                measureVersion: null,
                                isShedule: null,
                                shedulingExpression: null,
                                measureType: null,
                                manageLastMeasurement: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('measure-instance', null, { reload: 'measure-instance' });
                }, function() {
                    $state.go('measure-instance');
                });
            }]
        })
        .state('measure-instance.edit', {
            parent: 'measure-instance',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-instance/measure-instance-dialog.html',
                    controller: 'MeasureInstanceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeasureInstance', function(MeasureInstance) {
                            return MeasureInstance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('measure-instance', null, { reload: 'measure-instance' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('measure-instance.delete', {
            parent: 'measure-instance',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-instance/measure-instance-delete-dialog.html',
                    controller: 'MeasureInstanceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MeasureInstance', function(MeasureInstance) {
                            return MeasureInstance.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('measure-instance', null, { reload: 'measure-instance' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
