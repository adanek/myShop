(function (app) {
  app.controller('CartCtrl', ['$scope','Cart', '$window', function ($scope, Cart, $window) {

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

    $scope.checkout = function(){
      Cart.checkout().then(
        function success(response){
          var url = response.headers('Location');
          $window.location.href = url;
        },
        function error(response){
          console.log(response);
        });
    };

  }]);
})(angular.module('myshopApp'));
