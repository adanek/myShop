'use strict';

(function (app) {
  //noinspection JSUnusedGlobalSymbols
  app.factory('Categories', ['$http', function CategoriesFactory($http) {

    return {
      query: function () {
        return $http.get('api/categories');
      },

      new: function(category){
        return $http.post('api/categories', category);
      },

      update: function(category) {
        return $http.put('api/categories/'+ category.id, category);
      },

      remove: function(category){
        return $http.delete('api/categories/'+ category.id, category);
      }
    };
  }]);
})(angular.module('myshopApp'));
