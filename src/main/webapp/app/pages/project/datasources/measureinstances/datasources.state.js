(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
    	   $stateProvider.state('projectdatasources', {
            parent: 'app',
            url: '/project/:id/datasources',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/datasources/measureinstances/datasources.html',
                    controller: 'AppProjectDataSourcesController',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('projectdatasources.new', {
            parent: 'projectdatasources',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/datasources/measureinstances/datasource-dialog.html',
                    controller: 'ProjectDataSourceDialogController',
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
                                shedulingExpression: '3600000',
                                schedulingUnit: 'h',
                                measureType: null,
                                manageLastMeasurement: null,
                                id: null,
                                isRemote : null,
                                remoteLabel : null,
                                remoteAdress : null
                            };
                        },
                        project:['$stateParams', 'Project', function($stateParams, Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }] 
                    }
                }).result.then(function() {
                    $state.go('projectdatasources', null, { reload: 'projectdatasources' });
                }, function() {
                    $state.go('projectdatasources');
                });
            }]
        }).state('projectdatasources.delete', {
            parent: 'projectdatasources',
            url: '/{instanceId}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/datasources/measureinstances/datasource-delete-dialog.html',
                    controller: 'ProjectDataSourceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MeasureInstance', function(MeasureInstance) {
                            return MeasureInstance.get({id : $stateParams.instanceId}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projectdatasources', null, { reload: 'projectdatasources' });
                }, function() {
                    $state.go('^');
                });
            }]
        }) .state('projectdatasources.edit', {
            parent: 'projectdatasources',
            url: '/{instanceId}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/datasources/measureinstances/datasource-dialog.html',
                    controller: 'ProjectDataSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeasureInstance', function(MeasureInstance) {
                            return MeasureInstance.get({id : $stateParams.instanceId}).$promise;
                        }], project:['$stateParams', 'Project', function($stateParams, Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }] 
                    }
                }).result.then(function() {
                    $state.go('projectdatasources', null, { reload: 'projectdatasources' });
                }, function() {
                    $state.go('^');
                });
            }]
        }) .state('projectdatasources.test', {
            parent: 'projectdatasources',
            url: '/{instanceId}/test',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/datasources/measureinstances/datasource-test-dialog.html',
                    controller: 'TestDataSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeasureInstance', function(MeasureInstance) {
                            return MeasureInstance.get({id : $stateParams.instanceId}).$promise;
                        }],
                        isTest : true
                    }
                }).result.then(function() {
                    $state.go('projectdatasources', null, { reload: 'projectdatasources' });
                }, function() {
                    $state.go('^');
                });
            }]
        }) .state('projectdatasources.execute', {
            parent: 'projectdatasources',
            url: '/{instanceId}/execute',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/datasources/measureinstances/datasource-test-dialog.html',
                    controller: 'TestDataSourceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeasureInstance', function(MeasureInstance) {
                            return MeasureInstance.get({id : $stateParams.instanceId}).$promise;
                        }],
                        isTest : false
                    }
                }).result.then(function() {
                    $state.go('projectdatasources', null, { reload: 'projectdatasources' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
