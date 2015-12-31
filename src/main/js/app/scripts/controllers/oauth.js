'use strict';

(function(app){
  app.controller('OauthCtrl', ['$scope', '$localStorage', '$http', '$location', function ($scope, $localStorage, $http, $location) {

    var seed = $localStorage.seed;
    var code = getParameterByName('code');
    var state =  getParameterByName('state');
    console.log('seed: ' + seed);
    console.log('state: ' + state);
    console.log('state: ' + code);

    if(seed && state && code && (seed === state)){

      $http.get('api/users/login/oauth?code=' + code + '&state=' + state);
      delete $localStorage.seed;

    }
    else{
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


