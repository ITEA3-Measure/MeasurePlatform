(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'AppProjectDataSourcesController', AppProjectDataSourcesController);

	AppProjectDataSourcesController.$inject = [ '$location', '$scope',
			'Principal', 'LoginService', '$state', 'entity',
			'ProjectDataSources', 'MeasureAgentService', 'Notification' ,'ProjectAnalysis'];

	function AppProjectDataSourcesController($location, $scope, Principal,
			LoginService, $state, entity, ProjectDataSources,
			MeasureAgentService, Notification,ProjectAnalysis) {
		var vm = this;

		vm.project = entity;

		vm.measureInstances = [];
		loadInstances(vm.project.id);

		function loadInstances(id) {
			ProjectDataSources.instances({
				id : id
			}, function(result) {
				vm.measureInstances = result;
				for (var i = 0; i < vm.measureInstances.length; i++) {
					vm.measureInstances[i].properties = [];
					vm.measureInstances[i].ownedReferences = [];
					vm.measureInstances[i].showProperties = false;
					vm.measureInstances[i].showDependencies = false;

					loadReference(vm.measureInstances[i].id);
					loadProperties(vm.measureInstances[i].id);
					loadDefaultViewStatus(vm.measureInstances[i].id);
					isShedule(vm.measureInstances[i].id);
					if (vm.measureInstances[i].isRemote == true) {
						isAgentEnable(vm.measureInstances[i]);
					}

				}
			});
		}
		
		vm.showProperties = showProperties
		function showProperties(id, show) {
			for (var j = 0; j < vm.measureInstances.length; j++) {
				if (vm.measureInstances[j].id == id) {
					vm.measureInstances[j].showProperties = show;
				}
			}
		}
		
		vm.showDependencies = showDependencies
		function showDependencies(id, show) {
			for (var j = 0; j < vm.measureInstances.length; j++) {
				if (vm.measureInstances[j].id == id) {
					vm.measureInstances[j].showDependencies = show;
				}
			}
		}

		vm.agents = [];
		loadAgents();

		function loadAgents() {
			MeasureAgentService.allAgents(function(result) {
				vm.agents = result;
			});
		}

		vm.startSheduling = startSheduling
		vm.stopSheduling = stopSheduling

		function loadProperties(id) {
			ProjectDataSources
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
			ProjectDataSources.references({
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
		


		function isShedule(id) {
			ProjectDataSources.isShedule({
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
				if (vm.agents[i].agentName == measureInstance.remoteLabel) {
					measureInstance.agentEnable = true;
				}
			}
		}

		function isReferenceShedule(id, reference) {
			ProjectDataSources.isShedule({
				id : id
			}, function(result) {
				reference.status = result.data;
			});
		}

		function startSheduling(id) {
			ProjectDataSources
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
			ProjectDataSources
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

		vm.notifications = [];
		loadNotificationByProject(vm.project.id);

		function loadNotificationByProject(id) {
			Notification.notifications({
				id : id
			}, function(result) {
				vm.notifications = result;
			});
		}
		
		
		function loadDefaultViewStatus(id){
			ProjectDataSources.getDefaultVisualisation({
				id : id
			}, function(result) {
				for (var j = 0; j < vm.measureInstances.length; j++) {
					if (vm.measureInstances[j].id == id) {
						vm.measureInstances[j].defaultview = (result.data  != '');
					}
				}
			});
		}

		vm.createView = createView;
		function createView(id) {
			ProjectDataSources.createDefaultVisualisation(
			{
				id : id
			},
			function(result) {
				for (var j = 0; j < vm.measureInstances.length; j++) {
					if (vm.measureInstances[j].id == id) {
						vm.measureInstances[j].defaultview = (result.data  != '');
					}
				}
			});
		}
		
		
		vm.deleteView = deleteView; 
		function deleteView(id) {
			ProjectDataSources.deleteDefaultVisualisation(
			{
				id : id
			},
			function(result) {
				for (var j = 0; j < vm.measureInstances.length; j++) {
					if (vm.measureInstances[j].id == id) {
						vm.measureInstances[j].defaultview = false;
					}
				}
			});
		}
		
	}
})();
