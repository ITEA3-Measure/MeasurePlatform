(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
    	   $stateProvider.state('applicationinstances', {
            parent: 'app',
            url: '/project/:id/datasources/applicationinstances',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/datasources/applicationinstances/applicationinstances.html',
                    controller: 'ApplicationInstancesController',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('applicationinstances.new', {
            parent: 'applicationinstances',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/datasources/applicationinstances/applicationinstances-dialog.html',
                    controller: 'ProjectApplicationInstancesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                applicationType: null,
                                description: null,
                                id: null,
                                enable : null
                            };
                        },
                        project:['$stateParams', 'Project', function($stateParams, Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }] 
                    }
                }).result.then(function() {
                    $state.go('applicationinstances', null, { reload: 'applicationinstances' });
                }, function() {
                    $state.go('applicationinstances');
                });
            }]
        })
//        .state('projectdatasources.delete', {
//            parent: 'projectdatasources',
//            url: '/{instanceId}/delete',
//            data: {
//                authorities: ['ROLE_USER']
//            },
//            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
//                $uibModal.open({
//                    templateUrl: 'app/pages/project/datasources/datasource-delete-dialog.html',
//                    controller: 'ProjectDataSourceDeleteController',
//                    controllerAs: 'vm',
//                    size: 'md',
//                    resolve: {
//                        entity: ['MeasureInstance', function(MeasureInstance) {
//                            return MeasureInstance.get({id : $stateParams.instanceId}).$promise;
//                        }]
//                    }
//                }).result.then(function() {
//                    $state.go('projectdatasources', null, { reload: 'projectdatasources' });
//                }, function() {
//                    $state.go('^');
//                });
//            }]
//        }) .state('projectdatasources.edit', {
//            parent: 'projectdatasources',
//            url: '/{instanceId}/edit',
//            data: {
//                authorities: ['ROLE_USER']
//            },
//            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
//                $uibModal.open({
//                    templateUrl: 'app/pages/project/datasources/datasource-dialog.html',
//                    controller: 'ProjectDataSourceDialogController',
//                    controllerAs: 'vm',
//                    backdrop: 'static',
//                    size: 'lg',
//                    resolve: {
//                        entity: ['MeasureInstance', function(MeasureInstance) {
//                            return MeasureInstance.get({id : $stateParams.instanceId}).$promise;
//                        }], project:['$stateParams', 'Project', function($stateParams, Project) {
//                            return Project.get({id : $stateParams.id}).$promise;
//                        }] 
//                    }
//                }).result.then(function() {
//                    $state.go('projectdatasources', null, { reload: 'projectdatasources' });
//                }, function() {
//                    $state.go('^');
//                });
//            }]
//        }) .state('projectdatasources.test', {
//            parent: 'projectdatasources',
//            url: '/{instanceId}/test',
//            data: {
//                authorities: ['ROLE_USER']
//            },
//            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
//                $uibModal.open({
//                    templateUrl: 'app/pages/project/datasources/datasource-test-dialog.html',
//                    controller: 'TestDataSourceDialogController',
//                    controllerAs: 'vm',
//                    backdrop: 'static',
//                    size: 'lg',
//                    resolve: {
//                        entity: ['MeasureInstance', function(MeasureInstance) {
//                            return MeasureInstance.get({id : $stateParams.instanceId}).$promise;
//                        }],
//                        isTest : true
//                    }
//                }).result.then(function() {
//                    $state.go('projectdatasources', null, { reload: 'projectdatasources' });
//                }, function() {
//                    $state.go('^');
//                });
//            }]
//        }) .state('projectdatasources.execute', {
//            parent: 'projectdatasources',
//            url: '/{instanceId}/execute',
//            data: {
//                authorities: ['ROLE_USER']
//            },
//            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
//                $uibModal.open({
//                    templateUrl: 'app/pages/project/datasources/datasource-test-dialog.html',
//                    controller: 'TestDataSourceDialogController',
//                    controllerAs: 'vm',
//                    backdrop: 'static',
//                    size: 'lg',
//                    resolve: {
//                        entity: ['MeasureInstance', function(MeasureInstance) {
//                            return MeasureInstance.get({id : $stateParams.instanceId}).$promise;
//                        }],
//                        isTest : false
//                    }
//                }).result.then(function() {
//                    $state.go('projectdatasources', null, { reload: 'projectdatasources' });
//                }, function() {
//                    $state.go('^');
//                });
//            }]
//        })
        ;
    }
})();
