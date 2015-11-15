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
      .otherwise({
        redirectTo: '/'
      });
  });
