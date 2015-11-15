'use strict';

(function (app) {
  app.factory('Products', ['$http', function ProductsFactory($http) {
    return {
      query: function(){return $http.get('api/items')},
      fromCategory: function(categotryId){return $http.get('api/items/category/' + categotryId)}
    };
  }])})(angular.module('myshopApp'));

