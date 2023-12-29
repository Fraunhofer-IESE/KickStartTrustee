# Demo Use Cae (1) KST Basic FMIS

## Scenario Flow

The following diagram illustrates the use case flow:

![Scenario Flow](<../SVG/Use Case 1 - Flow.svg>)

## Prerequisite

Onboarding  _KST Basic FMIS_ (once): _KST Basic FMIS operator_  should register  _KST Basic FMIS_ as a client at the  _KST Platform_. Thus, the  _KST Platform_  operator should configure a public client  for  _KST Basic FMIS_. See [TODO]().

## First-Time Usage

A consent to authorize  _KST Basic FMIS_  to prosume (i.e., provide and consume) the farmer's data via  _KST Platform_ should be established upon the first usage of _KST Basic FMIS_.

To use the features offered by  _KST Basic FMIS_, the farmer has to connect it with their account on the  _KST Platform_  upon the first usage (i.e., the farmer should have an active consent for  _KST Basic FMIS_ on thier  _KST Platform_ account), as described in the following:

1. The farmer signs in to  _KST-FIMS_ using their _KST Platform_ account (_OAuth 2.0 Authorization Code Flow_):  
    - _KST Basic FMIS_ redirects the farmer to the KST Platform.  
    - The farmer enters their _KST Platform_ account credentials.
    - _KST Platform_  will redirect the user back to  _KST Basic FMIS_.  
2. When using _KST-FIMS_  for the first time, _KST Basic FMIS_ prompts the farmer to configure the authorization level when connecting to their account on the  _KST Platform_. The farmer can choose one of the following:
    1. Basic Access: With this level, the  _KST Basic FMIS_  can:
        - Provide field data to the farmer’s account on the  _KST Platform_.
        - Consume field data from the farmer’s account on  _KST Platform_.
    2. Full Access: With this level, the  _KST Basic FMIS_ can:
        1. Get Basic Access (see a).
        2. Provide disease report data to the farmer’s account on the  _KST Platform_.
        3. Consume disease report data, and disease warnings data from the farmer’s account on _KST Platform_.
3. _KST Basic FMIS_ sends a consent request to the farmer’s account on  _KST Platform_  based on the selected level of authorization (in step 2).
4. _KST Basic FMIS_  redirects the farmer to the  _KST Platform_.
5. The farmer logs into their account on the  _KST Platform_.
6. The farmer (re)views the consent request details and accepts (or rejects) the request.
7. _KST Platform_ redirects the farmer to _KST Basic FMIS_ where he/she can view the dashboard, and use its features based on the authorization level, if he/she has provided the consent in step 5 (otherwise they will be redirected to the _KST Basic FMIS_ landing page).

_KST Basic FMIS_ provides a feature that displays the current consent established with  _KST Platform_  and supports in revoking it. The farmer can view and revoke their consents at  _KST Platform_  as well. Once there is no active consent anymore, _KST Basic FMIS_ can no longer provide or consume the data of that farmer and the farmer is treated as a first-time user of the service (step 2).

## Subsequent Usage

1. Sign in to  _KST-FIMS_ using your credentials on  _KST Platform (__KST Basic FMIS_  redirects the user to the  _KST Platform,_ OAuth 2.0 Authorization Code Flow, where he/ she can enter their credentials, and then  _KST Platform_ would redirect the user back to  _KST Basic FMIS)._
2. The  _KST Basic FMIS_ consumes the required data from the farmer's  _KST Platform_  account (based on the previously granted consent level).
3. The farmer can  continue using the features offered by  _KST Basic FMIS_.
