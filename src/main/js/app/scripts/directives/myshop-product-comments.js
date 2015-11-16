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
        };

        $scope.comments = [];
        Comments.formProduct($scope.product.id).then(
          function successCallback(response) {
            $scope.comments = response.data;
          },
          function errorCallback() {

          }
        );

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
          $scope.comment.changeDate = $scope.comment.creationDate;
          $scope.comment.content = '';

          srv.formVisible = true;
        };

        srv.saveComment = function () {

          $scope.$broadcast('show-errors-check-validity');

          if ($scope.commentForm.$valid) {

            if ($scope.comment.commentId) {

              // Update existing comment
              Comments.update($scope.comment).then(
                function successCallback() {
                  srv.formVisible = false;
                },
                function error(response) {
                  console.log('Fail to save comment: ' + response.status);
                }
              );
            } else {

              // Create new comment
              Comments.save($scope.comment).then(
                function successCallback() {
                  $scope.comments.push($scope.comment);
                  srv.formVisible = false;
                },
                function error(response) {
                  console.log('Fail to save comment: ' + response.status);
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

