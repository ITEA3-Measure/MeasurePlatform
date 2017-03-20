'use strict';

describe('Controller Tests', function() {

    describe('Phase Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPhase, MockProject, MockDashboard, MockMeasureView;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPhase = jasmine.createSpy('MockPhase');
            MockProject = jasmine.createSpy('MockProject');
            MockDashboard = jasmine.createSpy('MockDashboard');
            MockMeasureView = jasmine.createSpy('MockMeasureView');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Phase': MockPhase,
                'Project': MockProject,
                'Dashboard': MockDashboard,
                'MeasureView': MockMeasureView
            };
            createController = function() {
                $injector.get('$controller')("PhaseDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'measurePlatformApp:phaseUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
