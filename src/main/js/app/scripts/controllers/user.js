'use strict';

(function (app) {
  app.controller('UserCtrl', ['$scope', 'User', function ($scope, User) {

    $scope.name = User.getUsername();
    $scope.user = User.getUserInfo();
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

    var home = User.getUserInfo().address.marker;
    console.log(home);
    $scope.markers = {
      home: home
    };

  }]);
})(angular.module('myshopApp'));

