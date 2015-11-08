'use strict';

describe('Controller: AboutCtrl', function () {

  var $controller, AboutCtrl, createController;

  beforeEach(module('myshopApp'));

  beforeEach(inject(function($injector){

    $controller = $injector.get('$controller');
    createController = function(){
      return $controller('Products', {});
    }
  }));

  //it('should be green', inject(function($controller){
  //
  //  var ctrl = createController();
  //  expect(true).toBe(true);
  //}));
  //


});
