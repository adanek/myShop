'use strict';

(function (app) {

  //noinspection JSUnusedGlobalSymbols
  app.factory('User', ['$http', '$rootScope', '$q', '$location', '$localStorage', function UserFactory($http, $rootScope, $q, $location, $localStorage) {

    var srv = this;
    var authenticated = false;
    var defaultUser = {
      alias: 'Tom Riddle',
      id: 999,
      role: 'evil lord',
      rights: {
        canCreateCategory: false,
        canCreateItem: false,
        canCreateComment: false
      }
    };
    var user = defaultUser;

    // Check existing session
    var token = $localStorage.token;
    if (token) {
      getUserInfo(token);
    }

    function getUserInfo(token) {
      var parts = token.split(".");
      if (parts[1]) {
        var uInfo = JSON.parse(atob(parts[1]));

        if (!uInfo.sub) {
          console.log(uInfo);
          return;
        }

        $http.get('api/users/' + uInfo.alias).then(
          function success(response) {
            user = response.data;
            srv.setAuthenticated(true);
            $rootScope.$broadcast('user-login');
          },
          function error() {
            delete $localStorage.token;
          });
      } else {
        delete $localStorage.token;
      }
    };

    srv.isAuthenticated = function () {
      return authenticated;
    };

    srv.isAdmin = function () {
      return user.role === 'admin';
    };

    srv.canCreateCategory = function () {
      return user.rights.canCreateCategory;
    };

    srv.canCreateProduct = function () {
      return user.rights.canCreateItem;
    };

    srv.canEditProduct = function (product) {
      return product.author === user.alias || user.role === 'admin';
    };

    srv.canCreateComment = function () {
      return user.rights.canCreateComment;
    };

    srv.canEditComment = function (comment) {
      return comment.author === user.alias || user.role === 'admin';
    };

    srv.setAuthenticated = function (newValue) {

      if ($rootScope.$root.$$phase !== '$apply' && $rootScope.$root.$$phase !== '$digest') {
        $rootScope.$apply(function () {
          authenticated = newValue;
        });
      }
      else {
        authenticated = newValue;
      }
    };

    srv.getUsername = function () {
      return user.alias === undefined ? '' : user.alias;
    };

    srv.getUserRole = function () {
      return user.role;
    };

    srv.getID = function () {
      return user.id;
    };

    srv.login = function (username, password) {

      /* globals CryptoJS */
      var hash = CryptoJS.SHA1(password).toString(CryptoJS.enc.Base64);
      return $http.post('api/users/login', {name: username, hash: hash}).then(
        function successCallback(response) {
          $localStorage.token = response.data.token;
          getUserInfo($localStorage.token);
          return response;
        },
        function errorCallback(response) {
          return response;
        });
    };

    srv.loginWithGitHub = function (code, state) {
      return $http.get('api/users/login/oauth?code=' + code + '&state=' + state).then(
        function successCallback(response) {
          $localStorage.token = response.data.token;
          getUserInfo($localStorage.token)
          return response;
        },
        function errorCallback(response) {
          return response
        }
      );
    }

    srv.logout = function () {

      delete $localStorage.token;
      srv.setAuthenticated(false);
      user = defaultUser;
      $location.path('/#').replace();
    };

    /**
     * Registers a new user on the server
     * @param name the name of the user
     * @param password the password of the user in cleartext
     * @returns Promise<User>
     */
    srv.register = function (user) {
      var hash = CryptoJS.SHA1(user.password).toString(CryptoJS.enc.Base64);

      return $http.post('api/users/register', {
        name: user.username,
        hash: hash,
        address: {
          zip: user.address.zip_code,
          city: user.address.city,
          street: user.address.street,
          country: user.address.country,
          longitude: user.position.long,
          latitude: user.position.lat
        }
      }).then(
        function successCallback(response) {
          return response;
        },
        function errorCallback(response) {
          console.log('Something went wrong');
          return $q.reject(response);
        }
      );
    };

    srv.remove = function (user) {
      return $http.delete('api/users/' + user.id, user);
    };

    srv.changeRole = function (user) {
      return $http.put('api/users/' + user.id, user);
    };

    srv.all = function () {
      return $http.get('api/users');
    };

    srv.roles = function () {
      return $http.get('api/users/roles');
    };

    return srv;
  }]);

})(angular.module('myshopApp'));
