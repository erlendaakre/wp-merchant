# wp-merchant

Partial implementation of the wp-merchant api described:
* on [swaggerhub](https://app.swaggerhub.com/apis/erlendaakre/wp-merchant/1.0.2)
* and in the `swagger.yaml` file

## Startup 
Start up http server with `sbt run`

rest api is now running at <http://localhost:8080> 

## Usage

### Get non-existing merchant
curl http://localhost:8080/api/v1/merchant/1701

### Create merchant
curl -H "Content-Type: application/json" -X POST http://localhost:8080/api/v1/merchant -d '{ id: 0, "name": "A merchant" }'

This returns the newly created merchant, this id can then be used with the get request above to see it working

## Notes and assumptions

* This implementation covers the basic functionality in the API specification to allow for
  the following goal *"Allowing a merchant to create a new simple offer".*
* The API created for the Merchant is more extensive than required by the task, and allows for general use of the merchant/offer/item system if there are multiple merchants.
* An offer does not have it's own description, but the item that is part of the offer does.
* Item and Offer are separate entities, Items are not directly tied to Merchants, for a real system they might be tied to Merchants or left unattached.
