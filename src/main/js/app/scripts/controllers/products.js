'use strict';

(function (app) {

  app.controller('ProductsCtrl', ['$scope', 'Products', 'User', function ($scope, Products, User) {

    $scope.caption = "Das könnte Ihnen gefallen:";
    $scope.userCanCreateProduct = User.canCreateProduct();
    $scope.userCanCreateComment = User.canCreateComment();

    if (User.isAuthenticated()) {
      if (User.isAdmin()) {
        $scope.adminMode = true;
      }
    }

    $scope.userCanEditProduct = function (product) {
      return User.canEditProduct(product);
    }

    var srv = this;

    // Demo Daten
    $scope.products = [
      {
        "id": 1,
        "title": "Demo Adidas Boost",
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
        "title": "Demo Lenovo Thinkpad",
        "category": "IT",
        "categoryID": 2,
        "description": "Laptop",
        "creationDate": 1447584696796,
        "changeDate": 1447584696796,
        "author": "Andi",
        "authorID": 3
      }, {
        "id": 3,
        "title": "Demo Adidas Boost",
        "category": "Sport",
        "categoryID": 1,
        "description": "Sportschuh",
        "creationDate": 1447584696796,
        "changeDate": 1447584696796,
        "author": "Pati",
        "authorID": 3
      },
      {
        "id": 4,
        "title": "Demo Lenovo Thinkpad",
        "category": "IT",
        "categoryID": 2,
        "description": "Laptop",
        "creationDate": 1447584696796,
        "changeDate": 1447584696796,
        "author": "Andi",
        "authorID": 3
      }
    ];

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

    $scope.removeProduct = function(product){
      var ndx = $scope.products.indexOf(product);

      if(ndx > -1){
        Products.delete(product).then(
          function successCallback(){
            $scope.products.splice(ndx, 1);
          },
          function errorCallback(){

          }
        );

      }

    };

  }]);

})(angular.module('myshopApp'));

