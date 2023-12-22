# KickStartTrustee Basic FMIS

## Purpose

KickStartTrustee Basic FMIS is a basic FMIS (Farm Management Information System) that serves as a demo to illustrate the envisioned interaction between an agricultural data trustee and a third-party application.

KST Basic FMIS is realized as a Single Page Application (SPA) for farmers and has no backend.
Farmers can use KST Basic FMIS to manage their field related data stored at the [KST Platform](https://gitlab.cc-asp.fraunhofer.de/kickstarttrustee/kickstarttrustee/-/tree/example-farming-trustee).
KST Basic FMIS can provide and consume field related data of the farmer that is currently logged in to KST Basic FMIS.

## General Information

KST Platform offers a web API for third-party applications.
The API covers the following concerns: data provisioning, data consumption and consent management.
The API is protected using Access Tokens according to OAuth 2.0 standard.

KST Basic FMIS is registered as a client to the KST Platform Authorization Server.
To obtain an Access Token, KST Basic FMIS performs the Authorization Code Flow with the farmer.
With that Access Token, KST Basic FMIS can use the KST Platform API.

Providing and Consuming data via KST Platform requires explicit consent of the farmer who is considered as the "owner" of its field related data.
Therefore, in case there is no consent yet, KST Basic FMIS will request the consent of the farmer using the consent request mechanism provided by KST Platform.
The farmer will be able to review the consent request and decide whether to accept or decline it.
Once the consent request is accepted, i.e., the consent has been established, KST Basic FMIS will be able to provide and consume farmer's data using KST Platform according to the farmer's consent.

KST Basic FMIS is developed for demonstration purposes only.
More sophisticated features and product qualities are currently out of scope.

The current main features are:

- OAuth 2.0 Authorization Code Flow to retrieve an Access Token
- Request farmer's consent using the consent request mechanism provided by KST Platform
- Provides the farmer with the ability to create a field and add its metadata (e.g., name, location, etc.) and pushes the field to be stored at the farmer’s account on KST Platform (i.e., **provide "field_data" data items**)
- Retrieve the farmer’s list of fields from their account on the KST Platform, and displays them in a list view as well as in a map view (i.e., **consume "field_data" data items**)
- Provides the farmer with the ability to create a disease report for their fields, and pushes the report to be stored at the farmer’s account on the KST Platform (i.e., **provide "disease_report" data items**)
- Retrieves the farmer’s list of disease reports from their account on the KST Platform, and displays the details in a list view (i.e., **consume "disease_report" data items**)
- Retrieves the farmer’s list of disease warnings from their account on the KST Platform, and displays the details in a list view (i.e., **consume "disease_warning" data items**)

## Technical Details

KST Basic FMIS is inspired by [COGNAC Basic FMIS](https://git.iese.fraunhofer.de/cognac/basic-fmis) and partly reuses elements of it (mainly concepts).
KST Basic FMIS is implemented using React and TypeScript.
The initial codebase has been created on the basis of the KST Platform Owner-UI.

To start the application locally:

- `npm install` (only needed on the first time or if there are changes to the dependencies)
- `npm start`

KST Basic FMIS will be opened automatically in the web browser.

The configuration of the SPA used while development can be adjusted using the [public/config.json](public/config.json) file.

For configuration of the KST Basic FMIS OAuth client see https://gitlab.cc-asp.fraunhofer.de/kickstarttrustee/kickstarttrustee/-/blob/main/docs/CLIENT_MANAGEMENT.md

For deployment and operation of KST Basic FMIS we use Docker. 
The KST Basic FMIS Docker Image requires certain environment variables to be configured:

- `KEYCLOAK_BASE_URL`: The base url of the Keycloak instance that will be used for user sign-in and to retrieve access tokens (e.g.: "https://auth.kickstarttrustee.de")
- `KEYCLOAK_REALM`: The Keycloak realm that will be used (e.g.: "master")
- `KEYCLOAK_CLIENT_ID`: The clientId for the KST Basic FMIS configured in Keycloak (e.g.: "basic-fmis")
- `KST_API_BASE_PATH`: API Base Path of the KST Platform instance that the KST-FMIS should connect to (e.g.: "https://demo.kickstarttrustee.de/api")
- `KST_OWNER_UI`: Url of the KST Owner UI, used to redirect the farmer to the owner UI to review/process consent requests (e.g.: "https://demo.kickstarttrustee.de/owner/")

These environment variables will be reflected to the [public/config.json](public/config.json) file at runtime once the container starts and configure the SPA at runtime.
Please keep in mind that this file will be publicly available and one must not place any sensitive information in it.
