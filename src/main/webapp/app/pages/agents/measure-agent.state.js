(function() {
	'use strict';

	angular.module('measurePlatformApp').config(stateConfig);

	stateConfig.$inject = [ '$stateProvider' ];

	function stateConfig($stateProvider) {
		$stateProvider.state('measure-agent', {
			parent : 'app',
			url : '/measure/agents',
			data : {
				authorities : []
			},
			views : {
				'content@' : {
					templateUrl : 'app/pages/agents/measure-agent.html',
					controller : 'MeasureAgent',
					controllerAs : 'vm'
				}
			}
		});
	}
})();
