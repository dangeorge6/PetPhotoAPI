(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('VoteController', VoteController);

    VoteController.$inject = ['$scope', '$state', 'Vote'];

    function VoteController ($scope, $state, Vote) {
        var vm = this;
        
        vm.votes = [];

        loadAll();

        function loadAll() {
            Vote.query(function(result) {
                vm.votes = result;
            });
        }
    }
})();
