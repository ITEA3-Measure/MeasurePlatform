(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('appproject', {
            parent: 'app',
            url: '/pages/project/:id',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/project.html',
                    controller: 'AppProjectController',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }]
            }
        }).state('appproject.edit', {
            parent: 'appproject',
            url: '/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/project-dialog.html',
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
                    $state.go('appproject', null, { reload: 'appproject' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appproject.delete', {
            parent: 'appproject',
            url: '/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/project-delete-dialog.html',
                    controller: 'AppProjectDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Project', function(Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('home', null, { reload: 'home' });
                }, function() {
                    $state.go('^');
                });
            }]
        }).state('appproject.newphase', {
            parent: 'appproject',
            url: '/newphase',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/projectphase-new-dialog.html',
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
                    $state.go('appproject', null, { reload: 'appproject' });
                }, function() {
                    $state.go('appproject');
                });
            }]
        }).state('appproject.addgraphic', {
            parent: 'appproject',
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
                    	  project: ['Project', function(Project) {
                              return Project.get({id : $stateParams.id}).$promise;
                          }],
                          phase:null,
                          dashboard:null 
                    }
                }).result.then(function() {
                    $state.go('appproject', null, { reload: 'appproject' });
                }, function() {
                    $state.go('appproject');
                });
            }]
        }).state('appproject.addphasegraphic', {
            parent: 'appproject',
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
	                            auto:true,
	                            interval:"Last Hour",
	                            custom:false
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
                    $state.go('appproject', null, { reload: 'appproject' });
                }, function() {
                    $state.go('appproject');
                });
            }]
        }).state('appproject.editgraphic', {
            parent: 'appproject',
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
                    $state.go('appproject', null, { reload: 'appproject' });
                }, function() {
                    $state.go('appproject');
                });
            }]
        });
    }
})();
