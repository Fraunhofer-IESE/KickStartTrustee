# Client Management

Clients are often companies providing applications or services to an end user or to other companies. They may as well be universities or research institutes or other public authorities that analyze the data to gain meaningful insights. To access the data trustees APIs a client has to either use an existing application or create a new one.

The system supports three types of operations for an application:

- Data Provider
- Data Consumer
- Data Prosumer (a Prosumer provides and consumes data items)

Those operation types are mutually exclusive, therefore each application has to choose exactly one type.

In general, applications running on an end user device can not be trusted to keep a secret secure. Therefore, a client has to specify for each application whether it is an application running on the user device (like, for example, a Single Page Web Application) or is a (potential non-interactive) backend service running on the client's server (or a third party trusted by the client, like a public cloud provider). Based on this information, the data trustee operator has to create the OAuth2 client in Keycloak for each application.

## Single Page Application

If the application is running on the users device as Single Page Web Application, the OAuth2 Authorization Code Flow has to be configured. The application has to provide a way to sign in with the KickStartTrustee account to obtain an OAuth2 token for the data trustee platform. This token will always have the rights of the user that has signed in. Therefore, the application will only be able to consume or provide data items for exactly this user. There is no need to assign a role for a single page application, because the role in the token for a single page application will always be owner.

Based on the operation type above, the corresponding scopes have to be added to the client as default scopes. Don't assign any owner scopes (prefixed with owner) to external Single Page Applications, those scopes are only intended for applications provided by the KickStartTrustee platform operator itself for the data owner (like for example the owner-ui). For a complete list of scopes see the [Setup Guide](SETUP.md).

After finishing the configuration you need to hand over the client id to the application provider.

## Backend Service

If the application is running on the a server of the client (or a third party trusted by the client), it can keep a secret secure. In this case the OAuth2 Client Credentials flow has to be configured. In addition to this, the corresponding role based on the customer's choice of operations has to be assigned to the service account of this client.

Based on the operation type above, the corresponding scopes have to be added to the client. Don't assign any owner scopes (prefixed with owner) to external backend services, those scopes are only intended for applications provided by the KickStartTrustee platform operator itself (like for example the owner-ui). For a complete list of scopes see the [Setup Guide](SETUP.md).

After finishing the configuration you need to hand over the client credentials (client id & client secret) to the application provider.
