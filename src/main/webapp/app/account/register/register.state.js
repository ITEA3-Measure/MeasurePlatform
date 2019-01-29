(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('register', {
            parent: 'app',
            url: '/register',
            data: {
                authorities: [],
                pageTitle: 'Registration'
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/register/register.html',
                    controller: 'RegisterController',
                    controllerAs: 'vm'
                }
            }
        });
    }
})();
