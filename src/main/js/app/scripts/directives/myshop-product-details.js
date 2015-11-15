'use strict';

(function (app) {

  app.directive('myshopProductDetails', function () {
    return {
      templateUrl: 'views/directives/myshop-product-details.html',
      restrict: 'E',

      link: function postLink(scope, element, attrs) {

      },

      controller: function(){
        this.tab = 1;

        this.selectTab = function(tab){
          this.tab= tab;
        };

        this.isSelected = function(tab){
          return this.tab === tab;
        };
      },

      controllerAs: 'panel'
    };
  });

})(angular.module('myshopApp'));

