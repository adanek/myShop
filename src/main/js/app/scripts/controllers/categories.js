'use strict';

(function (app) {
  app.controller('CategoriesCtrl', ['$scope', 'Categories', function ($scope, Categories) {

    $scope.category = {};

    Categories.query().then(
      function successCallback(response) {
        $scope.categories = response.data;
      },
      function errorCallback() {

      }
    );

    $scope.save = function () {

      $scope.$broadcast('show-errors-check-validity');

      if ($scope.categoryForm.$valid) {

        if ($scope.category.id) {

          // Update existing category
          Categories.update($scope.category).then(
            function successCallback() {
              $scope.category = {};
            },
            function errorCallback() {
            }
          );
        } else {

          // Add new category
          Categories.new($scope.category).then(
            function successCallback(response) {

              $scope.categories.push(response.data);
              $scope.category = {};
            },
            function errorCallback() {
              $scope.category = {};
            }
          );
        }
      }
    };

    $scope.edit = function (category) {
      $scope.category = category;
    };

    $scope.remove = function (category) {

      var ndx = $scope.categories.indexOf(category);

      if (ndx > -1) {

        Categories.remove(category).then(

          function successCallback() {
            $scope.categories.splice(ndx, 1);
          },

          function errorCallback() {
          }
        );
      }
    };

  }]);
})(angular.module('myshopApp'));
