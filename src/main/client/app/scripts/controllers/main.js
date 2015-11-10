'use strict';

angular.module('myshopApp')
  .controller('MainCtrl', function (Products) {

	this.product = {
		name:	"Name"
	}  
	  
	var ctrl = this;
	
	this.addToCart=function(){
		Products.put(this.product);
	}
	  
    this.products = [
      {
        pid: 1,
        name: "Product 1",
        description: "This is an awesome new product",
        price: 2,
        forSale: true,
        reviews: [
          {
            stars: 5,
            body: "So awesome",
            author: "Andi"
          },
          {
            stars: 0,
            body: "I do not like to learn cool things",
            author: "Pati"
          }
        ]
      },
      {
        pid: 2,
        name: "Product 2",
        description: "This product is even better than product 1",
        price: 2.7,
        forSale: true,
        reviews:[]
      }
    ];
  });
