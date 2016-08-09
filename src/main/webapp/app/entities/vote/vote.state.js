(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vote', {
            parent: 'entity',
            url: '/vote',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Votes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vote/votes.html',
                    controller: 'VoteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('vote-detail', {
            parent: 'entity',
            url: '/vote/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Vote'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vote/vote-detail.html',
                    controller: 'VoteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Vote', function($stateParams, Vote) {
                    return Vote.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vote',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vote-detail.edit', {
            parent: 'vote-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vote/vote-dialog.html',
                    controller: 'VoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vote', function(Vote) {
                            return Vote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vote.new', {
            parent: 'vote',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vote/vote-dialog.html',
                    controller: 'VoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isUp: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vote', null, { reload: true });
                }, function() {
                    $state.go('vote');
                });
            }]
        })
        .state('vote.edit', {
            parent: 'vote',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vote/vote-dialog.html',
                    controller: 'VoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Vote', function(Vote) {
                            return Vote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vote', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vote.delete', {
            parent: 'vote',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vote/vote-delete-dialog.html',
                    controller: 'VoteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Vote', function(Vote) {
                            return Vote.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vote', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
