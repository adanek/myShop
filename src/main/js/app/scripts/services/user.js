'use strict';

(function (app) {

  app.factory('User', ['$http', '$rootScope', function UserFactory($http, $rootScope) {

    var srv = this;
    var authenticated = false;
    var user= {};

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

    srv.getUsername = function(){
      console.log('getUsername called: ' + user);
      return user.alias == undefined ? '': user.alias;
    }

    srv.login = function (username, password) {
      return $http.post('api/users/login', {name: username, hash: password}).then(
        function successCallback(response){
          user = response.data;
          srv.setAuthenticated(true);

          $rootScope.$broadcast('user-login');
          return response;
        },
        function errorCallback(response){
          return response;
        });
    }

    srv.logout = function(){
      return $http.post('api/users/logout').then(
        function successCallback(response){
          srv.setAuthenticated(false);
          user = {};

          return response;
        },
        function errorCallback(response){
          return response;
        });
    };

    return srv;
  }]);

})(angular.module('myshopApp'));
