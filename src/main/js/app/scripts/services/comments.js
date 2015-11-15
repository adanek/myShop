'use strict';

(function(app){
  app.factory('Comments', ['$http', function CommentsFactory($http){
    return {
      formProduct: function (productId) {
        return $http.get('api/comments/item/' + productId);
      },

      save: function(comment){
        return $http.post('api/comments/new', comment);
      },

      update: function (comment){
        return $http.put('api/comments/'+ comment.commentId, comment);
      },

      remove: function(comment){
        return $http.delete('api/comments/'+ comment.commentId, comment);
      }
    };
  }]);
})(angular.module('myshopApp'));
