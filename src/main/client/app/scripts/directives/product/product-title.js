(function (app) {

  app.directive('productTitle', function () {
    return {
      restrict: 'E',
      templateUrl: 'templates/directives/product/product-title.html'
    };
  });

})(angular.module('myshopApp'));
