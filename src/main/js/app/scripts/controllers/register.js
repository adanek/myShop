'use strict';

(function (app) {
  app.controller('RegisterCtrl', ['$scope', '$location', 'User', '$window', function ($scope, $location, User, $window) {

    $scope.caption = 'Wer warsch jetzt du?';

    var defaultuser = {
      username: '',
      password: '',
      address: {
        street: '',
        zip_code: '',
        city: ''
      },
      position: {
        long: 0.0,
        lat: 0.0
      },
    };
    $scope.user = defaultuser;

    //locate
    if ($window.navigator.geolocation) {
      $window.navigator.geolocation.getCurrentPosition(
        function successCallback(pos) {
          $scope.user.position.long = pos.coords.longitude;
          $scope.user.position.lat = pos.coords.latitude;
        },
        function errorCallback(error) {
          console.log("Position konnte nicht ermittelt werden:");
          console.log(error);
        }
      )
    }

    $scope.registerUser = function () {

      $scope.$broadcast('show-errors-check-validity');

      if ($scope.registerForm.$valid) {
        User.register($scope.user).then(
          function successCallback() {
            $scope.user = defaultuser;
            $location.path('/login').replace();
          },
          function errorCallback(response) {
            console.log('Something went wrong' + response.status);
          });
      }
    };
  }]);
})(angular.module('myshopApp'));
