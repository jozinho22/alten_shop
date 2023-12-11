Étant donné mon niveau sur Angular, je n'ai pas pu connecter la partie back et front.
J'ai uniquement pu faire le back avec :
- l'API
- la sécurité
- un scénario de test unitaire
- la doc OpenAi
http://localhost:8080/swagger-ui/index.html#/

Avant de lancer le serveur, il faut créer une base psql (ou lancer un Docker).
(Je ne maîtrise pas Docker, j'ai préféré resté sur une base de données réelle)

# 1 Créer la base

Créer une base de données ALTEN_SHOP :

``` postgresql
sudo -i -u postgres
psql

CREATE DATABASE alten_shop;
```

# 2 Lancer l'appli

Un User (avec le rôle ADMIN) est déjà créé avec les id/pwd suivants ("joss@gmail.com", "joss").
De même, les produits du fichier products.json sont insérés.

# 3 S'authentifier 

Appeller la ressource http://localhost:8080/auth/authenticate, avec le body suivant :

{
    email : "joss@gmail.com",
    password : "joss"
}

Vous recevez alors un token ADMIN qui vous permet :

1) de créer un nouveau User qui aura le rôle USER :
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


