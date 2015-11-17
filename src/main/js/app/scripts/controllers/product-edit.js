'use strict';

(function (app) {
  app.controller('ProductEditCtrl', ['$scope', '$location', '$routeParams', 'Categories', 'Products', function ($scope, $location, $routeParams, Categories, Products) {

    $scope.caption = 'Produkt bearbeiten';
    $scope.product = {};
    $scope.category = {};

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
      function errorCallback() {

      }
    );


    $scope.save = function () {

      $scope.$broadcast('show-errors-check-validity');

      if ($scope.productForm.$valid) {

        $scope.product.category = $scope.category.name;
        $scope.product.categoryID = $scope.category.id;

        Products.update($scope.product).then(
          function success() {
            $location.path('/products').replace();
          },
          function error() {}
        );
      }
    };
  }]);
})(angular.module('myshopApp'));
