'use strict';

describe('Controller Tests', function() {

    describe('Vote Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockVote, MockDogPhoto, MockClient;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockVote = jasmine.createSpy('MockVote');
            MockDogPhoto = jasmine.createSpy('MockDogPhoto');
            MockClient = jasmine.createSpy('MockClient');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Vote': MockVote,
                'DogPhoto': MockDogPhoto,
                'Client': MockClient
            };
            createController = function() {
                $injector.get('$controller')("VoteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'apiChallengeApp:voteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
