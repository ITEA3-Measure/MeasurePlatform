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
                         entity: null
                        ,project:['$stateParams', 'Project', function($stateParams, Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }] ,
                        param : function () {
                            return {
                                mode: "create",
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('applicationinstances', null, { reload: 'applicationinstances' });
                }, function() {
                    $state.go('applicationinstances');
                });
            }]
        })
        .state('applicationinstances.delete', {
            parent: 'applicationinstances',
            url: '/{instanceId}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/datasources/applicationinstances/applicationinstances-delete-dialog.html',
                    controller: 'ProjectApplicationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ApplicationInstances', function(ApplicationInstances) {
                            return ApplicationInstances.get({id : $stateParams.instanceId}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('applicationinstances', null, { reload: 'applicationinstances' });
                }, function() {
                    $state.go('^');
                });
            }]
        }) .state('applicationinstances.edit', {
            parent: 'applicationinstances',
            url: '/{instanceId}/edit',
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
                        entity: ['ApplicationInstances', function(ApplicationInstances) {
                            return ApplicationInstances.get({id : $stateParams.instanceId}).$promise;
                        }], project:['$stateParams', 'Project', function($stateParams, Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }],
                        param : function () {
                            return {
                                mode: "edite",
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('applicationinstances', null, { reload: 'applicationinstances' });
                }, function() {
                    $state.go('^');
                });
            }]
        }) 
        ;
    }
})();
