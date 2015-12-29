(function (app) {
  app.directive('twitter', [
    function() {
      return {
        link: function(scope, element, attr) {
          setTimeout(function() {
            twttr.widgets.createShareButton(
              attr.url,
              element[0],
              function(el) {}, {
                text: attr.text,
                url: attr.url,
                hashtags: attr.hashtags,
                via: attr.via,
                size: 'large'
              }
            );
          });
        }
      }
    }
  ]);
})(angular.module('myshopApp'));
