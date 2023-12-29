# External Systems

## KickStartTrustee Basic FMIS

_KickStartTrustee Basic FMIS_ is a basic FMIS (Farm Management Information system) that serves as a demo to illustrate the envisioned interaction between an Agricultural Data Trustee and an agricultural third-party application. _KST Basic FMIS_ is realized as a Single Page Application (SPA) for farmers and has no backend. Farmers can use _KST Basic FMIS_ to manage their field-related data stored at the _KST Platform_. _KST Basic FMIS_ can provide and consume the field-related data of the farmer. Thus, _KST Basic FMIS_ plays the role of a Data Prosumer that interacts with _KST Platform_ (see [System Context and Domain](<../>)).

### Helpful Links

- The source code of  _KST Basic FMIS_  can be found in this GIT repository: [TODO](<>)  

### Features

_KST Basic FMIS_  has a simple dashboard that offers the following (basic) features/ capabilities to the farmer:

1. Provides the farmer with the ability to create a field and add its metadata (e.g., name, location, etc.,) and pushes (acts as a data provider)  the field to be stored at the farmer’s account on  _KST Platform_.
2. Provides the farmer with the ability to create a disease report for their fields, and pushes (acts as a data provider) the report to be stored at the farmer’s account on the  _KST Platform_.
3. Retrieves (acts as a data consumer) the farmer’s list of fields from their account on the  _KST Platform_, and displays them in a list view as well as in a map view (simple map and satellite).
4. Retrieves  (acts as a data consumer)  the farmer’s list of disease reports from their account on the  _KST Platform_, and displays the details in a list view.
5. Retrieves  (acts as a data consumer) the farmer’s list of disease warnings from their account on the  _KST Platform_, and displays the details in a list view.

### General Information

_KST Platform_ offers a web API for third-party applications. The API covers the following concerns: data provisioning, data consumption and consent management. The API is protected using Access Tokens according to OAuth 2.0 standard.

_KST Basic FMIS_  is registered as a client to the  _KST Platform_  Authorization Server. To obtain an Access Token,  _KST Basic FMIS_  performs the Authorization Code Flow with the farmer. With that Access Token,  _KST Basic FMIS_  can use the  _KST Platform_  API.

Providing and Consuming data via  _KST Platform_  requires explicit consent of the farmer who is considered as the "owner" of its field-related data. Therefore, in case there is no consent yet, _KST Basic FMIS_  will request the consent of the farmer using the consent request mechanism provided by  _KST Platform_. Via _KST Platform UI_, the farmer will be able to review the consent request and decide whether to accept or decline it. Once the consent request is accepted, i.e., the consent has been established, _KST Basic FMIS_  will be able to provide and consume farmer's data using _KST Platform_ according to the farmer's consent.

### Core Concepts and Technology

#### Core Concepts

- _KST Basic FMIS_  is a Data Prosumer that provides data to and consumes data from the  _KST Platform_.
  - Rationale: _KST Platform_ is envisioned as the central place to store/ manage the farmer's data and who is allowed to access it (i.e., reading/ consuming existing data as well as writing /providing new data). _KST Basic FMIS_  is one system of possibly many that will provide data to or consume data from the _KST Platform_ according to the consents of the farmer.
- _KST Basic FMIS_ is implemented as a Single Page Application (SPA)  
  - Rationale: With _KST Basic FMIS_ we want to explore and demonstrate how a third-party application can connect to the _KST Platform_. Building a SPA is one simple way to implement a simple prototype of such an application. Furthermore, the development team is familiar with SPAs.
- _KST Basic FMIS_ uses the OAuth2 Authorization Code Flow and Access Tokens
  - Rationale: _KST Platform_ uses OAuth2 and according access tokens to secure its API. To communicate with the _KST Platform_ API, _KST Basic FMIS_ needs to get and use such access tokens. We decided to use the Authorization Code Flow as the Authorization Code Flow is the recommended flow for SPA.

#### Technology

- Typescript: A programming language.
  - Rationale:  The development team prefers _Typescript_ over plain  _JavaScript_ because of the better  _IDE_ support (warnings, code completion).
- React: A Front-end JavaScript library for building user interfaces.
  - Rationale: It is the state of the Art Technology for  _SPAs_ (Single Page Applications),  and the development team is familiar with  _React_.
- Material UI: An open-source React component library that implements Google's Material Design.
  - Rationale: It offers a comprehensive nice looking easy to use set of components and has typescript support out of the box.
- MUI X ([https://github.com/mui/mui-x](https://github.com/mui/mui-x)): We used the  _Data Grid_  and  _Date and Time Pickers_.  
  - Rationale: It fits well to the  _material-ui_  library.
- React Intl: A library that supports internationalization for  _React_ applications.
  - Rationale: It enables the support of multiple natural languages in  _SPAs_.
- React Router: A  _JavaScript_ framework that supports in handling client and server-side routing in  _React_ applications.
  - Rationale: It allows bookmarks and having stable urls for our  _SPA_.
- Leaflet: A  _JavaScript_ library for interactive maps.
  - Rationale:  The development team used the technology in other projects and were familiar with it.
  - Used Plugins:
    - Leaflet Geoman Free: A  _Leaflet_ plugin for creating and editing geometry layers.
    - Leaflet Control Geocoder: A geocoder form to locate places ([https://github.com/perliedman/leaflet-control-geocoder](https://github.com/perliedman/leaflet-control-geocoder)).
    - React-Leaflet: A set of  _React_ components to integrate  _Leaflet_ maps in _React applications_.
- Keycloak JS ([https://www.npmjs.com/package/keycloak-js](https://www.npmjs.com/package/keycloak-js)): A client-side  _JavaScript OpenID Connect_ library that can be used to secure web applications.  
  - Rationale:  _Keycloak JS_ is provided by the  _keycloak_ open source project, and thus, it works perfectly together with  _keycloak._
- openapi-generator: A tool that automatically generates  _API_ client libraries (_SDK_ generation), server stubs, documentation and configuration given an OpenAPI Spec (v2, v3).  
  - Rationale: It works good with the  _openapi_ specifications created by springdoc to generate the  _API_ client libraries.
- npm: A package manager.
- GitLab CI/CD:  _GitLab_ built-in Continuous Integration, Continuous Deployment, and Continuous Delivery toolset (CI/CD).  
  - Rationale: It is easy to use, provide a better developer experience, faster commit-to-release, offered by the used version control system (_GitLab_).
- Renovate Bot: A tool that regularly scans the Git repositories for outdated dependencies and automatically creates merge requests for the available version updates.
  - Rationale: It provides good support to keep the dependencies up-to-date.
- Docker: A containarization tool.  
  - Rationale: It is the state of the Art technology to bundle and operate server side applications.
- lighttpd: A web server software.
  - Rationale: It is a very lightweight and fast web-server for static content.

### Deployment/ Operations

_KST Basic FMIS_ is containerized and can be operated using Docker. For details about the Deployment of our demo system as a whole (including _KST Basic FMIS_), please have a look at this page: [Deployment and Operation](<../../System Decomposition/Deployment/>).

The  _KST Basic FMIS_  Docker Image requires certain environment variables to be configured:

- **KEYCLOAK_BASE_URL**: The base url of the Keycloak instance that will be used for user sign-in and to retrieve access tokens (e.g., "https://auth.example.de").
- **KEYCLOAK_REALM**: The Keycloak realm that will be used (e.g., "master").
- **KEYCLOAK_CLIENT_ID**: The clientId for the  _KST Basic FMIS_  configured in Keycloak (e.g., "basic-fmis").
- **KST_API_BASE_PATH**: API Base Path of the  _KST Platform_  instance that the  _KST Basic FMIS_  should connect to (e.g., "https://demo.example.de/api")
- **KST_OWNER_UI**: Url of the  _KST Owner UI,_ used to redirect the farmer to the  _owner UI_ to review/process consent requests (e.g., "https://demo.example.de/owner/").

### Known Issues/ Limitations

_KST Basic FMIS_:

- Offers support for  **German** and  **English** languages. The user can change the language via a button in the __KST Basic FMIS_._ However, the validation messages displayed by the Browser (for example if leaving a required form field empty) will not consider the user's language choice in the application. Instead, the Browser Language Setting will determine the language used to display the validation message. As a result, it could be that a German hint is displayed by the Browser when using the app in English or the other way around. Thus, for a better experience, one should make sure to configure the Browser Language Setting in a suitable way to not be surprised by hints in a different language.
- Is implemented as a Single Page Application without a dedicated backend (_KST Basic FMIS_  connects directly to the  _KST Platform_  API). Therefore, it runs only in the browser of the user (i.e., the farmer) and only as long as the website is opened.

[NOTE]: _KST Basic FMIS_  is developed for demonstration purposes only. It is not intended to serve as a real FMIS. More sophisticated features and product qualities are currently out of scope. Furthermore, it does not support modifying or deleting existing data because  _KST Platform_  does not offer such capabilities for Data Providers, Data Consumers, or Data Prosumers.
