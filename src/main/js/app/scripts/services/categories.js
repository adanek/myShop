'use strict';

(function (app) {
  app.factory('Categories', ['$http', function CategoriesFactory($http) {
    return {
      query: function () {
        return $http.get('api/categories');
      }
    }
  }]);
})(angular.module('myshopApp'));
