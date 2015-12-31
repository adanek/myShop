'use strict';

(function (app) {
  app.controller('LoginCtrl', ['$scope', '$location', '$localStorage', 'User', function ($scope, $location, $localStorage, User) {

    $scope.caption = 'Dich kenn ich doch!';
    $scope.username = '';
    $scope.password = '';

    $scope.login = function () {

      $scope.$broadcast('show-errors-check-validity');

      if ($scope.loginForm.$valid) {

        User.login($scope.username, $scope.password).then(
          function success() {
            //User.setAuthenticated(true);
            $location.path('/').replace();
          },
          function error() {
          }
        );
      }
    };

    $scope.register = function () {
      $location.path('/register').replace();
    };

    $scope.useGithub = function () {

      var clientId = '2cd8ce35fb2392ce6d04';
      var redirect_uri = 'http://localhost:8000/';
      var scope = 'user:email';
      var state = 'NotGuessable';
      $localStorage.seed = state;

      window.location = 'https://github.com/login/oauth/authorize' +
        '?client_id=' + clientId +
        '&redirect_uri=' + redirect_uri +
        '&scope=' + scope +
        '&state=' + state;
    }
  }]);
})(angular.module('myshopApp'));
