'use strict';

(function (app){
  app.controller('ProductNewCtrl', ['$scope', '$location', 'Categories', 'Products', 'User',function ($scope, $location, Categories, Products, User){
    $scope.caption = "Neues Produkt anlegen";
    $scope.product= {};

    // Demo Daten
    $scope.categories =  [
      {"id": 1, "name": "Sport"},
      {"id": 2, "name": "IT"},
      {"id": 3, "name": "Bücher"}
    ];

    Categories.query().then(
      function successCallback(response){
        $scope.categories = response.data;
      },
      function errorCallback(response){

      }
    );


    $scope.save = function(){

      $scope.$broadcast('show-errors-check-validity');

      if ($scope.productForm.$valid) {

        $scope.product.category = $scope.category.name;
        $scope.product.categoryID = $scope.category.id;
        $scope.product.author = User.getUsername();
        $scope.product.authorID = User.getID();

        Products.new($scope.product).then(
          function success(response){
            $scope.product= {};
            $scope.category = {};
           $location.path("/products").replace();
          },
          function error(response){

          }
        );
      }
    };
  }]);
})(angular.module('myshopApp'));
