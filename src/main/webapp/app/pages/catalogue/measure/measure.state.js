(function() {
	'use strict';

	angular.module('measurePlatformApp').config(stateConfig);

	stateConfig.$inject = [ '$stateProvider' ];

	function stateConfig($stateProvider) {
		$stateProvider.state('measure', {
			parent : 'app',
			url : '/measure',
			data : {
				authorities : []
			},
			views : {
				'content@' : {
					templateUrl : 'app/pages/catalogue/measure/measure.html',
					controller : 'MeasureController',
					controllerAs : 'vm'
				}
			}
		}).state('measure.upload', {
            parent: 'measure',
            url: '/upload',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/catalogue/measure/measure-upload-dialog.html',
                    controller: 'MeasureUploadController',
                    controllerAs: 'vm',
                    size: 'md'
                }).result.then(function() {
                    $state.go('measure', null, { reload: 'measure' });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('measure.delete', {
            parent: 'measure',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/catalogue/measure/measure-delete-dialog.html',
                    controller: 'MeasureDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Measure', function(Measure) {
                            return Measure.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('measure', null, { reload: 'measure' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
	}
})();
