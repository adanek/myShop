'use strict';

(function(app){
  app.controller('MainCtrl', ['$scope', '$rootScope', 'User', 'Cart', '$localStorage', '$location', function ($scope, $rootScope, User, Cart, $localStorage, $location) {

    $scope.title = 'Home Tab';
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

    $scope.$watch(Cart.getItemCount, function(newValue){
      $scope.cartItemCount = newValue;
    });

    if($localStorage.seed){
      $location.path('/login/oauth').replace();
    }

  }]);
})(angular.module('myshopApp'));


