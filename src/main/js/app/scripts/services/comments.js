'use strict';

(function(app){
  app.factory('Comments', ['$http', function CommentsFactory($http){
    return {
      formProduct: function (productId) {
        return $http.get('api/comments/item/' + productId);
      },

      save: function(comment){
        return $http.post('api/comments/new', comment);
      }
    };
  }]);
})(angular.module('myshopApp'));
