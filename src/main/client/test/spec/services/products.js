'use strict'

// testing controller
describe('Service: Products', function () {

  var $httpBackend, $rootScope, createController, fakeBackend, sut;

  // Set up the module
  beforeEach(module('myshopApp'));

  beforeEach(inject(function($injector) {
    // Set up $http-mock
    $httpBackend = $injector.get('$httpBackend');
    fakeBackend = $httpBackend
      .when('GET', '/api/products')
      .respond({userId: 'userX'}, {'A-Token': 'xxx'});

    // Create service instance
    sut = $injector.get('Products');
  }));

  // Ensure that there are no unhandled requests
  afterEach(function() {
    $httpBackend.verifyNoOutstandingExpectation();
    $httpBackend.verifyNoOutstandingRequest();
  });

  describe('If i ask for magic', function(){
    it('should return 3', inject(function (Products) { //parameter name = service name

      expect(Products.magic).toEqual(3);
    }));
  });

  describe('if i call the get method', function(){
    it('should send a GET request to /api/products', function(){
      $httpBackend.expectGET('/api/products');
      sut.get();
      $httpBackend.flush();
    });
  });
});
