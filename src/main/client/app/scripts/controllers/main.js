'use strict';

angular.module('myshopApp')
  .controller('MainCtrl', function () {

    this.products = [
      {
        pid: 1,
        name: "Product 1",
        description: "This is a awesome product",
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
        reviews:[]
      }
    ];
  });
