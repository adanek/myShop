'use strict';

(function(app){
  app.controller('MainCtrl', ['$scope', '$rootScope', 'User', function ($scope, $rootScope, User) {

    $scope.title = 'Home';
    $scope.authenticated = User.isAuthenticated();
    $scope.username = User.getUsername();
    $scope.role = User.getUserRole();

    $scope.$watch(function () {
      return User.isAuthenticated();
    }, function (newVal) {
      $scope.authenticated = newVal;
    });

    $rootScope.$on('user-login', function(){
      $scope.username = User.getUsername();
      $scope.role = User.getUserRole();
    });

    $scope.logout = function(){
      User.logout();
    };

    $scope.isAdmin = function(){
      return User.isAuthenticated() && User.isAdmin();
    };

  }]);
})(angular.module('myshopApp'));


