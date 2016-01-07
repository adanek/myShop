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

          if($scope.activeCategory){
            $scope.activeCategory = $scope.activeCategory.id === category.id ? undefined : category;
          }
          else {
            $scope.activeCategory = category;
          }

          $scope.$emit('myshop-active-category-changed', $scope.activeCategory);
        };
      }]
    };
  }]);
})(angular.module('myshopApp'));
