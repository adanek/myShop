"use strict";angular.module("myshopApp",["ngResource","ngRoute","ui.bootstrap.showErrors","ngStorage"]).config(["$routeProvider","$httpProvider",function(a,b){a.when("/",{templateUrl:"views/pages/main.html",controller:"HomeCtrl",controllerAs:"home"}).when("/about",{templateUrl:"views/pages/about.html",controller:"AboutCtrl",controllerAs:"about"}).when("/login",{templateUrl:"views/pages/login.html",controller:"LoginCtrl"}).when("/register",{templateUrl:"views/pages/register.html",controller:"RegisterCtrl",controllerAs:"register"}).when("/products",{templateUrl:"views/pages/products.html",controller:"ProductsCtrl"}).when("/categories",{templateUrl:"views/pages/categories.html",controller:"CategoriesCtrl"}).when("/products/new",{templateUrl:"views/pages/product-edit.html",controller:"ProductNewCtrl"}).when("/products/:id/edit",{templateUrl:"views/pages/product-edit.html",controller:"ProductEditCtrl"}).when("/users",{templateUrl:"views/pages/users.html",controller:"UserCtrl"}).when("/cart",{templateUrl:"views/pages/cart.html",controller:"CartCtrl"}).otherwise({redirectTo:"/"}),b.interceptors.push(["$q","$location","$localStorage",function(a,b,c){return{request:function(a){return a.headers=a.headers||{},c.token&&(a.headers.Authorization="Bearer "+c.token),a},responseError:function(c){return 401===c.status&&b.path("#/login"),a.reject(c)}}}])}]),function(a){a.factory("Comments",["$http",function(a){return{formProduct:function(b){return a.get("api/comments/item/"+b)},save:function(b){return a.post("api/comments/new",b)},update:function(b){return a.put("api/comments/"+b.commentId,b)},remove:function(b){return a["delete"]("api/comments/"+b.commentId,b)}}}])}(angular.module("myshopApp")),function(a){a.factory("Categories",["$http",function(a){return{query:function(){return a.get("api/categories")},"new":function(b){return a.post("api/categories",b)},update:function(b){return a.put("api/categories/"+b.id,b)},remove:function(b){return a["delete"]("api/categories/"+b.id,b)}}}])}(angular.module("myshopApp")),function(a){a.factory("Products",["$http",function(a){return{"new":function(b){return a.post("api/items",b)},query:function(){return a.get("api/items")},fromCategory:function(b){return a.get("api/items/category/"+b)},"delete":function(b){return a["delete"]("api/items/"+b.id,b)},get:function(b){return a.get("api/items/"+b)},update:function(b){return a.put("api/items/"+b.id,b)}}}])}(angular.module("myshopApp")),function(a){a.factory("User",["$http","$rootScope","$q","$location","$localStorage",function(a,b,c,d,e){function f(c){var d=c.split(".");if(d[1]){var f=JSON.parse(atob(d[1]));if(!f.uid)return void console.log(f);a.get("api/users/"+f.uid).then(function(a){j=a.data,g.setAuthenticated(!0),b.$broadcast("user-login")},function(){delete e.token})}else delete e.token}var g=this,h=!1,i={alias:"Tom Riddle",id:999,role:"evil lord",rights:{canCreateCategory:!1,canCreateItem:!1,canCreateComment:!1}},j=i,k=e.token;return k&&f(k),g.isAuthenticated=function(){return h},g.isAdmin=function(){return"admin"===j.role},g.canCreateCategory=function(){return j.rights.canCreateCategory},g.canCreateProduct=function(){return j.rights.canCreateItem},g.canEditProduct=function(a){return a.author===j.alias||"admin"===j.role},g.canCreateComment=function(){return j.rights.canCreateComment},g.canEditComment=function(a){return a.author===j.alias||"admin"===j.role},g.setAuthenticated=function(a){"$apply"!==b.$root.$$phase&&"$digest"!==b.$root.$$phase?b.$apply(function(){h=a}):h=a},g.getUsername=function(){return void 0===j.alias?"":j.alias},g.getUserRole=function(){return j.role},g.getID=function(){return j.id},g.login=function(b,c){var d=CryptoJS.SHA1(c).toString(CryptoJS.enc.Base64);return a.post("api/users/login",{name:b,hash:d}).then(function(a){return e.token=a.data.token,f(e.token),a},function(a){return a})},g.logout=function(){return a.post("api/users/logout").then(function(a){return delete e.token,g.setAuthenticated(!1),j=i,d.path("/#").replace(),a},function(a){return c.reject(a)})},g.register=function(b,d){var e=CryptoJS.SHA1(d).toString(CryptoJS.enc.Base64);return a.post("api/users/register",{name:b,hash:e}).then(function(a){return a},function(a){return console.log("Something went wrong"),c.reject(a)})},g.remove=function(b){return a["delete"]("api/users/"+b.id,b)},g.changeRole=function(b){return a.put("api/users/"+b.id,b)},g.all=function(){return a.get("api/users")},g.roles=function(){return a.get("api/users/roles")},g}])}(angular.module("myshopApp")),function(a){a.factory("Cart",["$http",function(a){var b=this,c=[];return b.getItems=function(){return c},b.getItemCount=function(){return c.length},b.add=function(a,b){var d=!1;c.forEach(function(c){c.item===a&&(c.amount+=b,d=!0)}),d||c.push({amount:b,item:a})},b.remove=function(a){var b=c.indexOf(a);c.splice(b,1)},b.clear=function(){c.splice(0,c.length),console.log("ShoppingCart cleared")},b.checkout=function(){return a.post("api/cashdesk",c)},b}])}(angular.module("myshopApp")),function(a){a.directive("myshopCategorySelect",["Categories",function(a){return{replace:!0,restrict:"E",templateUrl:"views/directives/myshop-category-select.html",scope:{activeCategory:"="},link:function(b){a.query().then(function(a){b.categories=a.data},function(){})},controller:["$scope",function(a){this.getActiveCategory=function(){return a.activeCategory},this.setActiveCategory=function(b){a.activeCategory=a.activeCategory===b.id?void 0:b.id,a.$emit("myshop-active-category-changed",a.activeCategory)}}]}}])}(angular.module("myshopApp")),function(a){a.directive("myshopCategoryItem",[function(){return{replace:!0,restrict:"E",templateUrl:"views/directives/myshop-category-item.html",scope:{category:"="},require:"^myshopCategorySelect",link:function(a,b,c,d){a.makeActive=function(){d.setActiveCategory(a.category)},a.categoryActive=function(){return d.getActiveCategory()===a.category.id}}}}])}(angular.module("myshopApp")),function(a){a.directive("myshopProductDetails",function(){return{templateUrl:"views/directives/myshop-product-details.html",restrict:"E",controller:function(){this.tab=1,this.selectTab=function(a){this.tab=a},this.isSelected=function(a){return this.tab===a}},controllerAs:"panel"}})}(angular.module("myshopApp")),function(a){a.directive("myshopProductComments",function(){return{templateUrl:"views/directives/myshop-product-comments.html",restrict:"E",controller:["$scope","User","Comments",function(a,b,c){var d=this;a.comment={},d.formVisible=!1,a.userCanCreateComment=b.canCreateComment(),a.userCanEditComment=function(a){return b.canEditComment(a)},a.comments=[],c.formProduct(a.product.id).then(function(b){a.comments=b.data},function(){}),d.deleteComment=function(b){var d=a.comments.indexOf(b);d>-1&&c.remove(b).then(function(){a.comments.splice(d,1)},function(){})},d.editComment=function(b){a.comment=b,d.formVisible=!0},d.showForm=function(){a.comment.author=b.getUsername(),a.comment.authorID=b.getID(),a.comment.itemID=a.product.id,a.comment.creationDate=Date.now(),a.comment.changeDate=a.comment.creationDate,a.comment.content="",d.formVisible=!0},d.saveComment=function(){a.$broadcast("show-errors-check-validity"),a.commentForm.$valid&&(a.comment.commentId?c.update(a.comment).then(function(){d.formVisible=!1},function(a){console.log("Fail to save comment: "+a.status)}):c.save(a.comment).then(function(){a.comments.push(a.comment),d.formVisible=!1},function(a){console.log("Fail to save comment: "+a.status)}))}}],controllerAs:"commentCtrl"}})}(angular.module("myshopApp")),function(a){a.directive("myshopProductComment",function(){return{templateUrl:"views/directives/myshop-product-comment.html",restrict:"E"}})}(angular.module("myshopApp")),function(a){a.directive("myshopNavigation",function(){return{templateUrl:"views/directives/myshop-navigation.html",restrict:"E",controller:["$location","User",function(a,b){this.links=[{name:"Home",target:"/"},{name:"Produkte",target:"/products"},{name:"About",target:"/about"},{name:"Benutzer",target:"/users",adminOnly:!0}],this.isSelected=function(b){return a.path()===b.target},this.linkVisible=function(a){return a.adminOnly?b.isAuthenticated()&&b.isAdmin():!0}}],controllerAs:"nav"}})}(angular.module("myshopApp")),function(a){a.controller("MainCtrl",["$scope","$rootScope","User","Cart",function(a,b,c,d){a.title="Home",a.authenticated=c.isAuthenticated(),a.username=c.getUsername(),a.role=c.getUserRole(),a.$watch(function(){return c.isAuthenticated()},function(b){a.authenticated=b}),b.$on("user-login",function(){a.username=c.getUsername(),a.role=c.getUserRole()}),a.logout=function(){c.logout()},a.isAdmin=function(){return c.isAuthenticated()&&c.isAdmin()},a.$watch(d.getItemCount,function(b){a.cartItemCount=b})}])}(angular.module("myshopApp")),function(a){a.controller("AboutCtrl",[function(){}])}(angular.module("myshopApp")),angular.module("myshopApp").controller("HomeCtrl",function(){this.awesomeThings=["HTML5 Boilerplate","AngularJS","Karma"]}),function(a){a.controller("LoginCtrl",["$scope","$location","User",function(a,b,c){a.caption="Dich kenn ich doch!",a.username="",a.password="",a.login=function(){a.$broadcast("show-errors-check-validity"),a.loginForm.$valid&&c.login(a.username,a.password).then(function(){b.path("/").replace()},function(){})},a.register=function(){b.path("/register").replace()}}])}(angular.module("myshopApp")),function(a){a.controller("RegisterCtrl",["$scope","$location","User",function(a,b,c){a.caption="Wer warsch jetzt du?",a.username="",a.password="",a.registerUser=function(){a.$broadcast("show-errors-check-validity"),a.registerForm.$valid&&c.register(a.username,a.password).then(function(){b.path("/login").replace()},function(a){console.log("Something went wrong"+a.status)})}}])}(angular.module("myshopApp")),function(a){a.controller("ProductsCtrl",["$scope","Products","User","Cart",function(a,b,c,d){a.caption="Das könnte Ihnen gefallen:",a.userCanCreateCategory=c.canCreateCategory(),a.userCanCreateProduct=c.canCreateProduct(),a.userCanCreateComment=c.canCreateComment(),a.userCanEditProduct=function(a){return c.canEditProduct(a)},b.query().then(function(b){a.products=b.data},function(){}),a.$on("myshop-active-category-changed",function(c,d){d?b.fromCategory(d).then(function(b){a.products=b.data},function(){}):b.query().then(function(b){a.products=b.data},function(){})}),a.removeProduct=function(c){var d=a.products.indexOf(c);d>-1&&b["delete"](c).then(function(){a.products.splice(d,1)},function(){})},a.addToCart=function(a){d.add(a,1)}}])}(angular.module("myshopApp")),function(a){a.controller("CategoriesCtrl",["$scope","Categories",function(a,b){a.category={},b.query().then(function(b){a.categories=b.data},function(){}),a.save=function(){a.$broadcast("show-errors-check-validity"),a.categoryForm.$valid&&(a.category.id?b.update(a.category).then(function(){a.category={}},function(){}):b["new"](a.category).then(function(b){a.categories.push(b.data),a.category={}},function(){a.category={}}))},a.edit=function(b){a.category=b},a.remove=function(c){var d=a.categories.indexOf(c);d>-1&&b.remove(c).then(function(){a.categories.splice(d,1)},function(){})}}])}(angular.module("myshopApp")),function(a){a.controller("ProductNewCtrl",["$scope","$location","Categories","Products","User",function(a,b,c,d,e){a.caption="Neues Produkt anlegen",a.product={},c.query().then(function(b){a.categories=b.data},function(){}),a.save=function(){a.$broadcast("show-errors-check-validity"),a.productForm.$valid&&(a.product.category=a.category.name,a.product.categoryID=a.category.id,a.product.author=e.getUsername(),a.product.authorID=e.getID(),d["new"](a.product).then(function(){a.product={},a.category={},b.path("/products").replace()},function(){}))}}])}(angular.module("myshopApp")),function(a){a.controller("ProductEditCtrl",["$scope","$location","$routeParams","Categories","Products",function(a,b,c,d,e){a.caption="Produkt bearbeiten",a.product={},a.category={},e.get(c.id).then(function(b){a.product=b.data,a.category={id:b.data.categoryID,name:b.data.category}},function(){}),d.query().then(function(b){a.categories=b.data},function(){}),a.save=function(){a.$broadcast("show-errors-check-validity"),a.productForm.$valid&&(a.product.category=a.category.name,a.product.categoryID=a.category.id,e.update(a.product).then(function(){b.path("/products").replace()},function(){}))}}])}(angular.module("myshopApp")),function(a){a.controller("UserCtrl",["$scope","User",function(a,b){function c(){b.all().then(function(b){a.users=b.data},function(a){console.log(a)}),b.roles().then(function(b){a.roles=b.data},function(a){console.log(a)})}a.loading=!0,a.user=void 0,a.users=[],a.roles=[],c(),a.edit=function(b){a.user=b,a.oldRole=a.user.role},a.save=function(){b.changeRole(a.user).then(function(){a.user=void 0},function(a){console.log(a)})},a.cancel=function(){a.user.role=a.oldRole,a.oldRole=void 0,a.user=void 0},a.remove=function(c){var d=a.users.indexOf(c);d>-1&&b.remove(c).then(function(){a.users.splice(d,1)},function(b){a.error="Failed to delete user "+c.alias})}}])}(angular.module("myshopApp")),function(a){a.controller("CartCtrl",["$scope","Cart",function(a,b){a.items=[],a.items=b.getItems(),a.removeEntry=function(c){var d=a.items.indexOf(c);d>-1&&b.remove(c)},a.toggleEditState=function(a){a.edit?a.edit=void 0:a.edit=!0},a.getTotal=function(){var b=0;return a.items.forEach(function(a){b+=a.amount*a.item.price}),b},a.clearCart=function(){b.clear()}}])}(angular.module("myshopApp")),angular.module("myshopApp").run(["$templateCache",function(a){a.put("views/directives/myshop-category-item.html",'<button class="list-group-item" ng-click="makeActive()" ng-class="{\'active\': categoryActive()}"> <span>{{ category.name }}</span> </button>'),a.put("views/directives/myshop-category-select.html",'<div class="list-group"> <myshop-category-item ng-repeat="category in categories" category="category"></myshop-category-item> </div>'),a.put("views/directives/myshop-navigation.html",'<section class="myshop-navigation"> <ul class="nav navbar-nav"> <li ng-repeat="link in nav.links" ng-class="{active:nav.isSelected(link)}" ng-show="nav.linkVisible(link)"> <a ng-href="#{{link.target}}">{{link.name}}</a> </li> </ul> </section>'),a.put("views/directives/myshop-product-comment.html",'<blockquote> <span>{{ comment.content }}</span> <footer> <span>{{ comment.author }} am {{ comment.creationDate | date : \'dd. MMMM yyyy\'}}</span> <span ng-show="userCanEditComment(comment)" class="pull-right"> <a ng-click="commentCtrl.editComment(comment)"><span class="glyphicon glyphicon-edit"></span></a> <a ng-click="commentCtrl.deleteComment(comment)"><span class="glyphicon glyphicon-trash"></span></a> </span> </footer> </blockquote>'),a.put("views/directives/myshop-product-comments.html",'<section class="product-comments" ng-show="panel.isSelected(2)"> <section class="comment-list"> <myshop-product-comment ng-repeat="comment in comments"></myshop-product-comment> </section> <section class="comment-creation" ng-show="userCanCreateComment"> <button ng-class="{hidden:commentCtrl.formVisible}" class="btn btn-default" ng-click="commentCtrl.showForm()"> Kommentar hinterlassen </button> <form name="commentForm" novalidate ng-show="commentCtrl.formVisible" ng-submit="commentCtrl.saveComment()"> <div class="form-group" show-errors> <label for="comment">Kommentar:</label> <textarea class="form-control" id="comment" name="comment" ng-model="comment.content" placeholder="Schreib hier deinen Kommentar hin und drücke dannach auf Speichern" required></textarea> <div class="help-block">Kommentar ist notwendig</div> </div> <div class="form-group"> <input type="submit" value="Speichern" class="form-control"> </div> </form> </section> </section>'),a.put("views/directives/myshop-product-details.html",'<section class="product-details"> <ul class="nav nav-pills"> <li ng-class="{active:panel.isSelected(1)}"><a href ng-click="panel.selectTab(1)">Description</a></li> <li ng-class="{active:panel.isSelected(2)}"><a href ng-click="panel.selectTab(2)">Reviews</a></li> </ul> <section class="product-description" ng-show="panel.isSelected(1)"> <p>{{ product.description }}</p> </section> <myshop-product-comments></myshop-product-comments> </section>'),a.put("views/pages/about.html","<p>This is the about view.</p>"),a.put("views/pages/cart.html",'<section class="myshop-cart"> <h4>Warenkorb:</h4> <table class="table table-hover"> <tr> <th>#</th> <th>Name</th> <th>EP</th> <th>GP</th> <th>Edit</th> <th>Delete</th> </tr> <tr ng-repeat="entry in items"> <td> <span ng-show="!entry.edit">{{entry.amount}}</span> <div class="input-group" ng-show="entry.edit"> <input type="number" class="form-control" ng-model="entry.amount"> <span class="input-group-btn"> <a class="btn btn-default" ng-click="toggleEditState(entry)">Ok</a> </span> </div> </td> <td>{{entry.item.title}}</td> <td>{{entry.item.price | currency:\'€ \'}}</td> <td>{{entry.item.price * entry.amount| currency:\'€ \'}}</td> <td><a ng-click="toggleEditState(entry)"><span class="glyphicon glyphicon-pencil"></span></a></td> <td><a ng-click="removeEntry(entry)"><span class="glyphicon glyphicon-trash"></span></a></td> </tr> <tr> <td></td> <td><strong>Total</strong></td> <td></td> <td><strong>{{ getTotal() | currency:\'€ \'}}</strong></td> <td></td> <td></td> </tr> </table> <footer class="pull-right"> <a class="btn btn-default" ng-click="clearCart()">Warenkorb leeren</a> <a class="btn btn-default">bestellen</a> </footer> </section>'),a.put("views/pages/categories.html",'<section class="myshop-categories"> <a class="btn btn-info" ng-href="#/products">Zurück zu den Produkten</a> <table class="table table-hover"> <tr> <th>Id</th> <th>Name</th> <th>Edit</th> <th>Delete</th> </tr> <tr ng-repeat="category in categories"> <td>{{ category.id}}</td> <td>{{ category.name}}</td> <td><a ng-click="edit(category)"><span class="glyphicon glyphicon-edit"></span></a></td> <td><a ng-click="remove(category)"><span class="glyphicon glyphicon-trash"></span></a></td> </tr> </table> <form name="categoryForm" novalidate ng-submit="save()"> <div class="form-group"> <label for="category-name">Name:</label> <input type="text" name="category-name" id="category-name" required ng-model="category.name" class="form-control"> </div> <div> <input type="submit" class="form-control" value="Speichern"> </div> </form> </section>'),a.put("views/pages/login.html",'<header> <div> {{ caption }} </div> </header> <section> <div class="text-left my-panel"> <form name="loginForm" novalidate ng-submit="login()"> <div class="form-group" show-errors> <label for="username">Benutzername:</label> <input type="text" name="username" id="username" class="form-control" placeholder="Dein Name" ng-model="username" required> <div class="help-block">Deinen Namen musst du mir schon verraten.</div> </div> <div class="form-group" show-errors> <label for="password">Passwort:</label> <input name="password" id="password" class="form-control" placeholder="Dein Passwort" type="PASSWORD" ng-model="password" required> <div class="help-block">Ohne Passwort könnte ja jeder behaupten, dass er du ist</div> </div> <div class="form-group"> <input type="submit" value="Lass mich rein" class="btn btn-default form-control"> </div> <div class="form-group" ng-show="errorMessage"> <div class="alert alert-danger"> {{ errorMessage }}</div> </div> </form> </div> </section> <footer> <div> <div class="form-group text-center bordered"> <a class="btn btn-default form-control" ng-click="register()">Nein, mich kennst du noch nicht</a> </div> </div> </footer>'),a.put("views/pages/main.html",'<div class="jumbotron"> <h1>Team 1 - myshop</h1> <p class="lead">Willkommen zu unserem MegaStore!</p> <img src="images/Cart-icon.333fe993.png" alt="ShoppingCart"><br> <p><a class="btn btn-lg btn-success" ng-href="#/products">Zu den Produkten</a></p> </div> <div class="row marketing"> <h4>Sonderangebot</h4> <p> Nur heute! Kauf 2 Zahl 3 - Solange dein Bankkonto hält, was es verspricht </p> </div>'),a.put("views/pages/product-edit.html",'<section> <header>{{ caption }}</header> <form name="productForm" novalidate ng-submit="save()"> <div class="form-group" show-errors> <label for="product-title">Titel:</label> <input type="text" name="product-title" id="product-title" class="form-control" ng-model="product.title" required> <div class="help-block">Titel ist erforderlich</div> </div> <div class="form-group" show-errors> <label for="product-category">Kategorie:</label> <select name="product-category" id="product-category" ng-options="category.name for category in categories track by category.name" ng-model="category" class="form-control" required></select> <div class="help-block">Kategorie ist erforderlich</div> </div> <div class="form-group" show-errors> <label for="product-description">Beschreibung:</label> <textarea ng-model="product.description" name="product-description" id="product-description" rows="10" class="form-control" required></textarea> <div class="help-block">Beschreibung ist erforderlich</div> </div> <div class="form-group" show-errors> <label for="price">Preis:</label> <input type="number" id="price" ng-model="product.price" required class="form-control"> <div class="help-block">Preis ist erforderlich</div> </div> <div class="form-group"> <input type="submit" value="Speichern" class="form-control"> </div> </form> <h4>Vorschau</h4> <section class="product"> <header>{{ product.title }}</header> <myshop-product-details></myshop-product-details> <hr> </section> </section> '),a.put("views/pages/products.html",'<section class="myshop-products row"> <section class="categories col-sm-3"> <header> <span>Kategorien:</span> <span class="pull-right" ng-show="userCanCreateCategory"> <a ng-href="#/categories"><span class="glyphicon glyphicon-edit"></span></a></span> </header> <div ng-show="!editCategories"> <myshop-category-select active-category="activeCategory"></myshop-category-select> </div> <div ng-show="editCategories"> <myshop-categories-edit></myshop-categories-edit> </div> </section> <section class="products col-sm-9"> <header> <span>{{ caption }}</span> <span ng-show="userCanCreateProduct"><a ng-href="#/products/new"><span class="glyphicon glyphicon-plus-sign"></span></a></span> </header> <section class="product" ng-repeat="product in products"> <header> <span>{{ product.title }}</span> <span class="pull-right">{{product.price | currency:\'€ \'}}</span> </header> <myshop-product-details></myshop-product-details> <footer> <a class="btn btn-info" ng-click="addToCart(product)"> <span class="glyphicon glyphicon-plus"></span> <span class="glyphicon glyphicon-shopping-cart"></span> </a> <span class="pull-right" ng-show="userCanEditProduct(product)"> <a ng-href="#/products/{{product.id}}/edit" class="btn btn-warning"><span class="glyphicon glyphicon-edit"></span></a> <a ng-click="removeProduct(product)" class="btn btn-danger"><span class="glyphicon glyphicon-trash"></span></a> </span> </footer> <hr> </section> </section> </section>'),a.put("views/pages/register.html",'<header> <div> {{ caption }} </div> </header> <section> <div> <form name="registerForm" novalidate ng-submit="registerUser()"> <div class="form-group" show-errors> <label for="username">Benutzername:</label> <input type="text" id="username" name="username" placeholder="Schreib hier deinen Namen rein" class="form-control" ng-model="username" required> <div class="help-block">Deinen Namen brauche ich</div> </div> <div class="form-group" show-errors> <label for="password">Passwort:</label> <input type="password" class="form-control" id="password" name="password" placeholder="und hier dein gewünschtes Passwort" ng-model="password" required> <div class="help-block">Passwort ist <notwendig></notwendig></div> </div> <div class="form-group"> <input type="submit" class="form-control" value="Registrieren"> </div> </form> </div> </section> <footer> <div> </div> </footer>'),a.put("views/pages/users.html",'<section class="myshop-users"> <h4>Benutzer Verwaltung</h4> <table class="table table-hover"> <tr> <th>Id</th> <th>Name</th> <th>Role</th> <th>Edit</th> <th>Delete</th> </tr> <tr ng-repeat="user in users"> <td>{{ user.id}}</td> <td>{{ user.alias}}</td> <td>{{ user.role}}</td> <td><a ng-click="edit(user)"><span class="glyphicon glyphicon-edit"></span></a></td> <td><a ng-click="remove(user)"><span class="glyphicon glyphicon-trash"></span></a></td> </tr> </table> <form name="userForm" novalidate ng-submit="save()" ng-show="user"> <div class="form-group"> <label for="role">Role for {{user.alias}}:</label> <select name="role" id="role" ng-model="user.role" class="form-control" required> <option ng-repeat="role in roles">{{role}}</option> </select> </div> <div> <input type="submit" class="btn btn-default" value="Speichern"> <input type="button" class="btn btn-warning" value="Abbrechen" ng-click="cancel()"> </div> </form> </section>')}]);