'use strict';

(function(app){

  app.factory('User',[function UserFactory(){

    var srv = this;
    var authenticated = false;

    srv.isAuthenticated = function(){
      return authenticated;
    };

    return srv;
  }]);

})(angular.module('myshopApp'));
