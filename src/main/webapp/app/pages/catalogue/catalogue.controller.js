(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('CatalogueController',
			CatalogueController);

	CatalogueController.$inject = ['$scope', 'Principal', 'LoginService', '$state',
			'Catalogue', 'MeasureView' ];

	function CatalogueController($scope, Principal, LoginService, $state, Catalogue,
			MeasureView) {
		var vm = this;

		vm.account = null;
		vm.isAuthenticated = null;
		vm.login = LoginService.open;
		vm.register = register;
		vm.projects = null;

		loadAll();

		function loadAll() {
			Catalogue.projects(function(result) {
				vm.projects = result;
				for (var i = 0; i < vm.projects.length; i++) {
					MeasureView.byprojectoverview({
						id : vm.projects[i].id
					}, function(result) {
						if(result.length > 0){
							for (var i = 0; i < vm.projects.length; i++) {
								if(vm.projects[i].id == result[0].projectoverview.id){
									vm.projects[i].measureview = result;
								}
							}
						}
					});
				}
			});
		}

		$scope.$on('authenticationSuccess', function() {
			getAccount();
			loadAll();
		});

		getAccount();

		function getAccount() {
			Principal.identity().then(function(account) {
				vm.account = account;
				vm.isAuthenticated = Principal.isAuthenticated;
			});
		}
		function register() {
			$state.go('register');
		}

		vm.edition = edition;
		vm.editprojects = [];

		function edition(id) {
			if (vm.editprojects[id]) {
				vm.editprojects[id] = false;
			} else {
				vm.editprojects[id] = true;
			}
		}
		
		vm.deleteview = deleteview;
		function deleteview(id) {
			 MeasureView.delete({id: id});
			 $state.go('catalogue', null, { reload: 'catalogue' });
		}
	}
})();
