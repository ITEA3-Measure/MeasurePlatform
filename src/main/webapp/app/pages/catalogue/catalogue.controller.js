(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('CatalogueController',
			CatalogueController);

	CatalogueController.$inject = ['$scope', 'Principal', 'LoginService', '$state',
			'Catalogue', 'MeasureView' ,'MeasurementService'];

	function CatalogueController($scope, Principal, LoginService, $state, Catalogue,
			MeasureView,MeasurementService) {
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
									
									for(var j = 0; j < vm.projects[i].measureview.length; j++){
										if(vm.projects[i].measureview[j].mode == 'VALUE'){
											formatBasicViewData(vm.projects[i].measureview[j]);
											
											if(vm.projects[i].measureview[j].auto == true){
												vm.updatdeView.push(vm.projects[i].measureview[j]);
											}
										}else if(vm.projects[i].measureview[j].mode == 'TABLE'){
											formatBasicTableData(vm.projects[i].measureview[j]);
											
											if(vm.projects[i].measureview[j].auto == true){
												vm.updatdeView.push(vm.projects[i].measureview[j]);
											}
										}
										
										
									}
								}
							}
						}
					});
				}
			});
		}
		
		function formatBasicViewData(view) {
			var properties = view.visualisedProperty.split(",");
			if(properties.length == 1){
				view.displaymode = 'SINGLE';	
				MeasurementService.lastvalue({
					id : view.measureinstance.instanceName
				}, function(result) {
					var value = result.values[properties[0]];
					if(value.length == 24 && new Date(value) instanceof Date){
						view.datevalue = new Date(value);
					}else{
						view.value = value;
					}
				});
				
			}else{
				view.displaymode = 'MULTI';
				view.columns = properties;
				view.value = [];
				
				MeasurementService.lastvalue({
					id : view.measureinstance.instanceName
				}, function(result) {
					for(var i = 0 ; i < view.columns.length;i++){
						var col = new Object();
						
						var value = result.values[view.columns[i]];
						if(value.length == 24 && new Date(value) instanceof Date){
							col.datevalue = new Date(value);
						}else{
							col.value = value;
						}		
						var label = view.columns[i];
						col.label = label.substring(0,1).toUpperCase() + label.substring(1);				
						view.value.push(col);
					}
				});	
			}	
		}
		

		function formatBasicTableData(view) {
			var query = new Object();
			
			if(view.currentpage == null){
				view.currentpage = 1;
			}
			query.measureInstance=view.measureinstance.instanceName;
			query.page=view.currentpage - 1;
			query.pageSize=view.fontSize;
			query.query ='';
			
			if(query.pageSize == 0){
				query.pageSize = 10;
			}
		
			MeasurementService.find(query, function(result) {
				var properties = view.visualisedProperty.split(",");
				
				// Columns Labels
				view.columns = [];
				for(var i = 0;i < properties.length;i++){
					view.columns.push(properties[i].substring(0,1).toUpperCase() + properties[i].substring(1));
				}
				
				view.measurements = [];
				for(var i = 0;i < result.length;i++){
					var measurement = new Object();
					measurement.fields = [];				
					for(var j = 0;j < properties.length;j++){			
						var field = new Object();
						
						var value = result[i].values[properties[j]];
						if(value.length == 24 && new Date(value) instanceof Date){
							field.datevalue = new Date(value);
						}else{
							field.value = value;
						}	
						measurement.fields.push(field);			
					}
					view.measurements.push(measurement);
				}	
			});

		}
		
		vm.firstData = firstData;	
		function firstData(view){
			 view.currentpage = 1;
			 formatBasicTableData(view);
		}
		
		vm.previousData = previousData;
		function previousData(view){
			if(view.currentpage > 1){
				view.currentpage = view.currentpage - 1;
			}
			
			formatBasicTableData(view);
		}
		
		vm.nextData = nextData;
		function nextData(view){
			view.currentpage = view.currentpage + 1;
			formatBasicTableData(view);
		}	
		
		
		
		// Update View Data	
		vm.updatdeView = [];
		setInterval(function(){
			for(var i =0; i < vm.updatdeView.length; i++){
	    		if(vm.updatdeView[i].mode == 'VALUE'){
					formatBasicViewData(vm.updatdeView[i]);				
				}else if(vm.updatdeView[i].mode == 'TABLE'){
					if(vm.updatdeView[i].currentpage == 1){
						formatBasicTableData(vm.updatdeView[i]);
					}			
				}
	    	}
			
		   
		 }, 5000);

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
