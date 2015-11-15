'use strict';

describe('Directive: myshopProductDetails', function () {

  // load the directive's module
  beforeEach(module('myshopApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<myshop-product-details></myshop-product-details>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the myshopProductDetails directive');
  }));
});
