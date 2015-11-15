'use strict';

(function (app) {

  app.controller('ProductsCtrl', ['$scope', function ($scope) {

    $scope.caption ="Products:";
    var srv = this;

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
  }]);

})(angular.module('myshopApp'));

