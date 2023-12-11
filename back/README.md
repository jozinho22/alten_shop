# 1 Créer la base

Créer une base de données ALTEN_SHOP :

``` postgresql
sudo -i -u postgres
psql
\c alten_shop
```

# 2 Lancer l'appli

Un user (avec l'authority ADMIN) est déjà créé avec les id/pwd suivants ("joss@gmail.com", "joss").
De même, les produits du fichier products.json sont insérés.

# 3 S'authentifier 

Appeller la ressource http://localhost:8080/auth/authenticate, avec le body suivant :

{
    email : "joss@gmail.com",
    password : "joss"
}

Vous recevez alors un token qui vous permet :

1) de créer un nouveau user qui aura l'authority USER :
  - POST http://localhost:8080/auth/register

Idem avec le body :
{
  email : "joss2@gmail.com",
  password : "joss2"
}


2) d'appeler les ressources avec le token Bearer JWT ("Authorization" dans le Header) :
  - GET http://localhost:8080/api/products
  - GET http://localhost:8080/api/products/{id} 
  - POST http://localhost:8080/api/products, avec un body comme :
  {
    "code": "acvx872gc",
    "name": "Yellow Earbuds (from DB)",
    "description": "Product Description",
    "image": "yellow-earbuds.jpg",
    "price": 89,
    "category": "Electronics",
    "quantity": 35,
    "inventoryStatus": "INSTOCK",
    "rating": 3
  }
  - PATCH http://localhost:8080/api/products/{id}, idem avec un body
  - DELETE http://localhost:8080/api/products/{id}