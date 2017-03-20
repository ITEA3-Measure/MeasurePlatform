(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'AppProjectInstancesController', AppProjectInstancesController);

	AppProjectInstancesController.$inject = [ '$location', '$scope',
			'Principal', 'LoginService', '$state', 'entity', 'Home',
			'ProjectInstances', 'MeasureAgentService' ];

	function AppProjectInstancesController($location, $scope, Principal,
			LoginService, $state, entity, Home, ProjectInstances,
			MeasureAgentService) {
		var vm = this;

		vm.projects = [];
		vm.project = null;

		loadProjects();

		function loadProjects() {
			Home.projects(function(result) {
				vm.projects = result;
				if (entity.id != null) {
					for (var i = 0; i < vm.projects.length; i++) {
						if (vm.projects[i].id == entity.id) {
							vm.project = vm.projects[i];
						}
					}
					loadAll(vm.project.id);
				} else if (vm.projects.length > 0) {
					vm.project = vm.projects[0];
				}
			});
		}

		vm.agents = [];

		loadAgents();

		function loadAgents() {
			MeasureAgentService.allAgents(function(result) {
				vm.agents = result;
			});
		}

		$scope.$watch("vm.project", function(newValue, oldValue) {
			if (newValue != null && newValue.id != null
					&& (oldValue == null || newValue.id != oldValue.id)) {
				$location.path("/instances/" + newValue.id);
			}
		});

		vm.measureInstances = [];

		vm.startSheduling = startSheduling
		vm.stopSheduling = stopSheduling

		function loadProperties(id) {
			ProjectInstances
					.properties(
							{
								id : id
							},
							function(result) {
								for (var i = 0; i < result.length; i++) {
									for (var j = 0; j < vm.measureInstances.length; j++) {
										if (vm.measureInstances[j].id == result[i].measureInstance.id) {
											vm.measureInstances[j].properties
													.push(result[i]);
										}
									}
								}
							});
		}

		function loadReference(id) {
			ProjectInstances.references({
				id : id
			}, function(result) {
				for (var i = 0; i < result.length; i++) {
					for (var j = 0; j < vm.measureInstances.length; j++) {

						if (vm.measureInstances[j].id == id) {
							vm.measureInstances[j].ownedReferences
									.push(result[i]);
							isReferenceShedule(result[i].referencedInstance.id,
									result[i]);
						}
					}
				}
			});
		}

		function loadAll(id) {
			ProjectInstances.instances({
				id : id
			}, function(result) {
				vm.measureInstances = result;
				for (var i = 0; i < vm.measureInstances.length; i++) {
					vm.measureInstances[i].properties = [];
					vm.measureInstances[i].ownedReferences = [];

					loadReference(vm.measureInstances[i].id);
					loadProperties(vm.measureInstances[i].id);
					isShedule(vm.measureInstances[i].id);
					if (vm.measureInstances[i].isRemote == true) {
						isAgentEnable(vm.measureInstances[i]);
					}

				}
			});
		}

		function isShedule(id) {
			ProjectInstances.isShedule({
				id : id
			}, function(result) {
				for (var i = 0; i < vm.measureInstances.length; i++) {
					if (vm.measureInstances[i].id == id) {
						vm.measureInstances[i].status = result.data;
					}
				}
			});
		}

		function isAgentEnable(measureInstance) {
			measureInstance.agentEnable = false;
			for (var i = 0; i < vm.agents.length; i++) {
				if (vm.agents[i].agentAdress == measureInstance.remoteAdress) {
					measureInstance.agentEnable = true;
				}
			}
		}

		function isReferenceShedule(id, reference) {
			ProjectInstances.isShedule({
				id : id
			}, function(result) {
				reference.status = result.data;
			});
		}

		function startSheduling(id) {
			ProjectInstances
					.startSheduling(
							{
								id : id
							},
							function(result) {
								for (var i = 0; i < vm.measureInstances.length; i++) {
									if (vm.measureInstances[i].id == id) {
										vm.measureInstances[i].status = result.data;
									}

									for (var j = 0; j < vm.measureInstances[i].ownedReferences.length; j++) {
										if (vm.measureInstances[i].ownedReferences[j].referencedInstance.id == id) {
											vm.measureInstances[i].ownedReferences[j].status = result.data;
										}
									}
								}
							});

		}

		function stopSheduling(id) {
			ProjectInstances
					.stopSheduling(
							{
								id : id
							},
							function(result) {
								for (var i = 0; i < vm.measureInstances.length; i++) {
									if (vm.measureInstances[i].id == id) {
										vm.measureInstances[i].status = result.data;
									}

									for (var j = 0; j < vm.measureInstances[i].ownedReferences.length; j++) {
										if (vm.measureInstances[i].ownedReferences[j].referencedInstance.id == id) {
											vm.measureInstances[i].ownedReferences[j].status = result.data;
											isShedule(vm.measureInstances[i].id);
										}
									}
								}
							});
		}
	}
})();
