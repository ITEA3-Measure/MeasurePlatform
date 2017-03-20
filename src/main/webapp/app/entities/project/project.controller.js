(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('ProjectController', ProjectController);

    ProjectController.$inject = ['$scope', '$state', 'Project'];

    function ProjectController ($scope, $state, Project) {
        var vm = this;
        
        vm.projects = [];

        loadAll();

        function loadAll() {
            Project.query(function(result) {
                vm.projects = result;
            });
        }
    }
})();
