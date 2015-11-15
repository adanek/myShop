'use strict';

(function (app) {

  app.controller('CommentCtrl', ['$scope', 'User',  'Comments', function ($scope, User, Comments) {

    var srv = this;
    $scope.comment = {};
    $scope.enableCreation = User.isAuthenticated();

    $scope.cs = Comments.formProduct($scope.product.id);
    $scope.comments = [{
      "id": 1,
      "itemID": 12,
      "itemTitle": "Test title",
      "content": "erster kommentar",
      "author": "Pati",
      "creationDate": 1447584696796,
      "authorID": 3
    }, {
      "id": 2,
      "itemID": 12,
      "itemTitle": "Test title",
      "content": "zweiter kommentar",
      "author": "Andi",
      "authorID": 4
    }];

    srv.formVisible = false;

    srv.showForm = function(){

      if($scope.enableCreation){
        $scope.comment.author = User.getUsername();
        $scope.comment.authorID = User.getID();
        $scope.comment.creationDate = Date.now();
        $scope.comment.content = "";
      }

      srv.formVisible = true;
    };

    srv.addComment = function (){

      $scope.$broadcast('show-errors-check-validity');

      if ($scope.commentForm.$valid) {

        Comments.save($scope.comment).then(
          function successCallback(response){
            $scope.comments.push($scope.comment);
            srv.formVisible = false;
          },
          function error(response){
            console.log("Fail to save comment: " + response.status);
          }
        );
      }
    };

  }]);
})(angular.module('myshopApp'));
