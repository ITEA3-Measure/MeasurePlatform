(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
    	   $stateProvider.state('generalconf', {
            parent: 'app',
            url: '/project/:id/configuration/general',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/configuration/general/general.html',
                    controller: 'ProjectGeneralConfigurationController',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }]
            }
        }).state('generalconf.editproject', {
            parent: 'generalconf',
            url: 'editproject/',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/edition/project-dialog.html',
                    controller: 'AppProjectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Project', function(Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('generalconf', null, { reload: 'generalconf' });
                }, function() {
                    $state.go('generalconf');
                });
            }]
        })
        .state('generalconf.deleteproject', {
            parent: 'generalconf',
            url: 'deleteproject/',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/edition/project-delete-dialog.html',
                    controller: 'AppProjectDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Project', function(Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('catalogue', null, { reload: 'catalogue' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
