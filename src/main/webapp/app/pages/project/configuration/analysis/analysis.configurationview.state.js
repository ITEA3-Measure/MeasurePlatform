(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
    	   $stateProvider.state('analysisconfigurationview', {
            parent: 'app',
            url: '/project/:id/configuration/analysis/:anid',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/configuration/analysis/analysis.configurationview.html',
                    controller: 'ProjectAnalysisConfiguration',
                    controllerAs: 'vm'
                }
            },resolve: {
                entity: ['$stateParams', 'ProjectAnalysis', function($stateParams, ProjectAnalysis) {
                    return ProjectAnalysis.get({id : $stateParams.anid}).$promise;
                }],project: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }]
            }
        });
    }
})();
