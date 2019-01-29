(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('notification', {
            parent: 'app',
            url: '/project/:id/notification/',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/pages/project/notification/notification.html',
                    controller: 'NotificationController',
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
        });
    }
})();
