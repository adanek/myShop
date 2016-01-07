'use strict';

(function (app) {
  //noinspection JSUnusedGlobalSymbols
  app.factory('Products', ['$http', function ProductsFactory($http) {
    return {
      new: function (product) {
        return $http.post('api/items', product);
      },

      query: function () {
        return $http.get('api/items');
      },

      fromCategory: function (categotry) {
        return $http.get('api/items/category/' + categotry.id);
      },

      delete: function(product){
        return $http.delete('api/items/' + product.id, product);
      },

      get: function(productId){
        return $http.get('api/items/' + productId);
      },

      update: function(product){
        return $http.put('api/items/' + product.id, product);
      }
    };
  }]);
})(angular.module('myshopApp'));

