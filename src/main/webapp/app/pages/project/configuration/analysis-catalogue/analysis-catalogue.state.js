(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
    	   $stateProvider.state('projectanalysis-catalogue', {
            parent: 'app',
            url: '/project/:id/configuration/projectanalysis-catalogue',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/configuration/analysis-catalogue/analysis-catalogue.html',
                    controller: 'ProjectAnalysisConfigurationController',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('projectanalysis-catalogue.addanalysis', {
            parent: 'projectanalysis-catalogue',
            url: 'addanalysistool/',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/configuration/analysis-catalogue/add-analysis-dialog.html',
                    controller: 'AddAnalysisToolDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Project', function(Project) {
                            return Project.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projectanalysis-catalogue', null, { reload: 'projectanalysis-catalogue' });
                }, function() {
                    $state.go('projectanalysis-catalogue');
                });
            }]
        }).state('projectanalysis-catalogue.deleteanalysis', {
            parent: 'projectanalysis-catalogue',
            url: '/deleteanalysis/:anid',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/pages/project/configuration/analysis-catalogue/analysis-delete-dialog.html',
                    controller: 'AnalysisDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectAnalysis', function(ProjectAnalysis) {
                            return ProjectAnalysis.get({id : $stateParams.anid}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('projectanalysis-catalogue', null, { reload: 'projectanalysis-catalogue' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
