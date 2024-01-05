# Agricultural Service Provider
This repository contains the "agricultural service provider" demo use case.

## About the use case

### Participants
- Farmer
- KST Data Trustee Platform ("kst-platform")
- Seeder Simon Service
- Willy Weeder Service

### Pre Conditions
- The farmer is registered at the kst-platform.
- There is data about the farmer's fields available at the kst-platform.
- The service provider client accounts (for "seeder-simon-service" and "willy-weeder-service") are configured at KST platform.

### Story
#### Part One
- The farmer orders Seeder Simon Service to seed the farmer's fields. (outside of the system)
- Seeder Simon Service requests the farmer's consent to consume and provide certain types of the farmer's data from/to the kst-platform.
- The farmer examines the consent request and accepts it.
- Seeder Simon Service CONSUMES the farmer's field data from the kst-platform.
- Seeder Simon Service seeds the farmer's fields. (outside of the system)
- Seeder Simon Service PROVIDES seeding work records related to the farmer's fields to the kst-platform.

Post Condition: There are seeding work records for the farmer's fields available at the kst-platform.

#### Part Two
- The farmer orders Willy Weeder Service to control the weeds at the farmer's fields. (outside of the system)
- Willy Weeder Service requests the farmer's consent to consume and provide certain types of the farmer's data from/to the kst-platform.
- The farmer examines the consent request and accepts it.
- Willy Weeder Service CONSUMES the farmer's field data and seeding work records from the kst-platform.
- Willy Weeder Service controls the weeds at the farmer's fields. (outside of the system)
- Willy Weeder Service PROVIDES weed control work records related to the farmer's fields to the kst-platform.

Post Condition: There are weed control work records for the farmer's fields available at the kst-platform.

## Realization
The use case is realized as a [Postman](https://www.postman.com/) collection.

Client Credentials are used to authenticate the systems of the involved agricultural service providers.
Information about how to configure the credentials for the two Data Prosumer Backend Services ("seeder-simon-service" and "willy-weeder-service") can be found here: https://github.com/Fraunhofer-IESE/KickStartTrustee/blob/main/src/kst-platform/docs/CLIENT_MANAGEMENT.md

The Postman collection can be found in the [postman directory](postman/) and can be imported to a local Postman installation.

The collection contains the requests that the systems of the agricultural service providers will send to the kst-platform to realize the use case.
