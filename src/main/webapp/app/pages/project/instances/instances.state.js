(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
    	   $stateProvider.state('homeinstances', {
               parent: 'app',
               url: '/instances',
               data: {
                   authorities: []
               },
               views: {
                   'content@': {
                       templateUrl: 'app/pages/project/instances/instances.html',
                       controller: 'AppProjectInstancesController',
                       controllerAs: 'vm'
                   }
               },resolve: {
                   entity: function () {
                       return {
                           id: null
                       };
                   }            
               }
           }).state('projectinstances', {
            parent: 'app',
            url: '/instances/:id',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/instances/instances.html',
                    controller: 'AppProjectInstancesController',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('projectinstances.new', {
            parent: 'projectinstances',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/instances/instance-dialog.html',
                    controller: 'ProjectInstanceDialogController',
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
                    $state.go('projectinstances', null, { reload: 'projectinstances' });
                }, function() {
                    $state.go('projectinstances');
                });
            }]
        }).state('projectinstances.delete', {
            parent: 'projectinstances',
            url: '/{instanceId}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/instances/instance-delete-dialog.html',
                    controller: 'ProjectInstanceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MeasureInstance', function(MeasureInstance) {
                            return MeasureInstance.get({id : $stateParams.instanceId}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projectinstances', null, { reload: 'projectinstances' });
                }, function() {
                    $state.go('^');
                });
            }]
        }) .state('projectinstances.edit', {
            parent: 'projectinstances',
            url: '/{instanceId}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/instances/instance-dialog.html',
                    controller: 'ProjectInstanceDialogController',
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
                    $state.go('projectinstances', null, { reload: 'projectinstances' });
                }, function() {
                    $state.go('^');
                });
            }]
        }) .state('projectinstances.test', {
            parent: 'projectinstances',
            url: '/{instanceId}/test',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/instances/instance-test-dialog.html',
                    controller: 'TestInstanceDialogController',
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
                    $state.go('projectinstances', null, { reload: 'projectinstances' });
                }, function() {
                    $state.go('^');
                });
            }]
        }) .state('projectinstances.execute', {
            parent: 'projectinstances',
            url: '/{instanceId}/execute',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/instances/instance-test-dialog.html',
                    controller: 'TestInstanceDialogController',
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
                    $state.go('projectinstances', null, { reload: 'projectinstances' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
