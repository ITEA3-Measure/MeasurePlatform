(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('phase', {
            parent: 'entity',
            url: '/phase',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Phases'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/phase/phases.html',
                    controller: 'PhaseController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('phase-detail', {
            parent: 'entity',
            url: '/phase/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Phase'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/phase/phase-detail.html',
                    controller: 'PhaseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Phase', function($stateParams, Phase) {
                    return Phase.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'phase',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('phase-detail.edit', {
            parent: 'phase-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/phase/phase-dialog.html',
                    controller: 'PhaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Phase', function(Phase) {
                            return Phase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('phase.new', {
            parent: 'phase',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/phase/phase-dialog.html',
                    controller: 'PhaseDialogController',
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
                        }
                    }
                }).result.then(function() {
                    $state.go('phase', null, { reload: 'phase' });
                }, function() {
                    $state.go('phase');
                });
            }]
        })
        .state('phase.edit', {
            parent: 'phase',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/phase/phase-dialog.html',
                    controller: 'PhaseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Phase', function(Phase) {
                            return Phase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('phase', null, { reload: 'phase' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('phase.delete', {
            parent: 'phase',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/phase/phase-delete-dialog.html',
                    controller: 'PhaseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Phase', function(Phase) {
                            return Phase.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('phase', null, { reload: 'phase' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
