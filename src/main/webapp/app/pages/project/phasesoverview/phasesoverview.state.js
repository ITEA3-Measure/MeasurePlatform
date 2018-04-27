(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('phasesoverview', {
            parent: 'app',
            url: '/project/:id/phases',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/phasesoverview/phasesoverview.html',
                    controller: 'PhaseOverviewController',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }]
            }
        }).state('phasesoverview.newphase', {
            parent: 'phasesoverview',
            url: '/newphase',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/phasesoverview/projectphase-new-dialog.html',
                    controller: 'ProjectPhaseNewDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                phaseName: null,
                                phaseDescription: null,
                                phaseImage: null,
                                id: null
                            };
                        },project:['$stateParams', 'Project', function($stateParams, Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }] 
                    }
                }).result.then(function() {
                    $state.go('phasesoverview', null, { reload: 'phasesoverview' });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('phasesoverview.addphasegraphic', {
            parent: 'phasesoverview',
            url: '/graphic/phase/:phaseid/:mode',
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
	                            timeAgregation:"M",
	                            mode:$stateParams.mode,
	                            visualisedProperty:null,
	                            dateIndex:null,
	                            color:'Blue',
	                            kibanaName:null
	                        };
	                    },
                    	 data: function () {
                             return {
                                 isOverview: true
                             };
                         },
                         project:['$stateParams', 'Project', function($stateParams, Project) {
                             return Project.get({id : $stateParams.id}).$promise;
                         }],
                         phase:['Phase', function(Phase) {
                              return Phase.get({id : $stateParams.phaseid}).$promise;
                         }],
                         dashboard:null 
                    }
                }).result.then(function() {
                    $state.go('phasesoverview', null, { reload: 'phasesoverview' });
                }, function() {
                    $state.go('phasesoverview');
                });
            }]
        }).state('phasesoverview.editgraphic', {
            parent: 'phasesoverview',
            url: '/graphic/:projectid/phase/:phaseid/edit/:graphicid',
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
                              return MeasureView.get({id : $stateParams.graphicid}).$promise;
                          }],
                    	 data: function () {
                             return {
                                 isOverview: true
                             };
                         },
                         project: ['Project', function(Project) {
                             return Project.get({id : $stateParams.projectid}).$promise;
                         }],
                         phase:['Phase', function(Phase) {
                             return Phase.get({id : $stateParams.phaseid}).$promise;
                         }],
                         dashboard:null 
                    }
                }).result.then(function() {
                    $state.go('phasesoverview', null, { reload: 'phasesoverview' });
                }, function() {
                    $state.go('phasesoverview');
                });
            }]
        });
    }
})();
