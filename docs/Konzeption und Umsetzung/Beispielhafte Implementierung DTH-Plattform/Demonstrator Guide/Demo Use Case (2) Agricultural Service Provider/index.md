# Demo Use Case (2) Agricultural Service Provider

## Story

- Subcontractor "Seeder Simon" as a prosumer client: the system of a seeding service provider that consumes Field Data, and provides Seeding Data (seeding work records).
- Subcontractor "Weeder Willy" as a prosumer client: the system of a weed control service provider that consumes Field Data and Seeding Data and provide Weed Control Data (Weed Control Work Records).

## Realization

The behaviour of both Seeder Simon and Weeder Willy systems is implemented via Postman. The Postman Collection can be accessed and downloaded at:  [TODO]().

## Required Setup

The demo scenarios assume that both subcontractors ( "Seeder Simon" and "Weeder Willy") use some server based system to manage their contracting jobs and also store the data they collect during those jobs in the same system. Therefore before the first use of the postman collection you need to create two OAuth2 clients with client credentials enabled and assign the PROSUMER role to both service accounts . The demo postman collection includes an example environment named ExampleEnv. In order to use this example environment you need give the OAuth2 Clients the following client ids:

- Seeder Simon" clientId: seeder-simon-service.
- "Willy Weeder" clientId: willy-weeder-service.

You also need to obtain the client secrets for both clients and set them in the example environment. For more guidance and details on how to setup backend service clients see  [TODO]().

## Use Case Flow (How to run the script?)

- **Note**: This demo is using the farmer "**owner**" account, and the field "**Mein Testfeld 1**". To change that:

  - go to the collection, root element ("KST Agricultural Service Provider Use Case").
  - navigate to "Variables" Tab, and configure the "current value" of the corresponding variables:
    - "farmerOwnerId"= efb46c03-43af-4158-9c04-184814720898
    - "fieldId"= Bx574
- Before executing the script, you might want to check the state of the consents/ consent-requests already present for the  **"owner"**  user account in the owner UI. Maybe revoke some of them.
- Activate the "Seeder Simon" environment.
- Go to the collection, root element ("KST Agricultural Service Provider Use Case")
  - navigate to "Authorization" Tab.
  - scroll to bottom.
  - click get new access token.
  - click use token.
- Go to Seeding Service Provider Folder
  - execute the requests in the prepared order.
  - once the consent request is created, go to the owner UI and accept the consent request. The farmer for the use case is user "owner".
  - the last request is to provide data.
- Activate the "Willy Weeder" environment.
- Go to the collection, root element ("KST Agricultural Service Provider Use Case")
  - navigate to "Authorization" Tab.
  - scroll to bottom.
  - click get new access token.
  - click use token.
- Go to Weed Control Service Provider Folder.
  - execute the requests in the prepared order.
  - once the consent request is created, go to the owner UI and accept the consent request. The farmer for the use case is user "owner".
  - there are requests to consume data, including the seeding work record provided by seeder simon.
  - the last request is to provide data.
- Done, the weed control work record has been provided

## FAQ

- The request failed with status code 401:
  - The token is expired, get a new one:
    - Go to the collection, root element ("KST Agricultural Service Provider Use Case")
      - navigate to "Authorization" Tab.
      - scroll to bottom.
      - click get new access token.
      - click use token.
  - Retry the request that failed.
- The request failed with status code 403:
  - Make sure that the consents are arranged correctly.
- The request failed with status code 400:
  - There is something wrong with your request. Check the URL and payload. E.g. dataItemType not supported, parameters/attributes missing, ... ,etc.
- The request failed with status code 409:
  - There is already an active consent or a pending consent-request. Revoke the existing active consent or accept/decline the existing pending consent-request in the owner UI.
