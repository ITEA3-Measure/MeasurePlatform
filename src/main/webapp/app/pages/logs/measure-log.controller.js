(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('MeasureLog',
			MeasureLog);

	MeasureLog.$inject = [ '$scope', 'Principal', 'MeasureLogService',
			'$state' ];

	function MeasureLog($scope, Principal, MeasureLogService, $state) {
		var vm = this;

		vm.logs = [];
		
		vm.kibanaframe =null;

		loadAll();
		
		loadProperties();
			
		function loadAll() {
			MeasureLogService.alllogs(function(result) {
				vm.logs = result;
			});
		}
		
		function loadProperties() {
			MeasureLogService.kibanaadress(function(result) {
				vm.kibanaframe =  "<iframe src=\"http://" +result.kibanaAdress + "/app/kibana#/visualize/create?embed=true&type=area&indexPattern=measure.*&_g=(refreshInterval:(display:Off,pause:!f,value:0),time:(from:now-60d,mode:quick,to:now))&_a=(filters:!(),linked:!f,query:(query_string:(analyze_wildcard:!t,query:'*')),uiState:(vis:(colors:(Count:%237EB26D),legendOpen:!f)),vis:(aggs:!((enabled:!t,id:'1',params:(),schema:metric,type:count),(enabled:!t,id:'2',params:(customInterval:'2h',customLabel:'Measure+Collected+Daly',extended_bounds:(),field:postDate,interval:d,min_doc_count:1),schema:segment,type:date_histogram)),listeners:(),params:(addLegend:!t,addTimeMarker:!t,addTooltip:!t,defaultYExtents:!f,interpolate:linear,legendPosition:right,mode:stacked,scale:linear,setYExtents:!f,shareYAxis:!t,smoothLines:!t,times:!(),yAxis:()),title:'New+Visualization',type:area))\" height=\"" +window.screen.width / 3 * 0.75+ "\" width=\"" +window.screen.width / 3+ "\" style=\"border: none;\"></iframe>";
			});
		}
		
		setInterval(function(){
			
			MeasureLogService.alllogs(function(result) {
				
				var lastmeasure = vm.logs[0].exectionDate;
				for(var i = result.length-1 ; i >= 0;i--){
					if(result[i].exectionDate > lastmeasure){
						vm.logs.splice(0, 0, result[i]);
					}
				}
			});
		   
		 }, 2000);
	}
})();
