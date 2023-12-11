#language: en

Feature: Test differents authorizations

    Scenario: Get an ADMIN and register a new User

          Given I authenticate as ADMIN with the tuple : {email : "<email>", password : "<password>"}
          When I want to register a new AuthorizedUser who will have a USER Role
          Then I retrieve a USER token
          Then my new User has the USER role, and can access to the products list

          Examples:
               | email              | password      |
               | joss@gmail.com     | joss          |