'use strict';

(function (app) {

  app.directive('myshopNavigation', function () {
    return {
      templateUrl: 'views/directives/myshop-navigation.html',
      restrict: 'E',
      controller: ['$location', 'User', function ($location, User) {

        this.links = [
          {
            name: 'Home',
            target: '/'
          },
          {
            name: 'Produkte',
            target: '/products'
          },
          {
            name: 'About',
            target: '/about'
          },
          {
            name: 'Benutzer',
            target: '/users',
            adminOnly: true
          }
        ];

        this.isSelected = function (link) {
          return $location.path() === link.target;
        };

        this.linkVisible = function (link) {
          if (link.adminOnly) {
            return User.isAuthenticated() && User.isAdmin();
          }

          return true;
        }
      }],

      controllerAs: 'nav'
    };
  });

})(angular.module('myshopApp'));

