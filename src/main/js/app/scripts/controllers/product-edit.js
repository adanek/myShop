'use strict';

(function (app) {
  app.controller('ProductEditCtrl', ['$scope', '$location', '$routeParams', 'Categories', 'Products', 'User', function ($scope, $location, $routeParams, Categories, Products, User) {
    $scope.caption = "Produkt bearbeiten";

    $scope.product = {
      id: 1,
      author: "Andi",
      authorID: 12
    };

    $scope.category = {
      id: 2,
      name: "IT"
    }

    // Demo Daten
    $scope.categories = [
      {"id": 1, "name": "Sport"},
      {"id": 2, "name": "IT"},
      {"id": 3, "name": "Bücher"}
    ];

    // Load Product data
    Products.get($routeParams.id).then(
      function successCallback(response) {
        $scope.product = response.data;
        $scope.category = {id: response.data.categoryID, name: response.data.category};
      },
      function errorCallback() {

      }
    );

    // Load categories
    Categories.query().then(
      function successCallback(response) {
        $scope.categories = response.data;
      },
      function errorCallback(response) {

      }
    );


    $scope.save = function () {

      $scope.$broadcast('show-errors-check-validity');

      if ($scope.productForm.$valid) {

        $scope.product.category = $scope.category.name;
        $scope.product.categoryID = $scope.category.id;

        Products.update($scope.product).then(
          function success(response) {
            $location.path("/products").replace();
          },
          function error(response) {

          }
        );
      }
    };
  }]);
})(angular.module('myshopApp'));
