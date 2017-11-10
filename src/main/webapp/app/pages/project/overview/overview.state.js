(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('projectoverview', {
            parent: 'app',
            url: '/project/:id/overview/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/overview/overview.html',
                    controller: 'AppProjectController',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }]
            }
        }).state('projectoverview.addgraphic', {
            parent: 'projectoverview',
            url: '/graphic',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/graphics/graphic-dialog.html',
                    controller: 'GraphicDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {   
                    	entity: function () {
	                        return {
	                            viewData: null,
	                            id: null,
	                            name:null,
	                            description:null,
	                            size:"Medium",
	                            type:"Line chart",
	                            auto:false,
	                            timePeriode:"from:now-1y,mode:quick,to:now",
	                            timeAgregation:"1M",
	                            mode:'AUTO',
	                            visualisedProperty:null,
	                            dateIndex:null,
	                            color:'Blue',
	                            kibanaName:null
	                        };
	                    },
                    	 data: function () {
                             return {
                                 isOverview: false
                             };
                         },
                    	  project: ['Project', function(Project) {
                              return Project.get({id : $stateParams.id}).$promise;
                          }],
                          phase:null,
                          dashboard:null 
                    }
                }).result.then(function() {
                    $state.go('projectoverview', null, { reload: 'projectoverview' });
                }, function() {
                    $state.go('projectoverview');
                });
            }]
        }).state('projectoverview.editgraphic', {
            parent: 'projectoverview',
            url: '/graphic/:viewid/edit/',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/graphics/graphic-dialog.html',
                    controller: 'GraphicDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {   
                    	  entity: ['MeasureView', function(MeasureView) {
                              return MeasureView.get({id : $stateParams.viewid}).$promise;
                          }],
                    	 data: function () {
                             return {
                                 isOverview: true
                             };
                         },
                      	  project: ['Project', function(Project) {
                              return Project.get({id : $stateParams.id}).$promise;
                          }],
                         phase:null,
                         dashboard:null 
                    }
                }).result.then(function() {
                    $state.go('projectoverview', null, { reload: 'projectoverview' });
                }, function() {
                    $state.go('projectoverview');
                });
            }]
        });
    }
})();
