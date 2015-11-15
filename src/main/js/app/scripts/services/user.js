'use strict';

(function (app) {

  app.factory('User', ['$http', '$rootScope', '$q', '$location', function UserFactory($http, $rootScope, $q, $location) {

    var srv = this;
    var authenticated = false;
    var user= {alias: "Tom Riddle", id: 999, rights: {
      canCreateItem: false
    }};

    srv.isAuthenticated = function () {
      return authenticated;
    };

    srv.isAdmin = function (){
      return user.role === 'admin';
    };

    srv.canCreateProduct = function (){
      return user.rights.canCreateItem;
    }

    srv.canCreateComment = function(){
      return user.rights.canCreateComment;
    };

    srv.canEditComment= function(comment){
      return comment.author === user.alias || user.role === 'admin';
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
      return user.alias == undefined ? '': user.alias;
    }

    srv.getID = function(){
      return user.id;
    }

    srv.login = function (username, password) {

      var hash = CryptoJS.SHA1(password).toString(CryptoJS.enc.Base64);
      return $http.post('api/users/login', {name: username, hash: hash}).then(
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
          $location.path('/#').replace();
          return response;
        },
        function errorCallback(response){
          return $q.reject(response);
        });
    };

    /**
     * Registers a new user on the server
     * @param name the name of the user
     * @param password the password of the user in cleartext
       * @returns Promise<User>
       */
    srv.register = function(name, password){
      var hash = CryptoJS.SHA1(password).toString(CryptoJS.enc.Base64);

      return $http.post('api/users/register', {name: name, hash: hash}).then(
        function successCallback(response){
          return response;
        },
        function errorCallback(response){
          console.log("Something went wrong");
          return $q.reject(response);
        }
      );
    };

    return srv;
  }]);

})(angular.module('myshopApp'));
