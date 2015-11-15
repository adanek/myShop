'use strict';

(function (app) {

  app.directive('myshopProductComment', function () {
    return {
      templateUrl: 'views/directives/myshop-product-comment.html',
      restrict: 'E'
    };
  });

})(angular.module('myshopApp'));

