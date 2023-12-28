# KickStartTrustee Platform

## Goal

_KST Platform_  is a Data Trustee platform which regulates the data flow between farmers and agricultural service providers to ensure the data sovereignty right for farmers. Furthermore,  _KST Platfom_ offers its users (farmers) a service (the  _Disease Warning Service_) that enables farmers to report an infection/ disease on one of their fields and anonymously broadcasts a warning to other farmers who are subscribed to the service and have fields in the area that might be in danger.

## Benefit

_KST Platform_ serves as a datahub which provides the following benefits for the farmers:

- **_Data integrity_**:  Using  _KST Platform_, field data is no longer fragmented across multiple platforms. Thus, the farmer has access to a holistic overview of all fields’ related data.
- **_Interoperability and Preventing Vendor Lock-in_**: Using  _KST Platform_, fields related data can be fetched and updated by other parties (e.g., third-party agricultural service providers). Thus, the farmer does not have to worry about exporting and importing the data from one system to another along with the hurdle of reformatting and/or converting the data to different formats.
- **_Access Control_**: To provide/consume/prosume data, an agricultural service provider has to request the consent of the data owner (the farmer). The access is provided only when a valid consent is present. Furthermore, the farmer can revoke a consent at any point in time. Thus, the farmer has the full control over their data.
- **_Transparency_**: The farmer can track  **who** accessed **which** data items, **when**, and for which **purpose** (create, read).

The _Disease Warning Service_  offred by  _KST Platform_ provides the following benefits for the farmers:

- The reporter farmer can store the history of diseases/ infections of their fields in a sovereign manner.
- _KST Platform_ creates disease warnings based on a disease report leaving out any information that could be used to trace back the exact location of the infected field and the data of the reporter.
- _KST Platform_  generates warning notifications only for the farmers who have fields in a certain range near the infected field.
- The notified farmers can quickly react and take effective measures/ precautions to prevent such infection in their fields.

## Roles

1. **Data owner** (Human): The data subject (in this case, the farmer).
2. **Operator** (Human): The Admin of  _KST platform_.
3. **Agricultural service provider** (System): A provider of a digital service (e.g., a data preparation app, a recommendation service) and/ or a physical service (harvesting service, seeding service, weed control service). The agricultural service provider can act as a:
    1. **Data Provider**: Only provides data to the _KST Platform_ account(s) of one or several data owners.
    2. **Data Consumer**: Only consumes data from the _KST Platform_ account(s) of one or several data owners.
    3. **Data Prosumer**: Provides data to- and consumes data from- the _KST Platform_ account(s) of one or several data owners.

## KST Platform Characteristics

**Control Flow and Data Flow:**  _KST Platform_ supports the following flows:

- Data Provider (Push Data To)  _KST Platform_.
- Data Consumer (Pull Data From)  _KST Platform_.
- _KST Platform_ (Push Emails To) Data owner Mail Server.

**Data Storage:**

- _KST Platform_ supports Centralized Data Storage.
- _KST Platform_ stores the following data item types: Field Data, Seeding Work Record, Weed Control Work Record, Disease Report, Disease Warning.

**Data Sovereignty:**

For Data Owners (Data Subjects):

- _KST Platform_ supports access control; access is only granted to a Data Consumer/Provider/Prosumer if there is a consent by the Data Owner (Data Subject).
- A data consumer/provider/prosumer can create a consent request, which can be accepted or rejected by the data owner. If the consent request is accepted by the data owner, an active consent is created.
- Consent requests should have a clear description: Requester, Affected Data Item Types, Activity (Provision, Consumption, Prosumption), Data Usage Statement (e.g., the data will be used by our software to provide the requested service, the data will be deleted after 30 days, the data will not be shared with third parties), Purpose (Research, Service,.. ).
- The data owner can revoke an active consent at any point in time.
- The data owner can delete their account on  _KST Platform_, and thus all their data stored on the platform will be deleted (i.e., profile data, data items, events, consenst and consents requests).
- The data owner can delete a data item at any point in time.
- The data owner can  set and update the desigerd configuration of the  _Disease Warning Service_:  Provide their consent to contribute with disease reports  and/or provide their consent to receiving warnings. If the farmer provided a consent to receive warnings, they can also provide consent to receiving warnings emails.
- The data owner can choose to mark a particular disease report as "Confidential" and thus exclude the report from being used by the _Disease Warning Service_.

For Data Providers/Consumers/Prosumers:

- Data Provider/Consumer/Prosumer can retract a consent request as long as it is not accepted or rejected by the data owner.

**Data Processing:**

- The  _Disease Warning Service_ creates Disease Warnings based on parts of the information provided in a Disease Reports leaving out any information that could be used to trace back the exact location of the infected field and the data of the reporter.
- _KST Platform_ does not perform any data processing on other stored data item of the other data item types.

**Transparency:**

_KST Platform_ logs the following events:

- Consent-related events: Consent creation, consent revocation. [Note: Consent-request-related events are not logged currently].
- Data-related events: Data provision, data consumption and data deletion events.

The data owner  can view the details of those events along with all the other infromation stored on  _KST Platform_ via its UIs (Owner UI):

- Consent requests along with their status [Pending, Accepted, Rejected, Retracted].
- Consents along with their status [Active, Revoked].
- Data Items.
- _Disease Warning Service_ configurations (chosen settings).
