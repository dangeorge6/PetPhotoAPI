'use strict';

describe('Controller Tests', function() {

    describe('Dog Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDog, MockBreed, MockDogPhoto;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDog = jasmine.createSpy('MockDog');
            MockBreed = jasmine.createSpy('MockBreed');
            MockDogPhoto = jasmine.createSpy('MockDogPhoto');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Dog': MockDog,
                'Breed': MockBreed,
                'DogPhoto': MockDogPhoto
            };
            createController = function() {
                $injector.get('$controller')("DogDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apiChallengeApp:dogUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
