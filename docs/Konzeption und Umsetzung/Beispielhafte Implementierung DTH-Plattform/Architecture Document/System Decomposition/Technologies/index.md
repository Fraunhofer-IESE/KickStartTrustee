# Technologies

## Version Control

- GitLab
  - Rationale: It is the offered system by Fraunhofer (organizational constraint).

## For KST Platform OAuth 2 Authorization Server

- Keycloak
  - Rationale:
    - The development team is familiar with this technology.
    - It is free (no costs) and open source.

## For KST Platform Backend Development

- Java 17: A programming language.
  - Rationale: The development team is familiar with  _Java_ software development.
- Spring: A framework for developing modern  _Java_-based enterprise applications.
  - Rationale:
    - The development team is familiar with  _Spring_.
    - Spring offers a wide variety of projects that provide the needed support to develop the backend application, such as:
      - Spring Boot: A library that provides a complete runtime environment with low configuration effort to run spring applications.  
      - Spring WebFlux: A reactive web framework for spring.  
      - Spring Security, OAuth2 Resource Server: A library that protects endpoints in spring web applications. The library assumes that the web application is a resource server as defined in the OAuth2 protocol.  
      - Spring Boot Actuator: A library that provides health checks and monitoring capabilities for spring boot applications.  
      - Spring Boot Validation: A library that adds input validation capabilities (for endpoints exposed by spring web applications) to spring boot applications.  
      - Spring Boot Mail: A library for sending emails from spring boot applications.  
- Jetty (Webserver): An open source web server and servlet container written in java with low resource consumption and fast response times.  
  - Rationale:
    - The development team is familiar with jetty.
    - It can be embedded into a spring boot application.
    - It uses less resources than tomcat, which is the default embedded servlet container for spring boot application.
- SpringDoc OpenAPI: A _J__ava_ library automates the generation of  _API_ documentation for S_pring Boot_ projects.
  - Rationale: It works well with  _Spring_ Controllers (the way we implement the endpoints).
- SLF4J: (Simple Logging Facade for Java) A library that provides a  _Java_ logging  _API_.
  - Rationale: Standard, convenient.
- MongoDB: A document based  _NoSQL_ database (for Persistence).
  - Rationale: It is easy to use and the development team is familiar with it.
- Gradle: A Build tool.
  - Rationale: Fast building (short building time).
- JsonSchema Validator: To validate the structure of  _JSON_ objects (that they adhere to the defined  _JSON_ schema). Note: we use  _JSON_  schema to describe the valid data structure of the  _JSON_  objects.
- GeoTools: An open source java library for handling  _GeoJSON_ data.
- JUnit 5 & Mockito: A java unit testing framework for Unit Testing.
- GitLab CI/CD:  _GitLab_ built-in Continuous Integration, Continuous Deployment, and Continuous Delivery toolset (CI/CD).
  - Rationale: It is easy to use, provides a better developer experience, faster commit-to-release, offered by the used version control system (GitLab).
- Renovate Bot: A tool that regularly scans the Git repositories for outdated dependencies and automatically creates merge requests for the available version updates.
  - Rationale: It provides good support to keep the dependencies up-to-date.
- Docker: A containerization tool.
  - Rationale: It is the state of the Art technology to bundle and operate server side applications.

## For KST Platform Frontend Development

- Typescript: A programming language.
  - Rationale: The development team prefers  _Typescript_ over plain  _JavaScript_ because of the better  _IDE_ support (warnings, code completion).
- React: A Front-end JavaScript library for building user interfaces.
  - Rationale: It is the state of the Art Technology for  _SPAs_ (Single Page Applications), and the development team is familiar with React.
- Material UI: An open-source React component library that implements Google's Material Design.
  - Rationale: It offers a comprehensive nice looking and easy to use set of components, and has typescript support out of the box.
- MUI X Data Grid ([https://github.com/mui/mui-x](https://github.com/mui/mui-x)): A package that provides  _DataGrid_ component.
  - Rationale: It fits well to the  _material-ui_  library.
- React Intl: A library that supports internationalization for  _React_ applications.
  - Rationale:  It enables the support of multiple natural languages in  _SPAs_.
- React Router: A  _JavaScript_ framework that supports in handling client and server-side routing in  _React_ applications.
  - Rationale: It allows bookmarks and having stable urls for  _SPAs_.
- Keycloak JS  ([https://www.npmjs.com/package/keycloak-js](https://www.npmjs.com/package/keycloak-js)): A client-side  _JavaScript OpenID Connect_ library that can be used to secure web applications.
  - Rationale:  _Keycloak JS_ is provided by the  _keycloak_ open source project, and thus, it works perfectly together with  _keycloak._
- openapi-generator: A tool that automatically generates  _API_ client libraries (_SDK_ generation),  server stubs, documentation  and configuration given an OpenAPI Spec (v2, v3).
  - Rationale: It works good with the  _openapi_ specifications created by springdoc to generate the  _API_ client libraries.
- npm: A package manager.
- GitLab CI/CD  
  - Rationale: It is easy to use, provide a better developer experience, faster commit-to-release, offered by the used version control system (_GitLab_).
- Renovate Bot
  - Rationale: It provides good support to keep the dependencies up-to-date.
- Docker
  - Rationale: It is the state of the Art technology to bundle and operate server side applications.
- lighttpd: A web server software.
  - Rationale: It is a very lightweight and fast web-server for static content.
