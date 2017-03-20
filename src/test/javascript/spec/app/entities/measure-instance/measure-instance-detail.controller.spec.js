'use strict';

describe('Controller Tests', function() {

    describe('MeasureInstance Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMeasureInstance, MockProject, MockMeasureProperty;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMeasureInstance = jasmine.createSpy('MockMeasureInstance');
            MockProject = jasmine.createSpy('MockProject');
            MockMeasureProperty = jasmine.createSpy('MockMeasureProperty');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MeasureInstance': MockMeasureInstance,
                'Project': MockProject,
                'MeasureProperty': MockMeasureProperty
            };
            createController = function() {
                $injector.get('$controller')("MeasureInstanceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'measurePlatformApp:measureInstanceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
