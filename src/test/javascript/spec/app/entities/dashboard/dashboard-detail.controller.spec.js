'use strict';

describe('Controller Tests', function() {

    describe('Dashboard Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDashboard, MockPhase, MockMeasureView;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDashboard = jasmine.createSpy('MockDashboard');
            MockPhase = jasmine.createSpy('MockPhase');
            MockMeasureView = jasmine.createSpy('MockMeasureView');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Dashboard': MockDashboard,
                'Phase': MockPhase,
                'MeasureView': MockMeasureView
            };
            createController = function() {
                $injector.get('$controller')("DashboardDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'measurePlatformApp:dashboardUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
