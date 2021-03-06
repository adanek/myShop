'use strict';

(function (app) {
  app.directive('myshopCategoryItem', [function () {

    return {
      replace: true,
      restrict: 'E',
      templateUrl: 'views/directives/myshop-category-item.html',
      scope: {
        category: '='
      },
      require: '^myshopCategorySelect',
      link: function (scope, element, attrs, myshopCategorySelectCtrl) {
        scope.makeActive = function () {
          myshopCategorySelectCtrl.setActiveCategory(scope.category);
        };

        scope.categoryActive = function () {
          if (myshopCategorySelectCtrl.getActiveCategory()) {
            return myshopCategorySelectCtrl.getActiveCategory().id === scope.category.id;
          }
          return false;
        };
      }
    };
  }]);
})(angular.module('myshopApp'));
