'use strict';

(function (app) {
  app.controller('OauthCtrl', ['$scope', '$localStorage', '$http', '$location', 'User', function ($scope, $localStorage, $http, $location, User) {

    var seed = $localStorage.seed;
    var code = getParameterByName('code');
    var state = getParameterByName('state');
    console.log('seed: ' + seed);
    console.log('state: ' + state);
    console.log('code: ' + code);

    if (seed && state && code && (seed === state)) {

      User.loginWithGitHub(code, state).then(
        function success() {
          delete $localStorage.seed;
          $location.url($location.path());
        }
      );
    }
    else {
      console.log('no seed');
    }

    function getParameterByName(name) {
      name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
      var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
      return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }
  }]);
})(angular.module('myshopApp'));


