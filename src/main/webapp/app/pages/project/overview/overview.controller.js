(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('AppProjectController',
			AppProjectController);

	AppProjectController.$inject = [ '$scope','$cookies', 'Principal', 'LoginService',
			'$state', 'entity','role','Notification', 'MeasureView','Dashboard','ProjectAnalysis', 'UsersRightAccessService','MeasurementService'];

	function AppProjectController($scope,$cookies, Principal, LoginService, $state,
			entity,role,Notification, MeasureView,Dashboard,ProjectAnalysis, UsersRightAccessService,MeasurementService) {
		var vm = this;
		vm.project = entity;
		vm.hasManagerRole = null;		
		vm.dashboards = [];
		vm.selectedDashboard = 1;
		vm.hasManagerRole = role.data;

		loadAllDashBoard(vm.project.id);
		function loadAllDashBoard(id) {
			Dashboard.dashboards({
				id : id
			}, function(result) {
				vm.dashboards = result;
				vm.dashboards.sort((a,b) => (a.id > b.id) ? 1 : ((b.id > a.id) ? -1 : 0));
				for (var i = 0; i < vm.dashboards.length; i++) {
					MeasureView.bydashboard({
						id : vm.dashboards[i].id
					}, function(result) {
						if(result.length > 0){
							for (var i = 0; i < vm.dashboards.length; i++) {
								if(vm.dashboards[i].id == result[0].dashboard.id){
									vm.dashboards[i].measureview = result;
									for(var j = 0; j < vm.dashboards[i].measureview.length; j++){
										if(vm.dashboards[i].measureview[j].mode == 'VALUE'){
											formatBasicViewData(vm.dashboards[i].measureview[j]);
											
											if(vm.dashboards[i].measureview[j].auto == true){
												vm.updatdeView.push(vm.dashboards[i].measureview[j]);
											}
										}else if(vm.dashboards[i].measureview[j].mode == 'TABLE'){
											formatBasicTableData(vm.dashboards[i].measureview[j]);
											
											if(vm.dashboards[i].measureview[j].auto == true){
												vm.updatdeView.push(vm.dashboards[i].measureview[j]);
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
					if(value != undefined){
						if(value.length == 24 && new Date(value) instanceof Date){
							view.datevalue = new Date(value);
						}else{
							view.value = value;
						}
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
						if(value != undefined){
							if(value.length == 24 && new Date(value) instanceof Date){
								col.datevalue = new Date(value);
							}else{
								col.value = value;
							}		
							var label = view.columns[i];
							col.label = label.substring(0,1).toUpperCase() + label.substring(1);				
							view.value.push(col);
						}
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
			query.page=view.currentpage;
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
						if(value != undefined){
							if(value.length == 24 && new Date(value) instanceof Date){
								field.datevalue = new Date(value);
							}else{
								field.value = value;
							}	
							measurement.fields.push(field);	
						}
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


		vm.isActive = isActive;

		function isActive(idx) {
			if (idx == vm.selectedDashboard) {
				return 'active';
			}
			return '';
		}

		vm.setActive = setActive;

		// Tab Management
		vm.selectedDashboard = $cookies.get("selectedDashboard");
		
		function setActive(idx) {
			vm.selectedDashboard = idx;
			$cookies.put("selectedDashboard",idx);
			vm.editdashboard = false;
		}
		
		vm.editdashboard = false;
		vm.edition = edition;
		
		function edition(){
			if(vm.editdashboard){
				vm.editdashboard  = false;
			}else{
				vm.editdashboard  = true;
			}
		}
		
		vm.deletegraphic = deletegraphic;
		function deletegraphic(id) {
			 MeasureView.delete({id: id});
			 $state.go('projectoverview', null, { reload: 'projectoverview' });
		}
		
		vm.viewBlockStyle = viewBlockStyle;
		
		function viewBlockStyle(measureview){
			if(measureview.mode == 'AUTO' || measureview.mode == 'KVIS' || measureview.mode == 'CARD' ){
				return "display: inline-block;";
			}
			return "";			
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
	}
	
	angular.module('measurePlatformApp').filter('to_trusted',
			[ '$sce', function($sce) {
				return function(text) {
					return $sce.trustAsHtml(text);
				};
			} ]);
	
})();
