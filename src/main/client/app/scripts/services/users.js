(function(app){

    app.factory('Users',['$http', function($http){
        return {
            login: function (username, password){
                var cred = {
                    name: username,
                    hash: password
                }
                $http.post('api/users/login',cred);
            }
        }
    }]);
})(angular.module('myshopApp'));
