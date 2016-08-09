(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dog-photo', {
            parent: 'entity',
            url: '/dog-photo',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DogPhotos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dog-photo/dog-photos.html',
                    controller: 'DogPhotoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('dog-photo-detail', {
            parent: 'entity',
            url: '/dog-photo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DogPhoto'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dog-photo/dog-photo-detail.html',
                    controller: 'DogPhotoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DogPhoto', function($stateParams, DogPhoto) {
                    return DogPhoto.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dog-photo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dog-photo-detail.edit', {
            parent: 'dog-photo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dog-photo/dog-photo-dialog.html',
                    controller: 'DogPhotoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DogPhoto', function(DogPhoto) {
                            return DogPhoto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dog-photo.new', {
            parent: 'dog-photo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dog-photo/dog-photo-dialog.html',
                    controller: 'DogPhotoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                url: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dog-photo', null, { reload: true });
                }, function() {
                    $state.go('dog-photo');
                });
            }]
        })
        .state('dog-photo.edit', {
            parent: 'dog-photo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dog-photo/dog-photo-dialog.html',
                    controller: 'DogPhotoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DogPhoto', function(DogPhoto) {
                            return DogPhoto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dog-photo', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dog-photo.delete', {
            parent: 'dog-photo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dog-photo/dog-photo-delete-dialog.html',
                    controller: 'DogPhotoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DogPhoto', function(DogPhoto) {
                            return DogPhoto.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dog-photo', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
