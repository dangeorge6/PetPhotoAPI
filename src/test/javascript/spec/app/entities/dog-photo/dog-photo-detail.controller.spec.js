'use strict';

describe('Controller Tests', function() {

    describe('DogPhoto Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDogPhoto, MockDog, MockVote;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDogPhoto = jasmine.createSpy('MockDogPhoto');
            MockDog = jasmine.createSpy('MockDog');
            MockVote = jasmine.createSpy('MockVote');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DogPhoto': MockDogPhoto,
                'Dog': MockDog,
                'Vote': MockVote
            };
            createController = function() {
                $injector.get('$controller')("DogPhotoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apiChallengeApp:dogPhotoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
