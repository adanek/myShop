(function(app){

    app.factory('Users',['$http', function($http){
        return {
            login: function (username, password){
                var cred = {
                    name: username,
                    hash: Crypto.SHA1(password)
                }
                $http.post('api/users/login',cred);
            }
        }
    }]);
})(angular.module('myshopApp'));
