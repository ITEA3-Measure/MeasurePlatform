(function() {
	'use strict';

	angular.module('measurePlatformApp').config(stateConfig);

	stateConfig.$inject = [ '$stateProvider' ];

	function stateConfig($stateProvider) {
		$stateProvider.state('application', {
			parent : 'app',
			url : '/application',
			data : {
				authorities : []
			},
			views : {
				'content@' : {
					templateUrl : 'app/pages/catalogue/application/application.html',
					controller : 'ApplicationController',
					controllerAs : 'vm'
				}
			}
		})
		.state('application.upload', {
            parent: 'application',
            url: '/upload',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/catalogue/application/application-upload-dialog.html',
                    controller: 'ApplicationUploadController',
                    controllerAs: 'vm',
                    size: 'md'
                }).result.then(function() {
                    $state.go('application', null, { reload: 'application' });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('application.delete', {
            parent: 'application',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/catalogue/application/application-delete-dialog.html',
                    controller: 'ApplicationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Application', function(Application) {
                            return Application.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('application', null, { reload: 'application' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        ;
	}
})();
