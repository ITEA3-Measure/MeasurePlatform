(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('projectphases', {
            parent: 'appproject',
            url: '/phase/:phaseId',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/phases/projectphase.html',
                    controller: 'ProjectPhasesController',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'Phase', function($stateParams, Phase) {
                    return Phase.get({id : $stateParams.phaseId}).$promise;
                }]
            }
        }).state('projectphases.edit', {
            parent: 'projectphases',
            url: '/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/phases/projectphase-dialog.html',
                    controller: 'ProjectPhaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Phase', function(Phase) {
                            return Phase.get({id : $stateParams.phaseId}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('projectphases.delete', {
            parent: 'projectphases',
            url: '/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/phases/projectphase-delete-dialog.html',
                    controller: 'ProjectPhaseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Phase', function(Phase) {
                            return Phase.get({id : $stateParams.phaseId}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('appproject', null, { reload: 'appproject' });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('projectphases.newdashboard', {
            parent: 'projectphases',
            url: '/phasedashboard/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/phases/dashboard/phasedashboard-dialog.html',
                    controller: 'PhaseDashboardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                    	 entity: function () {
                             return {
                                 dashboardName: null,
                                 dashboardDescription: null,
                                 content: null,
                                 isExternal:false,
                                 id: null
                             }
                    	 },
                        phase: ['Phase', function(Phase) {
                            return Phase.get({id : $stateParams.phaseId}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('projectphases.editdashboard', {
            parent: 'projectphases',
            url: '/phasedashboard/:dashboardid/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/phases/dashboard/phasedashboard-dialog.html',
                    controller: 'PhaseDashboardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                    	 entity: ['Dashboard', function(Dashboard) {
                             return Dashboard.get({id : $stateParams.dashboardid}).$promise;
                         }],
                         phase:null
                    }
                }).result.then(function() {
                    $state.go('projectphases', null, { reload: 'projectphases' });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('projectphases.deletedashboard', {
            parent: 'projectphases',
            url: '/phasedashboard/:dashboardid/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/phases/dashboard/phasedashboard-delete-dialog.html',
                    controller: 'PhaseDashboardDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Dashboard', function(Dashboard) {
                            return Dashboard.get({id : $stateParams.dashboardid}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projectphases', null, { reload: 'projectphases' });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('projectphases.addgraphic', {
            parent: 'projectphases',
            url: '/phasedashboard/:dashboardid/graphic',
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
	                            auto:true,
	                            interval:"Last Hour",
	                            custom:false
	                        };
	                    },
                    	 data: function () {
                             return {
                                 isOverview: false
                             };
                         },
                    	  project: null,
                          phase:null,
                          dashboard:['Dashboard', function(Dashboard) {
                              return Dashboard.get({id : $stateParams.dashboardid}).$promise;
                          }] 
                    }
                }).result.then(function() {
                    $state.go('projectphases', null, { reload: 'projectphases' });
                }, function() {
                    $state.go('projectphases');
                });
            }]
        }).state('projectphases.editgraphic', {
            parent: 'projectphases',
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
                                 isOverview: false
                             };
                         },
                         project:null,
                         phase:null,
                         dashboard:null 
                    }
                }).result.then(function() {
                    $state.go('projectphases', null, { reload: 'projectphases' });
                }, function() {
                    $state.go('projectphases');
                });
            }]
        });
    }
})();
