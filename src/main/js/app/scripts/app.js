'use strict';

/**
 * @ngdoc overview
 * @name myshopApp
 * @description
 * # myshopApp
 *
 * Main module of the application.
 */
angular
  .module('myshopApp', [
    'ngResource',
    'ngRoute',
    'ui.bootstrap.showErrors'
  ])


  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/pages/main.html',
        controller: 'HomeCtrl',
        controllerAs: 'home'
      })
      .when('/about', {
        templateUrl: 'views/pages/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .when('/login', {
        templateUrl: 'views/pages/login.html',
        controller: 'LoginCtrl'
      })
      .when('/register', {
        templateUrl: 'views/pages/register.html',
        controller: 'RegisterCtrl',
        controllerAs: 'register'
      })
      .when('/products', {
        templateUrl: 'views/pages/products.html',
        controller: 'ProductsCtrl'
      })
      .when('/categories', {
        templateUrl: 'views/pages/categories.html',
        controller: 'CategoriesCtrl'
      })
      .when('/products/new', {
        templateUrl: 'views/pages/product-edit.html',
        controller: 'ProductNewCtrl'
      })
      .when('/products/:id/edit', {
        templateUrl: 'views/pages/product-edit.html',
        controller: 'ProductEditCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });

//
//Categories.query().then(
//  function successCallback(response){
//
//  },
//  function errorCallback(response){
//
//  }
//)
