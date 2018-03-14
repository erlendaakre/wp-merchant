# wp-merchant

Partial implementation of the wp-merchant api described:
* on [swaggerhub](https://app.swaggerhub.com/apis/erlendaakre/wp-merchant/1.0.2)
* and in the `swagger.yaml` file

## Startup 
Start up lagom with `sbt runAll`

view the service gateway at <http://localhost:9000> to confirm all services are running:
* Merchant service should be running on port 55045
* Item service should be running on port ????
* Offer service should be running on port ????

## Usage

### Get non-existing merchant
curl http://localhost:9000/api/v1/merchant/e63cc5c2-9d41-4e04-9f57-92c35343f1a6

### Create merchant
curl -H "Content-Type: application/json" -X POST http://localhost:9000/api/v1/merchant -d '{ "name": "A merchant" }'

This returns the id of the created merchant, this id can be used with the get request above

## Notes and assumptions

* This implementation covers the basic functionality in the API specification to allow for
  the following goal *"Allowing a merchant to create a new simple offer".*
* The API created for the Merchant is more extensive than required by the task, and allows for general use of the merchant/offer/item system if there are multiple merchants.
* An offer does not have it's own description, but the item that is part of the offer does.
* Item and Offer are separate entities, Items are not directly tied to Merchants, for a real system they might be tied to Merchants or left unattached.
