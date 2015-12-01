'use strict';

(function (app) {
  app.controller('UserCtrl', ['$scope', 'User', function ($scope, User) {

    $scope.loading = true;
    $scope.user = undefined;
    $scope.users = [];
    $scope.roles = [];

    init();

    //// Demo data
    //$scope.users = [
    //  {
    //    id: 1,
    //    alias: "Tim",
    //    role: "admin"
    //  },
    //  {
    //    id: 2,
    //    alias: "Ben",
    //    role: "user"
    //  }];
    //$scope.roles = [
    //  "admin",
    //  "guest",
    //  "user"
    //];

    function init (){
      // Load users
      User.all().then(
        function success(response){
          $scope.users = response.body;
        },
        function error(response){
          console.log(response);
        }
      );

      // load user roles
      User.roles().then(
        function success(response){
          $scope.roles = response.body;
        },
        function error(response){
          console.log(response);
        });
    }

    $scope.edit = function (user) {
      $scope.user = user;
      $scope.oldRole = $scope.user.role;
    };

    $scope.save = function () {
      User.changeRole($scope.user).then(
        function successCallback() {
          $scope.user = undefined;
        },
        function errorCallBack(response) {
          console.log(response);
        });
    };

    $scope.cancel = function () {
      $scope.user.role = $scope.oldRole;
      $scope.oldRole = undefined;
      $scope.user = undefined;
    };

    $scope.remove= function(user){
      User.deleteUser(user).then(
        function success(){
          $scope.oldRole = undefined;
          $scope.user = undefined;
        },
        function error(response){
          console.log(response);
        }
      );
    };

  }]);
})(angular.module('myshopApp'));
