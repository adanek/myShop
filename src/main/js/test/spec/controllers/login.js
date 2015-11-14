'use strict';

describe('Controller: LoginCtrl', function () {

  // load the controller's module
  beforeEach(module('myshopApp'));

  var AboutCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AboutCtrl = $controller('LoginCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a caption to the scope', function () {
    expect(scope.caption).toBeDefined();
  });
});
