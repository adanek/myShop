'use strict';

(function (app) {

  app.factory('User', ['$http', '$rootScope', function UserFactory($http, $rootScope) {

    var srv = this;
    var authenticated = false;

    srv.isAuthenticated = function () {
      return authenticated;
    };

    srv.setAuthenticated = function (newValue) {

      if ($rootScope.$root.$$phase != '$apply' && $rootScope.$root.$$phase != '$digest') {
        $rootScope.$apply(function () {
          authenticated = newValue;
        });
      }
      else {
        authenticated = newValue;
      }
    }

    srv.login = function (username, password) {
      return $http.post('api/users/login', {name: username, hash: password});
    }

    srv.logout = function(){
      return $http.get('api/users/logout');
    };

    return srv;
  }]);

})(angular.module('myshopApp'));
