'use strict';

(function (app) {
  app.controller('UserCtrl', ['$scope', 'User', function ($scope, User) {

    $scope.name = User.getUsername();

    $scope.defaults = {
      scrollWheelZoom: false
    };

    $scope.center = {
      lat: 47.269203,
      lng: 11.402229,
      zoom: 12
    };

    $scope.layers = {
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
    };

    $scope.markers = {
      osloMarker: {
        lat: 47.269203,
        lng: 11.402229,
        message: "Your Homebase!",
        focus: false,
        draggable: false
      }
    };

  }]);
})(angular.module('myshopApp'));
