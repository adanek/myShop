'use strict';

(function (app) {

  app.controller('ProductsCtrl', ['$scope', 'Products', function ($scope, Products) {

    $scope.caption = "Das könnte Ihnen gefallen:";
    var srv = this;

    // Demo Daten
    $scope.products = [
      {
        "id": 1,
        "title": "Adidas Boost",
        "category": "Sport",
        "categoryID": 1,
        "description": "Sportschuh",
        "creationDate": 1447584696796,
        "changeDate": 1447584696796,
        "author": "Pati",
        "authorID": 3
      },
      {
        "id": 2,
        "title": "Lenovo Thinkpad",
        "category": "IT",
        "categoryID": 2,
        "description": "Laptop",
        "creationDate": 1447584696796,
        "changeDate": 1447584696796,
        "author": "Pati",
        "authorID": 3
      }];

    Products.query().then(
      function successCallback(response) {
        $scope.products = response.data;
      },
      function errorCallback(response) {

      }
    );

    $scope.$on('myshop-active-category-changed', function (event, category) {

      // Load only products from category
      if (category) {
        Products.fromCategory(category).then(
          function successCallback(response) {
            $scope.products = response.data;
          },
          function errorCallback(response) {

          }
        );

        // Load all products
      } else {

        Products.query().then(
          function successCallback(response) {
            $scope.products = response.data;
          },
          function errorCallback(response) {

          }
        );
      }
    });


  }]);

})(angular.module('myshopApp'));

