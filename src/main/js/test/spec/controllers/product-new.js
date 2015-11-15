'use strict';

describe('Controller: ProductNewCtrl', function () {

  // load the controller's module
  beforeEach(module('myshopApp'));

  var ProductNewCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    ProductNewCtrl = $controller('ProductNewCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(ProductNewCtrl.awesomeThings.length).toBe(3);
  });
});
