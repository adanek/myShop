'use strict';

(function (app){
  app.controller('PanelCtrl', [function (){
    this.tab = 1;

    this.selectTab = function(tab){
      this.tab= tab;
    };

    this.isSelected = function(tab){
      return this.tab === tab;
    };
  }]);
})(angular.module('myshopApp'));
