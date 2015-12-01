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
    'ui.bootstrap.showErrors',
    'ngStorage'
  ])


  .config(['$routeProvider', '$httpProvider', function ($routeProvider, $httpProvider) {
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
      .when('/users', {
        templateUrl: 'views/pages/users.html',
        controller: 'UserCtrl'
      })
      .when('/cart', {
        templateUrl: 'views/pages/cart.html',
        controller: 'CartCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });

    $httpProvider.interceptors.push(['$q', '$location', '$localStorage', function($q, $location, $localStorage){
      return {
        'request': function(config){
          config.headers = config.headers || {};
          if($localStorage.token){
            config.headers.Authorization = 'Bearer '+ $localStorage.token;
          }
          return config;
        },
        'responseError': function(response){
          if(response.status === 401){
            $location.path('#/login');
          }

          return $q.reject(response);
        }
      }
    }]);
  }]);

//
//Categories.query().then(
//  function successCallback(response){
//
//  },
//  function errorCallback(response){
//
//  }
//)
