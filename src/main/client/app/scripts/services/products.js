(function(app){

  app.factory('Products',['$http', function($http){
    return {
      magic: 3,
      get: function(){$http.get('/api/products');},
      put:function(product){$http.put('api/products', product);}
    }
  }]);
})(angular.module('myshopApp'));
