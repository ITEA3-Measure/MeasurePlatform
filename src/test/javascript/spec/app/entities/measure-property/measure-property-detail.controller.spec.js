'use strict';

describe('Controller Tests', function() {

    describe('MeasureProperty Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMeasureProperty, MockMeasureInstance;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMeasureProperty = jasmine.createSpy('MockMeasureProperty');
            MockMeasureInstance = jasmine.createSpy('MockMeasureInstance');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MeasureProperty': MockMeasureProperty,
                'MeasureInstance': MockMeasureInstance
            };
            createController = function() {
                $injector.get('$controller')("MeasurePropertyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'measurePlatformApp:measurePropertyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
