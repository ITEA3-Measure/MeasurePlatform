(function() {
	'use strict';

	angular.module('measurePlatformApp').config(stateConfig);

	stateConfig.$inject = [ '$stateProvider' ];

	function stateConfig($stateProvider) {
		$stateProvider.state('measure-log', {
			parent : 'app',
			url : '/measure/log',
			data : {
				authorities : []
			},
			views : {
				'content@' : {
					templateUrl : 'app/pages/logs/measure-log.html',
					controller : 'MeasureLog',
					controllerAs : 'vm'
				}
			}
		});
	}
})();
