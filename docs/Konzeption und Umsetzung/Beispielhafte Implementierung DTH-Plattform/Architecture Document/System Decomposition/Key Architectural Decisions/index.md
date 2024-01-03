# Key Architectural Decisions

In the following we list the key architectural decisions, we made to realize our solution, the KST Platform:

## KST Platform Backend

- We used Reactive Programming.
  - Rationale: We want to have a fast response time and a low resource consumption for our backend, to be able to run the demo locally on our laptops.
- We built the backend as a Structured Monolith.
  - Rationale: Keep things simple as long as there is no need for a more complex structure. Having multiple repositories, microservices, distributed computing, etc., would have added complexity that was not necessary for our small demo implementation. Nevertheless, we organized the code in a structured way that enables easier separation in the future in case this was required.
- The backend offers/Exposes  RESTful-Web-APIs that can be consumed by external systems.
  - Rationale: Web-API is the state-of-the-art approach to offer services. Also, the development team is familiar with RESTful-Web-APIs.
- The backend internal modules communicate asynchronously using  _Events_.
  - Rationale: Achieve a high decoupling between the internal components to facilitate reuse of individual components.

## KST Platform Frontend (Owner UI)

- For the web application facing the data owner we used: Single Page Application (SPA).
  - Rationales: To achieve a better scalability, the development team is familiar with SPA.
- For authentication and authorization we use: OAuth2 Authorization Code Flow with Access Tokens and Refresh Tokens.
  - Rationales: This is the state-of-the-art way to ensure the security of SPA when using OAuth2, the development team is familiar with OAuth2.
