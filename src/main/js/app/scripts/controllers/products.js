'use strict';

(function (app) {

  app.controller('ProductsCtrl', ['$scope', 'Products', 'User', 'Cart', '$http', '$window', function ($scope, Products, User, Cart, $http, $window) {


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
      function errorCallback() {
      }
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

        // Load shops
        $http.get('/api/categories/shops/' + category.searchtoken).then(
          function successCallback(data) {
            $scope.map.markers = data;
          },
          function errorCallback() {
            $scope.map.markers = [];
          });

        // Load all products
      } else {

        Products.query().then(
          function successCallback(response) {
            $scope.products = response.data;
          },
          function errorCallback() {

          }
        );

        $scope.map.markers = [];
      }
    });

    $scope.removeProduct = function (product) {
      var ndx = $scope.products.indexOf(product);

      if (ndx > -1) {
        Products.delete(product).then(
          function successCallback() {
            $scope.products.splice(ndx, 1);
          },
          function errorCallback() {
          }
        );
      }
    };

    $scope.addToCart = function (product) {
      Cart.add(product, 1);
    };

    // Map
    $scope.map = {
      defaults: {
        scrollWheelZoom: false
      },
      center: {
        lat: 47.269203,
        lng: 11.402229,
        zoom: 12
      },
      layers: {
        baselayers: {
          googleTerrain: {
            name: 'Google Terrain',
            layerType: 'TERRAIN',
            type: 'google'
          },
          googleHybrid: {
            name: 'Google Hybrid',
            layerType: 'HYBRID',
            type: 'google'
          },
          googleRoadmap: {
            name: 'Google Streets',
            layerType: 'ROADMAP',
            type: 'google'
          }
        }
      },
      markers: []
    };

    // Center map to current position
    if ($window.navigator.geolocation) {
      $window.navigator.geolocation.getCurrentPosition(
        function successCallback(pos) {
          $scope.map.center.lng = pos.coords.longitude;
          $scope.map.center.lat = pos.coords.latitude;
        },
        function errorCallback(error) {
          console.log(error);
        });
    }
  }]);

})(angular.module('myshopApp'));

