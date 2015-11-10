(function (app) {

  app.directive('productTitle', function () {
    return {
      restrict: 'E',
      templateUrl: 'views/directives/product/product-title.html'
    };
  });

})(angular.module('myshopApp'));
