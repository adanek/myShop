'use strict';

(function (app) {
  app.factory('Categories', ['$http', function CategoriesFactory($http) {

    return {
      query: function () {
        return $http.get('api/categories');
      },

      update: function(category) {
        return $http.put('api/categories/'+ category.id, category);
      },

      new: function(category){
        return $http.post('api/categories/new', category);
      },

      remove: function(category){
        return $http.delete('api/categories/'+ category.id, category);
      }
    }
  }]);
})(angular.module('myshopApp'));
