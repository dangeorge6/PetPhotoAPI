(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dog', {
            parent: 'entity',
            url: '/dog',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Dogs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dog/dogs.html',
                    controller: 'DogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('dog-detail', {
            parent: 'entity',
            url: '/dog/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Dog'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dog/dog-detail.html',
                    controller: 'DogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Dog', function($stateParams, Dog) {
                    return Dog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dog',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dog-detail.edit', {
            parent: 'dog-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dog/dog-dialog.html',
                    controller: 'DogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dog', function(Dog) {
                            return Dog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dog.new', {
            parent: 'dog',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dog/dog-dialog.html',
                    controller: 'DogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                years_old: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dog', null, { reload: true });
                }, function() {
                    $state.go('dog');
                });
            }]
        })
        .state('dog.edit', {
            parent: 'dog',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dog/dog-dialog.html',
                    controller: 'DogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dog', function(Dog) {
                            return Dog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dog', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dog.delete', {
            parent: 'dog',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dog/dog-delete-dialog.html',
                    controller: 'DogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Dog', function(Dog) {
                            return Dog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dog', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
