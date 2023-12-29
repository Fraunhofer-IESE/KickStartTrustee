# Technical Debts, Limitations, and Possible Improvements

## Technical Debts

- We use Spring Boot 2.7. Support for Spring Boot 2.7 ends in November 2023, so there is a need to upgrade to Spring Boot 3.X.
  - We decided not to do that because the project ends in December 2023.

- KST Platform differentiates the roles "OWNER", "CONSUMER", "PROVIDER", "PROSUMER" (see  [Authentication and Authorization](<../Quality Concepts/Security/Authentication and Authorization/>)). Roles other than "OWNER" MUST NOT be assigned to user accounts as this would cause serious security issues (unauthorized data access). Role "CONSUMER", "PROVIDER" or "PROSUMER" may only be assigned to the service account of a client (system with client credentials).

## Limitations and Possible Improvements

### Consent Mechanism

- Our system only supports fixed immutable consents.
- Our system does not allow the sender of a consent request to retract an active consent. Only the data owner can revoke an active consent.
- Our system is designed to allow the data owner to give consent at the  _Data Item Type_  level. There is currently no support for giving consent at the  _Data Item_  level. Thus, if a data owner gives consent to a data consumer to consume data of type X, all instances (i.e., all data items) of type X can be consumed based on the given consent.
- Our system is designed to support only one active consent per user/client pair at any given time. I.e., if there is an active consent in the system for user x/client y, a new consent request from client y to user x will be rejected by the system (with error code 409: a conflict exists), even if the new consent request is sent for a different data item type than the current active consent. Therefore:
  - To upgrade/downgrade an active consent for KST Basic FMIS, the owner must first revoke the current active consent and trigger the sending of a new consent request.
  - If an active consent exists, a service provider can't send a new consent request to the same data owner, even with different properties (e.g., data element types), unless the data owner has revoked the active consent.

### Data Storage

- In our system, we use Spring and MongoDB to store JSON objects. This limits the request size for the data item and is therefore not suitable for use cases that deal with large data items (more than 16MB). The upper limit of data records size is 16MB:

- - 2MB (Spring default for the request body payload). This limit can be raised to meet the MongoDB limit or more.
  - 16MB (MongoDB limit). This limit is the actual reason of our system's upper limit.
- Our system supports the storage of structured data only. Currently, we do not support unstructured data storage.
- Our system does not examine the content of stored records, and therefore is unaware of any relationships between stored records.  
  - Thus, deleting a data item would not affect other related items (no on-cascade deletion is supported).
  - For example, if a field has been deleted for which disease reports exist, when using _KST Basic FMIS_ to retrieve the information of a related disease report, _KST Basic FMIS_  will not be able to retrieve the name of the corresponding field from _KST Platform_  (since the field data item has been deleted). In our implementation,  _KST Basic FMIS_  displays the disease report information with a missing value for the field's name.
- When a data item is deleted, its ID is also deleted. Attempting to retrieve the data item details based on a previous data provision/consumption event would result in a not found (404 error code). Therefore, there is currently no way to determine whether a not found data ietm with a particular ID has been deleted or was never stored on  _KST Platform_.

### Legal Concerns

- In our demonstrator, we have no mechanism to ensure that the farmer owns the fields he/she claims to own. Currently, with _KST Basic FMIS_, any farmer can create a field and store it in the  _KST platform_  as the owner. This can lead to legal issues, for example:
  - A farmer can request a service from a service provider on a field that he/she does not actually own.
  - One can exploit this vulnerability by creating many fields and claiming to own them in order to reveal data related to a disease report (creating many fields in an area to discover the location of the infected field).

So the legal question would be: should the data trustee verify that the fields created by a farmer actually belong to that farmer? and how to do so?  
Suggestion: ask the farmer to confirm ownership when creating a new field. This could be achieved by adding a checkbox in  _KST Basic FMIS_  with the phrase: "I hereby confirm that I own this field .. etc.".

- There is no way a user can receive all its personal data at once in structured machine readable format. This is required by GDPR (Art. 20 GDPR: Right to data portability see  [https://gdpr-info.eu/art-20-gdpr/](https://gdpr-info.eu/art-20-gdpr/)), therefore the platform is not fully GDPR compliant.

### Data Consumption

- When a client consumes data, the response currently contains all records of the requested data item type. Selective consumption is possible, but only if the consumer (or prosumer) provides the dataItem ID. The ID is stored in our system, but is not currently used for data consumption.
  - Possible solution: Introduce an endpoint to request the "metadata" of the data along with the data item ID. The data consumer (or prosumer) can first call this endpoint to get the ID of the targeted data item, and then specify this data item ID when consuming the data in the following request. Note: in our implementation we do not reveal much in the metadata, this can be enriched to be more helpful. THUS: we can add a note to the user of our code (the developer of a domain-specific data trustee) to enrich the metaData in a way that would support their use case.
  - Note: in our syetem, the metadata is stored in a different collection by design which can efficiently support the points mentioned above.
- The data provider is part of the metadata of the data items stored in our system. This could be considered data leakage.

### Disease Warning Service

- Data access of the Disease Warning Service is not logged in the audit logs.
- Disease reports are not semantically validated. Only the syntax and the field presence are checked. No checks are done to prevent sending a disease report with a date in the future or with an unexpected severity value (other than: low, medium, high).
- The Disease Warning Service does not generate disease warnings for the fields of the farmer who created the disease report. That is, if the reporting farmer has a field at risk based on his/ her report, he or she will not be notified.

### Logging

- Our system does not log the following events: Consent request rejection, consent request retraction, Consent revocation.
