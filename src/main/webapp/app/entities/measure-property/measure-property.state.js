(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('measure-property', {
            parent: 'entity',
            url: '/measure-property',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MeasureProperties'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/measure-property/measure-properties.html',
                    controller: 'MeasurePropertyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('measure-property-detail', {
            parent: 'entity',
            url: '/measure-property/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MeasureProperty'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/measure-property/measure-property-detail.html',
                    controller: 'MeasurePropertyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MeasureProperty', function($stateParams, MeasureProperty) {
                    return MeasureProperty.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'measure-property',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('measure-property-detail.edit', {
            parent: 'measure-property-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-property/measure-property-dialog.html',
                    controller: 'MeasurePropertyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeasureProperty', function(MeasureProperty) {
                            return MeasureProperty.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('measure-property.new', {
            parent: 'measure-property',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-property/measure-property-dialog.html',
                    controller: 'MeasurePropertyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                propertyName: null,
                                propertyValue: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('measure-property', null, { reload: 'measure-property' });
                }, function() {
                    $state.go('measure-property');
                });
            }]
        })
        .state('measure-property.edit', {
            parent: 'measure-property',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-property/measure-property-dialog.html',
                    controller: 'MeasurePropertyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeasureProperty', function(MeasureProperty) {
                            return MeasureProperty.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('measure-property', null, { reload: 'measure-property' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('measure-property.delete', {
            parent: 'measure-property',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/measure-property/measure-property-delete-dialog.html',
                    controller: 'MeasurePropertyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MeasureProperty', function(MeasureProperty) {
                            return MeasureProperty.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('measure-property', null, { reload: 'measure-property' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
