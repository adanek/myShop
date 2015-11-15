'use strict';

(function (app) {
  app.factory('Products', ['$http', function ProductsFactory($http) {
    return {
      query: function(){return $http.get('api/items')},
      fromCategory: function(categotryId){return $http.get('api/items/category/' + categotryId)},
      new: function(product){
        return $http.post('api/items/new', product);
      }
    };
  }])})(angular.module('myshopApp'));

