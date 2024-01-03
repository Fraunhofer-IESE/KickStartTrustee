# Consent Management

One of the key features of our  _KST Platform_  is that a data owner is always in control over their data. Therefore, we have developed the **Consent Mechanism**  which allows a data owner to be in control over their data.

## The Consent Mechanism

To allow any type of data access (for a third-party application  to provide data and/or consume data) on  _KST platform_, there should be a valid consent (valid means: active and provides the required level of permissions) for the requester (third-party application). Otherwise,  _KST platform_  will prevent the access.

- In order to establish a valid consent:
  1. The requester should create a consent request.
  2. The Data Subject should check the consent request and accept it (if he/she wishes to).
- The only allowed actiona by a third-party application without a valid consent are: creating a consent request, and retracting pending consent requests.

## Types of Consent Requests and Consents

- A  **Consent Request**  can be:
  - **Fixed**: The request is made for specific set of permissions as a block (as a” take it or leave it” offer). It can be either accepted as-is or rejected as a whole.
  - **Negotiable**:
    1. Medium level of freedom: The request consists of two parts: mandatory part with a mandatory set of permissions (that should be provided in order to reach an agreement); and an optional part with the optional set of permissions (that could be rejected).
    2. High level of freedom: The request is made for a set of permissions that can be configured as desired by the data subject (i.e., all is optional, no mandatory part). Optionally you can allow the requester to predefine a suitable selection.

Note: the consent request can be fixed at the Data Trustee Platform, however, a negotiation could take a place at an earlier stage outside the context of the consent request flow on Data Trustee Platform. E.g., in our demo Basic FMIS provides the possibility to choose between two types of consent requests with different levels of permissions (basic vs full). Thus, the consent request sent to the  _KST Platform_  is fixed based on the farmer's choice, but a negotiation has taken a place on the KST Basic FMIS.

- A  **Consent** can be:
  - **Mutable**: The consent can be configured even after it has been granted. E.g., the data subject can edit the granted permissions of the optional part of a _negotiable consent request_.
  - **Immutable**: Once the consent is granted, it cannot be configured anymore. It can only be revoked. E.g., once a  _negotiable consent request_  is configured and accepted, the data subject cannot edit the granted permissions of the optional part anymore. However, the whole consent can be revoked.

Depending on the use case and the context, one or the another variant may be more appropriate.

**In our _KST Platform_  we support: Fixed Consent requests, and Immutable Consents. In addition the _KST Platform_ does not enable a providers, consumer or prosumer to have multiple active consents for a user (a data owner).**

### Realization in the KST Platform

- Each data item in our  _KST Platform_  is of a certain type.
- Consents in  _KST Platform_  limit the access on a data item type level (not on individual data items level).
- A data owner can only accept/reject a consent request as-is and revoke a consent as-is since we only support  fixed consents (no optional part or free choice).
- Consents are immutable after creation.

## Detailed Information

In the  [Consent API Usage Guide](<Consent API Usage Guide/>)  page, you can find the details of our API and how to use them.

In the  [Consent Flows](<Consent Flows/>)  page, we describe how an application can obtain the data of a user from the  _KST Platform_  by establishing a consent.
