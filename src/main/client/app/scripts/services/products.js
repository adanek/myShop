(function(app){

  app.factory('Products',['$http', function($http){
    return {
      magic: 3,
      get: function(){$http.get('/api/products');}
    }
  }]);
})(angular.module('myshopApp'));
