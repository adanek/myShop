'use strict';

(function (app) {

  app.directive('myshopProductComments', function () {
    return {
      templateUrl: 'views/directives/myshop-product-comments.html',
      restrict: 'E',
      controller: function ($scope, User, Comments) {

        var srv = this;
        $scope.comment = {};
        srv.formVisible = false;

        $scope.userCanCreateComment = User.canCreateComment();

        $scope.userCanEditComment = function (comment) {
          return User.canEditComment(comment);
        }

        $scope.comments = [];
        Comments.formProduct($scope.product.id).then(
          function successCallback(response) {
            $scope.comments = response.data;
          },
          function errorCallback(response) {

          }
        );

        //Demo Daten
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

        srv.deleteComment = function (comment) {
          var ndx = $scope.comments.indexOf(comment);

          if (ndx > -1) {
            Comments.remove(comment).then(
              function successCallback() {
                $scope.comments.splice(ndx, 1);
              },
              function successCallback() {
              }
            );
          }
        };

        srv.editComment = function (comment) {
          $scope.comment = comment;

          srv.formVisible = true;
        };

        srv.showForm = function () {
          $scope.comment.author = User.getUsername();
          $scope.comment.authorID = User.getID();
          $scope.comment.itemID = $scope.product.id;
          $scope.comment.creationDate = Date.now();
          $scope.comment.changeDateDate = $scope.comment.creationDate;
          $scope.comment.content = "";

          srv.formVisible = true;
        };

        srv.saveComment = function () {

          $scope.$broadcast('show-errors-check-validity');

          if ($scope.commentForm.$valid) {

            if ($scope.comment.commentId) {

              // Update existing comment
              Comments.update($scope.comment).then(
                function successCallback(response) {
                  srv.formVisible = false;
                },
                function error(response) {
                  console.log("Fail to save comment: " + response.status);
                }
              );
            } else {

              // Create new comment
              Comments.save($scope.comment).then(
                function successCallback(response) {
                  $scope.comments.push($scope.comment);
                  srv.formVisible = false;
                },
                function error(response) {
                  console.log("Fail to save comment: " + response.status);
                }
              );
            }
          }
        };

      },
      controllerAs: 'commentCtrl'
    };
  });

})(angular.module('myshopApp'));

