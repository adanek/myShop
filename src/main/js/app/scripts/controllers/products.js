'use strict';

(function (app) {

  app.controller('ProductsCtrl', ['$scope', 'Products', 'User', 'Cart', 'Categories', '$window', function ($scope, Products, User, Cart, Categories, $window) {


    $scope.caption = 'Das könnte Ihnen gefallen:';
    $scope.userCanCreateCategory = User.canCreateCategory();
    $scope.userCanCreateProduct = User.canCreateProduct();
    $scope.userCanCreateComment = User.canCreateComment();


    var markers = [
      {
        "lat": 47.2655023,
        "lng": 11.3948637,
        "message": "3 Handy Shop",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.2651858,
        "lng": 11.3969495,
        "message": "Foto Lamprechter",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.265906,
        "lng": 11.3952855,
        "message": "Foto Lamprechter",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.2664658,
        "lng": 11.402551,
        "message": "Hartlauer",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.2312649,
        "lng": 11.2797852,
        "message": "Elektro Kaufmann",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.2596743,
        "lng": 11.3966554,
        "message": "Tangl",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.2362934,
        "lng": 11.310654,
        "message": "Grundig",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.2362785,
        "lng": 11.3107323,
        "message": "Elektrocenter",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.2696559,
        "lng": 11.3930849,
        "message": "arcustik",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.2585417,
        "lng": 11.2717825,
        "message": "Elektro Thaler",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.262529,
        "lng": 11.3943243,
        "message": "Worldwide Electronics",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.261119,
        "lng": 11.3953588,
        "message": "Futuretec",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.2596729,
        "lng": 11.4108254,
        "message": "Hausberger Elektrotechnik",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.2764852,
        "lng": 11.3986942,
        "message": "IBM Ã–sterreich Internationale BÃ¼romaschinen GmbH",
        "focus": false,
        "draggable": false
      }, {
        "lat": 47.2594919,
        "lng": 11.3968204,
        "message": "Bohnissimo",
        "focus": false,
        "draggable": false
      }
    ];

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
        Categories.getShops(category, $scope.pos).then(
          function successCallback(response) {
            $scope.map.markers = response.data;
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
          $scope.pos = pos.coords;
        },
        function errorCallback(error) {
          console.log(error);
          $scope.pos = {

            // Default wert wenn pos nicht verfügbar
            longitude: 11.404804,
            latitude: 47.269436
          }
        });
    }
  }]);

})(angular.module('myshopApp'));

