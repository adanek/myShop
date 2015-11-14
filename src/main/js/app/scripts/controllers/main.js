'use strict';

(function(app){
  app.controller('MainCtrl', ['$scope', 'User', function ($scope, User) {

    $scope.title = "Home"
    $scope.authenticated = User.isAuthenticated();

  }]);
})(angular.module('myshopApp'));


