# Scalability, Availability, Adaptability and Others

## Scalability

### KST Platform Backend

- We used  _reactive programing_  to be able to process many requests in parallel per backend instance.
- _we used mongodb_ as a fast and scalable NoSQL database.
- _KST Platform_  backend has no shared application state (everything is persisted and fetched from the database). This allows us to run multiple instances of the backend in parallel.
- _KST Platform_  backend is built as a structured monolith without a shared state between the modules. This means every module persists everything it requires for its APIs in its own database collections optimized for its own usage.
- Our docker images have fast startup time (avg. 5 seconds) and teardown time (avg. 2 seconds) for dynamic horizontal scaling. This is realized by using additional  _jvm_ command line parameters, to optimize the  _jvm_ memory consumption for our application.

### KST Platform Frontend and Basic FMIS  

- Both applications are implemented as Single Page Applications. Thus, they run on the clients web browser.
- There is no server side load for generating dynamic web pages.
- There is no session management on the server.
- We use a fast and lightweight webserver (_lighttpd_) for hosting the static resources of our single page applications.
- We use appropriate HTTP headers to ensure that the static resources of the single page applications are only reloaded from the server if necessary and not upon each time the user loads the application.

## Availability

- By using  _spring boot_  in combination with a suitable default  _Java Virtual Machine garbage collection_  configuration, we can ensure that the average restart time of the  _KST Platform_  Backend is not greater then ten seconds.
- We use  _spring actuator_  and the possibility of docker images to specify a healthcheck command to realize automatic health checks, which can detect faulty instances within 30s.
- By providing independent docker images for the  _KST Platform_  Backend and the  _KST Platform_  frontend we are able run and restart both containers independently.

## Adaptability

- We use the  _spring_  _framework_ configuration properties mechanism to read the data items config from the environment during the startup of an instance.
- We provide Java-Interfaces for extensions, to hook into certain steps of the core platform modules and use the  _spring framework_  dependency inject mechanism in the core platform modules to get a list of beans implementing the extension hook interface. This a way an extension can easily intercept in certain steps of certain actions (like for example when a data item is validated) without having to make code change in the core modules.

## Auditable

- We provide an event log view for the data owner. The event log provides an easy and user friendly way to see all activities related to the data items and consents of data owner.
- The  _KST Platform_  backend logs all actions concerning consent requests, consents, data items, and the data owner event logging. This way an admin can detect malicious activities and take countermeasures.

## Usability

- The  _KST Platform_ Frontend is realized as a  _react_ single page web application. This way a data owner can use a normal web browser to access to UI of the  _KST Platform._
- We use  _material-ui_ and simple elements, like tables and textboxes, to provide a user-friendly UI for the data owner, so that he/she can use the  _KST Platform_  without technical knowledge.

## Maintainability

- We use  _JUnit 5_ for unit testing and to ensure that we can have regression unit tests for our business code. This way we can detected failures introduced during refactoring early in the development lifecycle.
