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
GET                         /api/users/logout				-> logout user
http://localhost:8080/myShop/api/users/logout

GET 	/api/categories             -> List Category
POST	/api/categories/new			-> create new category
PUT		/api/categories/{category}	-> change category
DELETE	/api/categories/{category}	-> delete category

GET 	/api/items                      -> List Item 
GET 	/api/items/category/{category}          -> List Item in Category
POST	/api/items/new				-> create new item
PUT		/api/items/{item}			-> change item
DELETE 	/api/items/{item}			-> delete item

GET 	/api/comments/item/{itemID}   -> List Comment
POST    /api/comments/new             
GET     /api/comments/{commentID} -> Status 201 Location Header set
PUT     /api/comments/{commentID} -> 
DELETE  /api/comments/{commentID} -> 

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
