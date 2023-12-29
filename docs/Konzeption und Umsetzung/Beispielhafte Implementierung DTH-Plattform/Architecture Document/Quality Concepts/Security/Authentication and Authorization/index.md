# Authentication and Authorization

- _KST Platform_ uses  **OAuth 2.0 Access Tokens** to enable the following possibilities:  
  - **Client Credentials:** on behalf of a system.
  - **Auth Code Flow:** on behalf of the Data Owner.
- We use  **scopes** to limit the power of an Access Token (see OAuth2 Scopes subsection below).
- We use  **roles**  and  **consents** to restrict data access on the data item type level and operation level (provide and/or consume).
- For any data access (provide data or consume data) we require a valid consent for the requestor. Else, we will inhibit the access. The only allowed action without a valid consent is to create a consent request.
- The  data owner  decides whether to accept or decline a consent request. In case the data owner accepts the consent request, a consent will be established.

## Roles

We differentiate four different kinds of accounts:

- **Owner**: A data owner is the data subject (in terms of the GDPR) and has the "ownership" over his/her data.
- **Consumer**:  A data consumer  only consumes data from the data trustee. The consumption may serve different purposes, like for example, providing a service to the data owner.
- **Provider**:  A data provider only provides data to the data trustee.
- **Prosumer**: A data prosumer is both a consumer and a provider of data.

Each account on  _KST Platform_  is assigned one of those roles. Based on the assigned role, the data is filtered in  _KST Platform_ so that: a data owner can only see their own data; a data consumer can only see the data for which a valid consent for data consumption is given by a data owner; and a data provider can only provide data to a data owner if there is a valid consent for providing data of this data Item type.

Roles are also used to avoid unwanted access to the  _KST Platform_ frontend (**Owner-UI**)  by account types other than  **Owners** (such as a Consumer, a Provider, a Prosumer).  Currently, Consumer, Provider, and Prosumer account types are not supported for user accounts by  _KST Platform._ The roles other than Owner are only assigned to service accounts of a client ,a system with client credentials, not to users.  

**Attention!**: Roles other than "OWNER" MUST NOT be assigned to user accounts as this would cause serious security issues (unauthorized data access). Role "CONSUMER", "PROVIDER" or "PROSUMER" may only be assigned to the service account of a client (system with client credentials).

## OAuth2 Scopes

We offer a dedicated set of APIs (that support certain operations) for each type of client applications (consumer, provider, and prosumer). Each type of client applications can only use the corresponding APIs. Those APIs are tagged by the following pattern:  **<_client-type_>.<_resource_>**. For example: endpoints tagged with **_prosumer.consent_**  are dedicated for consent management (request a consent, get the requested consents, etc.,). For each tag there exists a scope with the following naming pattern:  **<_client-type_>.<_resource_>:<_permission_>;** whereas permission can be either  **read**,  **write** or  **manage** (read & write). The KST Platform backend is configured to only accept those requests where an appropriate scope is included in the access token (this is done using spring security). Therefore clients can only access those endpoints where a scope has been configured in our oauth2 authorization server. This allows us to restrict the operations a client can do with its access token.

Furthermore,  _KST Platform_  offers a dedicated client type (Owner client type) and a set of scopes for the KST Platform frontend (The Owner-UI). The KST Platform frontend allows the data owner to manage his/her consents, view logs of the consumed and provided data, ...etc. Those scopes, and the correspoding endpoints, should not be used by other types of client applications like for example a data consumer, and therefore should not be configured/assigned to such applications.

For a complete list of all scopes, check the SETUP.md file in the git repository of the KST Platform: [TODO](<>).

Attention: Even though the access token has the required scope, the KST Platform backend may still reject some calls to its REST-Endpoints because these calls require a valid consent from the affected data owner to work (see [Consent Management](<../Consent Management/>)).
