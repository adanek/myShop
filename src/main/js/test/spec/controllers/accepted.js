'use strict';

describe('Controller: AcceptedCtrl', function () {

  // load the controller's module
  beforeEach(module('myshopApp'));

  var AcceptedCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AcceptedCtrl = $controller('AcceptedCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(AcceptedCtrl.awesomeThings.length).toBe(3);
  });
});
