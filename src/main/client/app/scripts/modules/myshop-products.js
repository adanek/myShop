(function () {
  var app = angular.module('myshop-products', []);

  app.directive('productTitle', function () {
    return {
      restrict: 'E',
      templateUrl: 'scripts/directives/product/product-title.html'
    };
  });

  app.directive('productPanels', function () {
    return {
      restrict: 'E',
      templateUrl: 'scripts/directives/product/product-panels.html',
      controller: function () {
        this.tab = 1;

        this.selectTab = function (tab) {
          this.tab = tab;
        }

        this.isSelected = function (tab) {
          return tab === this.tab;
        }
      },
      controllerAs: 'panel'
    }
  });

})();
