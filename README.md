# wp-merchant

Partial implementation of the [wp-merchant api](https://app.swaggerhub.com/apis/erlendaakre/wp-merchant/1.0.2) (See swagger.yaml)

## Instructions
Run with `sbt runAll`

## Notes and assumptions

* This implementation covers the basic functionality in the API specification to allow for
  the following goal "Allowing a merchant to create a new simple offer".
* An offer does not have it's own description, but the item that is part of the offer does.
