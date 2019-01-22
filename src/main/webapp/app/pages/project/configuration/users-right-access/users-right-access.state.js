(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
    	$stateProvider.state('users-rightaccess', {
            parent: 'app',
            url: '/project/:id/configuration/users-right-access',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/configuration/users-right-access/users-right-access.html',
                    controller: 'UsersRightAccessController',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('users-rightaccess.invite', {
            parent: 'users-rightaccess',
            url: '/invite',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                	templateUrl: 'app/pages/project/configuration/users-right-access/users-right-access-dialog.html',
                    controller: 'UsersRightAccessDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        project:['$stateParams', 'Project', function($stateParams, Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('users-rightaccess', null, { reload: 'users-rightaccess' });
                }, function() {
                    $state.go('users-rightaccess');
                });
            }]
        })
        .state('users-rightaccess.delete', {
            parent: 'users-rightaccess',
            url: '/:userId/delete',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                	templateUrl: 'app/pages/project/configuration/users-right-access/users-right-access-delete-dialog.html',
                    controller: 'UsersRightAccessDeleteDialogController',
                    controllerAs: 'vm',
                    size: 'md'
                }).result.then(function() {
                    $state.go('users-rightaccess', null, { reload: 'users-rightaccess' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
