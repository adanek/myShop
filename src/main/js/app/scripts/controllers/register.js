'use strict';

(function (app) {
  app.controller('RegisterCtrl', ['$scope', '$location', 'User', function ($scope, $location, User) {

    $scope.caption = 'Wer warsch jetzt du?';

    $scope.username = '';
    $scope.password = '';

    $scope.registerUser = function () {

      $scope.$broadcast('show-errors-check-validity');

      if ($scope.registerForm.$valid) {
        User.register($scope.username, $scope.password).then(
          function successCallback() {
            $location.path('/login').replace();
          },
          function errorCallback(response){
            console.log('Something went wrong' + response.status);
          });
      }
    };
  }]);
})(angular.module('myshopApp'));
