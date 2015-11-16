'use strict';

(function (app) {

  app.controller('ProductsCtrl', ['$scope', 'Products', 'User', function ($scope, Products, User) {

    $scope.caption = 'Das könnte Ihnen gefallen:';
    $scope.userCanCreateCategory = User.canCreateCategory();
    $scope.userCanCreateProduct = User.canCreateProduct();
    $scope.userCanCreateComment = User.canCreateComment();

    $scope.userCanEditProduct = function (product) {
      return User.canEditProduct(product);
    };

    Products.query().then(
      function successCallback(response) {
        $scope.products = response.data;
      },
      function errorCallback() { }
    );

    $scope.$on('myshop-active-category-changed', function (event, category) {

      // Load only products from category
      if (category) {
        Products.fromCategory(category).then(
          function successCallback(response) {
            $scope.products = response.data;
          },
          function errorCallback() {

          }
        );

        // Load all products
      } else {

        Products.query().then(
          function successCallback(response) {
            $scope.products = response.data;
          },
          function errorCallback() {

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

