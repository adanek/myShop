'use strict';

(function (app) {
  app.directive('myshopCategorySelect', ['Categories', function (Categories) {

    return {
      replace: true,
      restrict: 'E',
      templateUrl: 'views/directives/myshop-category-select.html',

      scope: {
        activeCategory: "="
      },

      link: function (scope, element, attrs) {

        // Demo Daten
        scope.categories =  [
          {"id": 1, "name": "Sport"},
          {"id": 2, "name": "IT"},
          {"id": 3, "name": "Bücher"}
        ];

        Categories.query().then(
          function successCallback(response){
            scope.categories = response.data;
          },
          function errorCallback(response){

          }
        );
      },

      controller: function ($scope) {


        this.getActiveCategory = function () {
          return $scope.activeCategory;
        };

        this.setActiveCategory = function (category) {

          if ($scope.activeCategory === category.id) {
            $scope.activeCategory = undefined;
          } else {
            $scope.activeCategory = category.id;
          }

          $scope.$emit('myshop-active-category-changed', $scope.activeCategory);
        };
      }
    }
  }]);
})(angular.module('myshopApp'));
