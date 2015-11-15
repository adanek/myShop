'use strict';

(function(app){
    app.controller('LoginCtrl',['$scope', '$location', 'User', function($scope, $location, User){

      $scope.caption = 'Dich kenn ich doch!';
      $scope.username = '';
      $scope.password = '';

      $scope.login = function() {
        console.log("Läuft");

        $scope.$broadcast('show-errors-check-validity');

        if ($scope.loginForm.$valid) {

          User.login($scope.username, $scope.password).then(
            function success(){
              //User.setAuthenticated(true);
              $location.path("/").replace();
            },
            function error(){

            }
          );
        }
      }

      $scope.register = function(){
        $location.path('/register').replace();
      }
    }]);
})(angular.module('myshopApp'));
