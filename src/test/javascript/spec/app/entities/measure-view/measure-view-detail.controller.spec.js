'use strict';

describe('Controller Tests', function() {

    describe('MeasureView Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMeasureView, MockPhase, MockDashboard;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMeasureView = jasmine.createSpy('MockMeasureView');
            MockPhase = jasmine.createSpy('MockPhase');
            MockDashboard = jasmine.createSpy('MockDashboard');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MeasureView': MockMeasureView,
                'Phase': MockPhase,
                'Dashboard': MockDashboard
            };
            createController = function() {
                $injector.get('$controller')("MeasureViewDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'measurePlatformApp:measureViewUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
