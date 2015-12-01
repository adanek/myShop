'use strict';

(function(app){
  app.controller('MainCtrl', ['$scope', '$rootScope', 'User', 'Cart', function ($scope, $rootScope, User, Cart) {

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

    $scope.$watch(Cart.getItemCount, function(newValue){
      $scope.cartItemCount = newValue;
    });

  }]);
})(angular.module('myshopApp'));


