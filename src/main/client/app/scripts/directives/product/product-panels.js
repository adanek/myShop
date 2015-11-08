(function(app){
  app.directive('productPanels', function () {
    return {
      restrict: 'E',
      templateUrl: 'templates/directives/product/product-panels.html',
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
})(angular.module('myshopApp'));
