(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
    	   $stateProvider.state('analysismainview', {
            parent: 'app',
            url: '/project/:id/analysis',
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
               project: ['$stateParams', 'Project', function($stateParams, Project) {
                    return Project.get({id : $stateParams.id}).$promise;
                }],
                role : ['$stateParams', 'UsersRightAccessService', function($stateParams, UsersRightAccessService) {
                    return UsersRightAccessService.currentUserHasManagerRole({projectId : $stateParams.id}).$promise;
                }]
            }
        });
    }
})();
