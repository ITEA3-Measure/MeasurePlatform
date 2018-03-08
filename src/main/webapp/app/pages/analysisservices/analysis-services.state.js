(function() {
	'use strict';

	angular.module('measurePlatformApp').config(stateConfig);

	stateConfig.$inject = [ '$stateProvider' ];

	function stateConfig($stateProvider) { 
		$stateProvider.state('analysis-services', {
			parent : 'app',
			url : '/measure/analysisservices',
			data : {
				authorities : []
			},
			views : {
				'content@' : {
					templateUrl : 'app/pages/analysisservices/analysis-services.html',
					controller : 'AnalysisServices',
					controllerAs : 'vm'
				}
			}
		})
		.state('analysis-services.configure', {
	        parent: 'app',
	        url: '/measure/analysisservices/:id/configure',
	        data: {
	            authorities: ['ROLE_USER']
	        },
	        onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
	            $uibModal.open({
	                templateUrl: 'app/pages/analysisservices/analysis-services-configuration-dialog.html',
	                controller: 'AnalysisServicesConfigurationDialogController',
	                controllerAs: 'vm',
	                backdrop: 'static',
	                size: 'lg',
	                resolve: {
	                    entity: ['AnalysisServicesService', function(AnalysisServicesService) {
	                        return AnalysisServicesService.findByName({id : $stateParams.id}).$promise;
	                    }]
	                }
	            }).result.then(function() {
	                $state.go('analysis-services', null, { reload: 'analysis-services' });
	            }, function() {
	            	$state.go('analysis-services');
	            });
	        }]
    });
	}
})();
