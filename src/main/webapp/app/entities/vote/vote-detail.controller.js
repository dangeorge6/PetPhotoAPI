(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('VoteDetailController', VoteDetailController);

    VoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vote', 'DogPhoto', 'Client'];

    function VoteDetailController($scope, $rootScope, $stateParams, previousState, entity, Vote, DogPhoto, Client) {
        var vm = this;

        vm.vote = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('apiChallengeApp:voteUpdate', function(event, result) {
            vm.vote = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
