(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('PhaseController', PhaseController);

    PhaseController.$inject = ['$scope', '$state', 'Phase'];

    function PhaseController ($scope, $state, Phase) {
        var vm = this;
        
        vm.phases = [];

        loadAll();

        function loadAll() {
            Phase.query(function(result) {
                vm.phases = result;
            });
        }
    }
})();
