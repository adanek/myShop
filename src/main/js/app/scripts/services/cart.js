'use strict';

(function(app){
  app.factory('Cart',  ['$http', function CartFactory($http){

    var srv = this;
    var items = [];

    //// DemoData
    //var items = [
    //  {
    //    amount: 12,
    //    item: {
    //      id: 1,
    //      title: 'Turnschuh',
    //      price: 12.50,
    //      available: true
    //    }
    //  },
    //  {
    //    amount: 50,
    //    item: {
    //      id: 2,
    //      title: 'Taschentuch',
    //      price: 1.12,
    //      available: true
    //    }
    //  }
    //];

    srv.getItems = function(){
      return items;
    }

    srv.getItemCount = function(){
      return items.length;
    }

    srv.add = function(item, amount){

      var exist = false;

      items.forEach(function(entry){
        if(entry.item === item){
          entry.amount += amount;
          exist =true;
        }
      });

      if(!exist){
        items.push({amount: amount, item: item});
      }
    };

    srv.remove = function(cartItem){
      var ndx = items.indexOf(cartItem);
      items.splice(ndx, 1);
    };

    srv.clear = function(){
      items.splice(0, items.length)
      console.log("ShoppingCart cleared");
    };

    srv.checkout = function(){
      return $http.post('api/cashdesk', items);
    };

    return srv;
  }]);
})(angular.module('myshopApp'));
