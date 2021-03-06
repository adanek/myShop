'use strict';

(function (app) {
  app.controller('UsersCtrl', ['$scope', 'User', function ($scope, User) {

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

    function init() {
      // Load users
      User.all().then(
        function success(response) {
          $scope.users = response.data;
        },
        function error(response) {
          console.log(response);
        }
      );

      // load user roles
      User.roles().then(
        function success(response) {
          $scope.roles = response.data;
        },
        function error(response) {
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

    $scope.remove = function (user) {

      var ndx = $scope.users.indexOf(user);
      if (ndx > -1) {

        User.remove(user).then(
          function success() {
            $scope.users.splice(ndx, 1);
          },
          function error(response) {
            $scope.error = "Failed to delete user " + user.alias;
          });
      }
    };
  }]);
})(angular.module('myshopApp'));
