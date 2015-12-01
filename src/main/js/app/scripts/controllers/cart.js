(function (app) {
  app.controller('CartCtrl', ['$scope','Cart', function ($scope, Cart) {

    $scope.items = [];
    $scope.items = Cart.getItems();

    $scope.removeEntry = function(entry){
      var ndx = $scope.items.indexOf(entry);

      if(ndx > -1){
        Cart.remove(entry);
        //$scope.items.splice(ndx, 1);
      }
    };

    $scope.toggleEditState = function(entry){

      if(entry.edit){
        entry.edit = undefined;

      }else {
        entry.edit = true;
      }
    };

    $scope.getTotal = function(){
      var sum = 0.0;
      $scope.items.forEach(function(entry){
        sum += entry.amount * entry.item.price;
      })

      return sum;
    };

    $scope.clearCart = function(){
      Cart.clear();
    };

  }]);
})(angular.module('myshopApp'));
