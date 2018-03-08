(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
    	   $stateProvider.state('analysismainview', {
            parent: 'app',
            url: '/project/:id/analysis/:anid',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/analysis/analysis.mainview.html',
                    controller: 'AnalysisMainView',
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
