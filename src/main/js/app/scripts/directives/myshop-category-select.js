'use strict';

(function (app) {
  app.directive('myshopCategorySelect', ['Categories', function (Categories) {

    return {
      replace: true,
      restrict: 'E',
      templateUrl: 'views/directives/myshop-category-select.html',

      scope: {
        activeCategory: '='
      },

      link: function (scope) {

        Categories.query().then(
          function successCallback(response) {
            scope.categories = response.data;
          },
          function errorCallback() {
          }
        );
      },

      controller: ['$scope', function ($scope) {

        this.getActiveCategory = function () {
          return $scope.activeCategory;
        };

        this.setActiveCategory = function (category) {
          $scope.activeCategory = $scope.activeCategory === category.id ? undefined : category.id;
          $scope.$emit('myshop-active-category-changed', $scope.activeCategory);
        };
      }]
    };
  }]);
})(angular.module('myshopApp'));
