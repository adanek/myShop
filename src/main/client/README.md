# client

This project is generated with [yo angular generator](https://github.com/yeoman/generator-angular)
version 0.12.1.

## Build & development

Run `grunt` for building and `grunt serve` for preview.

## Testing

Running `grunt test` will run the unit tests with karma.


API

GET /api/users/{userId}             -> Get UserInfo()
POST /api/users/login               -> 
POST /api/users/register 	        -> register user
GET /api/users/logout				-> logout user

GET /api/categories                 -> List Category

GET /api/items                      -> List Item 
GET /api/items/category/{category}          -> List Item in Category

GET /api/comments/product/{productId}   -> List Comment

POST    /api/comments             
GET     /api/comments/{commentID} -> Status 201 Location Header set
PUT     /api/comments/{productID} -> 
DELETE  /api/comments/{productID} -> 

Category {
    id: int
    name: String
}

Item {
    id: int
    title: string
    description: string
    creationDate: int
    changeDate: int
    author: string (alias)
}

Comment {
    id: int
    content: string
    author: string (alias)
}