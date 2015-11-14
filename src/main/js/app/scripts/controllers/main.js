'use strict';

(function(app){
  app.controller('MainCtrl', ['$scope', 'User', function ($scope, User) {

    $scope.title = "Home"
    $scope.authenticated = User.isAuthenticated();

    $scope.$watch(function () {
      return User.isAuthenticated();
    }, function (newVal) {
      $scope.authenticated = newVal;
    });


    $scope.logout = function(){
      User.logout().then(function success(){
        User.setAuthenticated(false);
      });
    };

  }]);
})(angular.module('myshopApp'));


