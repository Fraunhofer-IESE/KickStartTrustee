# Architecture Drivers

## Business Goals

- _KST Platform_ should provide easy-to-use modules to enable software vendors to create a data trustee platform with low effort.
- _KST Platform_ should help software developers to adapt a data trustee platform to a specific domain or use case with low effort.

## Technical Constraints

- Use Open Source Software whenever possible.

## Business Constraints

- First version should be ready in November 2022.
- Final version should be ready by December 2023.
- Two Developer (one Senior, one security engineer).

## Key Functional Requirements

[NOTE] This is not a requirements document. Therefore the list is not complete; it contains the key functional requirements only. Requirements which have low or no impact on the architecture are not listed here.  

_KST Platform_ should:

- Provide an interface for data providers/ data consumers/ data prosumers to send consent requests.
- Provide means for the data owner to control/restrict the usage of their data (accept/ reject consent requests, and revoke consents).
- Provide an interface for data providers and data prosumers to ingest data.
- Provide an interface for data consumers and data prosumers to retrieve data.
- Provide means to store structured data.
- Provide an interface to add or store meta data for a data item.
- Provide means for data processing.
- Provide means logging of all actions of a data provider, a data consumer, a data prosumer, and a data owner.
- Provide means for the data owner to view the logged actions.
- Provide means for the data owner, the data provider, the data consumer, the data prosumer, and the data trustee operator to authenticate themself.  
- Provide a way to store legal and/or contract related information  (e.g., consents).

## Quality Requirements

- **Security**: _KST Platform_ should
  - Not prevent the secure storage of sensible data (e.g., not prevent server side encryption and distribution of data over multiple servers)
  - Not prevent the storage and usage of End-To-End encrypted data.
  - Ensure that data is only transferred over a secure channel.

- **Legal Compliance**:  _KST Platform_ should  
  - Provide the data owner with means to export their data.
  - Provide the data owner with means to delete/erase their personal data.

- **Scalability**: _KST Platform_ should  
  - Be able to handle at least 1000 data items per user.
  - Be able to handle at least 5000 events per user.
  - Be able to handle at least 500 users per instance.
  - Allow caching of static assets (images, CSS and JavaScript files) of the web frontend.
  - Allow the operation of the system behind a http load balancer.

- **Adaptability**: _KST Platform_ should
  - Support the configuration of Data Item Types at system startup.
  - Should support the development of domain specific extension services.

- **Usability**: _KST Platform_ should  
  - Provide its functionalities to the data owner via UIs accessible using a web browser (Chrome, Firefox, Edge).
  - Provide its functionalities to the data owner in a user-friendly manner (the functionalities should be understandable without knowledge about the technical details).

- **Maintainability**: _KST Platform_ should  
  - Have a unit statement test coverage of the business logic of at least 50%.
  - Should have a concise and easy to read documentation.

- **Auditable**: _KST Platform_ should
  - Allow a data owner to audit data related activity within the platform.
  - Allow platform admins to check all data and user activities happening on the platform using the logs.

- **Availability:** _KST Platform should_
  - Have a restart time which is not greater then ten seconds.
  - Have automatic health checks to detect faulty instances within 30s.
  - Offer a frontend and a backend that can be run and restarted independently.
