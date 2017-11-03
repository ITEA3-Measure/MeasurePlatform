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
            url: '/graphic/phase/:phaseid',
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
	                            mode:'AUTO',
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
                    	  project: null,
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
        }).state('phasesoverview.editproject', {
            parent: 'phasesoverview',
            url: 'editproject/',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/edition/project-dialog.html',
                    controller: 'AppProjectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Project', function(Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('phasesoverview', null, { reload: 'phasesoverview' });
                }, function() {
                    $state.go('phasesoverview');
                });
            }]
        })
        .state('phasesoverview.deleteproject', {
            parent: 'phasesoverview',
            url: 'deleteproject/',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/edition/project-delete-dialog.html',
                    controller: 'AppProjectDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Project', function(Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('catalogue', null, { reload: 'catalogue' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
