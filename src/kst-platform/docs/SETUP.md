# Setup

## Keycloak

### Clients

The following clients needs to be created in keycloak (preferable using the keycloak admin ui):

- create a confidential client with the id `kickstarttrustee-server`. This client needs read access to the keycloak REST-API users endpoint.
- create a public client with the id `swagger-ui`. This client should be able to use the proof key for code exchange and is intended to be used by developers using the swagger-ui.
- create a public client with the id `owner-ui`. This client should be able to use the proof key for code exchange and is intended to be used by data owners using the data owner ui.

### Client Scopes

The following client scopes need to be created in keycloak (preferable using the keycloak admin ui):

- `consumer.consent:manage`
- `consumer.data:read`
- `owner.audit:manage`
- `owner.consent:manage`
- `owner.data:manage`
- `owner.profile:manage`
- `prosumer.consent:manage`
- `prosumer.data:manage`
- `provider.consent:manage`
- `provider.data:write`

The owner scopes (`owner.*:manage`)  scopes should *only* be assigned to the owner-ui and the swagger-ui client!

### Realm Roles

The following realm roles should be created:

- OWNER: role for users, which are data owners
- PROSUMER: role for service account users, which are data providers and consumer
- PROVIDER: role for service account users, which are only data providers
- CONSUMER: role for service account users, which are only data consumers

Attention: Never assign PROVIDER, PROSUMER or CONSUMER roles to normal users of an SPA-Application. This can lead to a security problem!

### Groups

The following groups should be created:

- Owners: All users in this group should be assigned the OWNER role
- Prosumers: All users in this group should be assinged the PROSUMER role. In this group there should be only service account users!
- Providers: All users in this group should be assigned the PROVIDER role. In this group there should be only service account users!
- Consumers: All users in this group should be assigned the CONSUMER role. In this group there should be only service account users!

The Owners group should be configured as default group for user registration. For service account users you need to remove the Owners group explicitly to prevent them having the OWNER role!

## Server

The server uses spring boot, therefore the following properties (from the keycloak setup steps) needs to be set using the usual spring boot mechanism for settings properties. Please refer to the spring boot documentation on how to do this.

- KEYCLOAK_ADMIN-CREDENTIALS_CLIENT-SECRET: needs to be set to the client secret of the keycloak client with the id `kickstarttrustee-server`
- OAUTH2_SERVER: Needs to be set to the keycloak URL (defaults to http://localhost:8080)
- OAUTH2_SERVER_REALM: Needs to be set the keycloak realm (defaults to `master`)

You can run the server on your local machine using: `./gradlew bootRun`. The swagger-ui of the server can be reached at http://localhost:8090/swagger-ui.html.

## UI

The ui uses Node.js, npm and react. All configuration properties are located in the config.json file in the public folder:
 
- keycloak url (the base url of the keycloak)
- keycloak realm
- keycloak clientId
- kst api url (the base url of the kickstarttrustee server)

For the UI docker container those properties can be set using environment variables. For the end user using the UI, those properties will only become active after a page reload.
