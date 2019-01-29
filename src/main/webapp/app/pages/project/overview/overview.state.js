(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('projectoverview', {
            parent: 'app',
            url: '/project/:id/overview',
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
                }],
               role : ['$stateParams', 'UsersRightAccessService', function($stateParams, UsersRightAccessService) {
                   return UsersRightAccessService.currentUserHasManagerRole({projectId : $stateParams.id}).$promise;
               }]
            }
        }).state('projectoverview.newdashboard', {
            parent: 'projectoverview',
            url: '/dashboard/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/overview/dashboard/phasedashboard-dialog.html',
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
                                 kibanaId:null,
                                 auto:false,
                                 mode:'MANUAL',
                                 id: null
                             }
                    	 },
                        project: ['Project', function(Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('projectoverview.editdashboard', {
            parent: 'projectoverview',
            url: '/dashboard/:dashboardid/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/overview/dashboard/phasedashboard-dialog.html',
                    controller: 'PhaseDashboardDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                    	 entity: ['Dashboard', function(Dashboard) {
                             return Dashboard.get({id : $stateParams.dashboardid}).$promise;
                         }],
                         project:null
                    }
                }).result.then(function() {
                    $state.go('projectoverview', null, { reload: 'projectoverview' });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('projectoverview.deletedashboard', {
            parent: 'projectoverview',
            url: '/dashboard/:dashboardid/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/overview/dashboard/phasedashboard-delete-dialog.html',
                    controller: 'PhaseDashboardDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Dashboard', function(Dashboard) {
                            return Dashboard.get({id : $stateParams.dashboardid}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projectoverview', null, { reload: 'projectoverview' });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('projectoverview.addgraphic', {
            parent: 'projectoverview',
            url: '/dashboard/:dashboardid/graphic/:mode',
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
	                            interval:"Last Hour",
	                            mode:$stateParams.mode,
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
                         project:['$stateParams', 'Project', function($stateParams, Project) {
                             return Project.get({id : $stateParams.id}).$promise;
                         }],
                         dashboard:['Dashboard', function(Dashboard) {
                             return Dashboard.get({id : $stateParams.dashboardid}).$promise;
                         }] 
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
                                 isOverview: false
                             };
                         },
                         project:['$stateParams', 'Project', function($stateParams, Project) {
                             return Project.get({id : $stateParams.id}).$promise;
                         }],
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
