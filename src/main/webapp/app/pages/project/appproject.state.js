(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('appproject', {
            parent: 'app',
            url: '/pages/project/:id',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/project.html',
                    controller: 'AppProjectController',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }]
            }
        }).state('appproject.edit', {
            parent: 'appproject',
            url: '/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/project-dialog.html',
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
                    $state.go('appproject', null, { reload: 'appproject' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appproject.delete', {
            parent: 'appproject',
            url: '/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/project-delete-dialog.html',
                    controller: 'AppProjectDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Project', function(Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: 'home' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
