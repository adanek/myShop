(function(app){

  app.controller('MyController', MyController);
// The controller code
  function MyController($scope, $http) {
    var authToken;

    $http.get('/auth.py').success(function (data, status, headers) {
      authToken = headers('A-Token');
      $scope.user = data;
    }).error(function () {
      $scope.status = 'Failed...';
    });

    $scope.saveMessage = function (message) {
      var headers = {'Authorization': authToken};
      $scope.status = 'Saving...';

      $http.post('/add-msg.py', message, {headers: headers}).success(function (response) {
        $scope.status = '';
      }).error(function () {
        $scope.status = 'Failed...';
      });
    };
  }
})(angular.module('myshopApp'));

//// The module code
//angular
//  .module('myshopApp', [])
//  .controller('MyController', MyController);
//
//// The controller code
//function MyController($scope, $http) {
//  var authToken;
//
//  $http.get('/auth.py').success(function(data, status, headers) {
//    authToken = headers('A-Token');
//    $scope.user = data;
//  }).error(function(){
//    $scope.status = 'Failed...';
//  });
//
//  $scope.saveMessage = function(message) {
//    var headers = { 'Authorization': authToken };
//    $scope.status = 'Saving...';
//
//    $http.post('/add-msg.py', message, { headers: headers } ).success(function(response) {
//      $scope.status = '';
//    }).error(function() {
//      $scope.status = 'Failed...';
//    });
//  };
//}
