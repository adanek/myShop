'use strict';

(function (app) {
  app.controller('UserCtrl', ['$scope', 'User', function ($scope, User) {

    $scope.user = User.getUserInfo();

    $scope.map = {
      defaults: {
        scrollWheelZoom: false
      },
      center: {
        lat: 47.269203,
        lng: 11.402229,
        zoom: 10
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
      markers : []
    }

    // Zoom to home
    if($scope.user.address && $scope.user.address.marker){
      var home = $scope.user.address.marker;

      $scope.map.markers = {
        home: home
      };

      $scope.map.center.lat = home.lat;
      $scope.map.center.lng = home.lng;
      $scope.map.center.zoom = 12;
    }
  }]);
})(angular.module('myshopApp'));

