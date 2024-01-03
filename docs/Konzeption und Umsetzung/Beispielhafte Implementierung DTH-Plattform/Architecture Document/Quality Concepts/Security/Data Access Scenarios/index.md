# Data Access Scenarios

Applications need an access token to access the endpoints of the _KST platform_. In accordance with the OAuth 2 terminology, we call those applications "clients". Clients need to be registered at the  _KST platform_  (or to be more precise: in the  _KST platform_'s Keycloak instance by a  _KST platform_  admin).

The OAuth 2 protocol supports several flows. Flows are ways to retrieve an access token. In the  _KST platform_  we use the following two flows:  **Client Credentials Flow**,  **Authorization Code Flow**. In the  _KST platform,_  we do consider other flows, like for example the Device Flow (similar to the Authorization Code Flow).

With this technical background, we can differentiate the following scenarios for data access/ getting an access token that can be used to access data:

- **No user interaction is required to get an access token (Client Credentials Flow):**
  - Software Component (configured with client credentials)  accesses the data of a Data Subject on the  _KST platform_.
    - Example:  JD-Tractor#123 (running an instance of JD-Tractor software) pushes seeding work records to Bill-the-farmer account on  _KST Platform_.
      - Consent should be given only for (client:  JD-Tractor#123 software instance).
    - Example 2:  JD-Cloud Backend pushes seeding work records (e.g., JD Robots send the seeding records to JD-Cloud, that would then send it to the KST) to Bill-the-farmer account on _KST Platform_.
      - Consent should be given for only (client:  JD-Cloud Backend).
    - Example 3: Hospital X is running their instance of  Hospital4U Information System and they push the medical data to Patient Z account  on  _KST Platform_.  
      - Consent should be given only for (client: Hospital4U Information System run by Hospital X). Note: (client: Hospital4U Information System run by Hospital Y) should not be granted access.

- **User interaction is required to get an access token (Authorization Code Flow):**
  - **User is the Data Subject (whose data is to be accessed)**
    - Data Subject using a Third-Party Software to access their data on the  _KST Platform_.
      - Example:  Bill-the-farmer is using Basic FMIS to create a disease report and get the disease warnings on his account on  _KST platform_.
        - Consent should be given for only (user:  Bill-the-farmer, client: Basic FMIS). I.e., (Alice-the-farmer, Basic FMIS) or (Bill-the-farmer, Advanced FMIS) should not be granted access.
  - **User is not the Data Subject (data of other users is to be accessed)**
    - **User is an individual (an employee with a dedicated account)**
      - Company Employee using their Own Account on their Company Software to access the data of a Data Subject on the  _KST platform_.
        - Example:  Bob-the-agronomist is using Bob-the-agronimist account on  Agronomist4U  to access the field data of Bill-the-farmer on  _KST Platform_.
          - Consent should be given only for (user:  Bob-the-agronomist account,  client:  Agronomist4U).
        - Example 2: Only doctor X using the software of Hospital Y can access Patient Z data on DataTrustee.
      - Same, but using  Another Company's Software  (a software that is operated by another entity, can be operated by another entity for public usage, e.g., Basic-FMIS).
      - _KST platform_  user accessing the data of another  _KST platform_ user using a  Third-Party-Software.
        - Example:  Alice-the-Farmer is using Alice-the-Farmer account on  Basic FMIS  to access the field data of Bill-the-farmer on  _KST Platform_.
    - **User is a legal entity (a shared company account used by multiple employees)**
      - Company Employee using Company Account on their Company Software to access the data of a Data Subject on the  _KST platform_.
        - Example:  Bob-the-agronomist is using Agronomist4U account on  Agronomist4U  to access the field data of Bill-the-farmer on  _KST Platform_.
        - Consent should be given only for employees in  Agronomist4U  who have the Agronomist4U account credentials (user:  Agronomist4U account,  client: Agronomist4U).
      - Same, but using  a Third-Party-Software. (e.g., Basic-FMIS)

**Note**: all of these scenarios take both the user and the client into consideration. A possible alternative is to consider the user only, i.e., the consent is given to the user, and thus, the user can use any client (software).

**In our  _KST Platform_, we support only the following scenarios:**

- No User Interaction is required to get an access token (Client Credentials Flow).
  - Note: Software Component can use the access token to access the data, of all the Data Subjects that provided consents, at once.
- User Interaction is required to get an access token (Authorization Code Flow):
  - User is the Data Subject (whose data id to be accessed).  
    - Note: in our implementation, the access token can be used only to access the data of the Data Subject involved in the authorization code flow (the Data of a single Data Subject).
