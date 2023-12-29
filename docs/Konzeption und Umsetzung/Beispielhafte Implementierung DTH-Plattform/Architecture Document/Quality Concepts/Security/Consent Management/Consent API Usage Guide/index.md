
# Consent API Usage Guide

In the following we describe in details the API and its usage of our consent management implementation in _KST Platform_.

## Consent Requests

- A consent request is sent to the KST Platform data owner account. Thus, it contains the id of the data owner. E.g.,:

    {"ownerId": "efb46c03-43af-4158-9c04-184814720898"}

- A consent request is sent for specific Data Item Type(s) that are supported by the KST platform (e.g., Field Data, Seeding Data, Harvesting Data). E.g.,

    {"ownerId": "efb46c03-43af-4158-9c04-184814720898",

    “dataItemType”: ["Field Data", "Seeding Data"]}

- A consent request can be for different data flows:
  - Providing Data.
  - Consuming Data.
  - Prosuming Data.

    E.g.,

    {"ownerId": "efb46c03-43af-4158-9c04-184814720898",

    “consumedDataItemTypes”:  ["Field Data", "Seeding Data", "Irrigation Data"],

    “providedDataItemTypes”: ["Harvesting Data", "Irrigation Data"]}

- A data owner can:
  - Accept a consent request.
  - Reject a consent request.

- The consent requester can:
  - Retract a consent request as long as it has not been accepted or rejected by the data owner. Consent requests for established consents can not be retracted by the original consent-request creator. They can only be revoked by the data owner  

- A consent request can have one of the following states:
  - Pending.
  - Accepted.
  - Rejected.
  - Retracted.
  
- A consent request as seen by the data owner contains the following information:
  - Requester: the sender system
  - Request Time.
  - A list of consumed data item types
  - A list of provided data item types
  - Data Usage Statement: text clarifying how the data is processed and stored.
  - Purpose: {research, service, …}.
  - State: {pending, accepted, rejected, retracted}.
- The data usage statement and the purpose are mandatory fields for data consumers.
- The data usage statement and the purpose may be null/not set for consent requests of data providers, because in this case these fields do not make sense.
- The data usage statement and the purpose are optional/ not-required in the schema for consent requests sent by data prosumers. However, if the list of consumed data item types of the request is not empty, the data usage statement and the purpose have to be provided, otherwise, there will be an "Invalid Request Error". This check is made at runtime, when a data prosumer tries to create a consent request.  

## Consents

- If a consent request is accepted by the Data Subject, an active consent is created.
- Data Subject can:
  - Revoke an active consent.
- A consent can have one of the following states:
  - Active: a consent  request  has been accepted and the respective consent has not been revoked.
  - Revoked.
- A consent contains the following information:
  - Requester: the sender system
  - Creation Time: the time of the consent request acceptance.
  - A list of consumed data item types
  - A list of provided data item types
  - Data Usage Statement: text clarifying how the data is processed and stored.
  - Purpose: {research, service other}.
  - Status: {active, revoked}.
- The data usage statement and the purpose may be null/not set for consent of data providers, because in this case these field do not make sense.

## Redirects in KST Platform Frontend

The consent request and consent details page have an optional query parameter "redirect_uri". This parameter accept an URL and after accepting or rejecting the consent request or revoking the KST Platform Frontend redirects the user this URL. Third party applications from data providers, data consumers or data prosumer can use this parameter to integrate the consent creation/revocation flow into the application to achieve a nice user experience for the data owner.

## KST Platform backend Internal Data Model

The KST Platform backend internal data model for consents differs from the model the consent API exposes. The data model stores the granted permissions a map with dataItemTypes as keys and a set of operations (provide, consume) as value:

`public`  `class`  `Consent {`

`...`

`private`  `Map<DataItemType, Set<Operation>> permissions;`

`...`

`}`

The reason for this is performance and maintainability, because storing the permissions in such a map, we can do many check faster and with less code. In particular we don't have to iterate through the list of data items upon each request data item consumption or provision request. This is done under the assumption that on average the number of data item consumption and provision requests is much higher then the API requests for consents.

The conversion between the API data model and the internal data model is done upon creation/updating or retrieving a consent via the API, therefore if you encounter performance issues with the consent API it is advised to cache the consents in you application and evict the cache if you get an DataItemNoPermissionException error from the data item API.
