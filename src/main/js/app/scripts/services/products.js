'use strict';

(function (app) {
  app.factory('Products', ['$http', function ProductsFactory($http) {
    return {
      query: function () {
        return $http.get('api/items')
      },

      fromCategory: function (categotryId) {
        return $http.get('api/items/category/' + categotryId)
      },

      new: function (product) {
        return $http.post('api/items/new', product);
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
  }])
})(angular.module('myshopApp'));

