'use strict';

describe('Service: user', function () {

  // load the service's module
  beforeEach(module('myshopApp'));

  // instantiate service
  var user;
  beforeEach(inject(function (User) {
    user = User;
  }));

  it('should be false after creation', function () {
    expect(user.isAuthenticated()).toBe(false);
  });

});
