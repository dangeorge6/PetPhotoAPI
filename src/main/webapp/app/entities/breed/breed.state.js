(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('breed', {
            parent: 'entity',
            url: '/breed',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Breeds'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/breed/breeds.html',
                    controller: 'BreedController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('breed-detail', {
            parent: 'entity',
            url: '/breed/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Breed'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/breed/breed-detail.html',
                    controller: 'BreedDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Breed', function($stateParams, Breed) {
                    return Breed.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'breed',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('breed-detail.edit', {
            parent: 'breed-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/breed/breed-dialog.html',
                    controller: 'BreedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Breed', function(Breed) {
                            return Breed.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('breed.new', {
            parent: 'breed',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/breed/breed-dialog.html',
                    controller: 'BreedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('breed', null, { reload: true });
                }, function() {
                    $state.go('breed');
                });
            }]
        })
        .state('breed.edit', {
            parent: 'breed',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/breed/breed-dialog.html',
                    controller: 'BreedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Breed', function(Breed) {
                            return Breed.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('breed', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('breed.delete', {
            parent: 'breed',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/breed/breed-delete-dialog.html',
                    controller: 'BreedDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Breed', function(Breed) {
                            return Breed.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('breed', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
