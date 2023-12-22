# System Structure

The KST Platform consists of two components we have developed ourselves:

- **KST Platform Backend**: The backend contains the implementation of the REST endpoints, the main business logic and the data persistence logic. It is structured according to domains: consent, data items, audit, data owner profile. For each domain there exists a module and each module has a three layer architecture (api, business and persistence).
- **KST Platform Frontend**: The frontend provides the web user interface for the data owner. It has a folder for each type of element in react: view, components, hooks, context, api, etc. The views folder has a nested structure in accordance with the page routes.

In addition we are using keycloak as our OAuth2 Authorization Server. Keycloak is an off the shelf component, which we configured according to our needs.

The following figure provides a high level overview of the system architecture:

![High Level System Structure](SVG/High%20Level%20System%20Structure.svg)

## KST Platform Backend Modules

### Core Modules

The following diagram illustrates the core modules of our _KST Platform_ structured monolith (our backend) and the dependencies. The _disease warning service_ (see below for details) is left out in this section on purpose, because it serves as an example for a domain specific extension and is therefore not part of the data trustee core functionalities.

![KST Platform Backend Core Modules](SVG/KST%20Platform%20Backend%20Core%20Modules.svg)

The modules don't share common data in the database and don't call each other directly through method calls. But the modules are loosely coupled in a publish subscribe pattern, through the events published on the event bus. It is recommended to keep this loose coupling for easy extensibility and maintainability.

The core modules of our _KST Platform_ Backend are:

#### Common

The  common  module contains often used interfaces, exceptions, and DTO classes (Data Transfer Objects) for other modules.

#### Event

This module contains all our events as java records as well as our reactive in memory event bus implementation.

#### Data catalog module

The data catalog module contains the handling of the configured data item types and their JSON Schemas (if configured for a data item type).

#### Data storage module

The data storage module is responsible for managing and persisting the data items and the corresponding metadata.

#### Data Control module

The data control module has two submodules:

- Audit: This module is responsible for logging and persisting owner data-related events (like for example data item consumption events).
- Consent: This module is responsible for managing and persisting the consents and the consent requests.

#### Notification

The notification module allows other modules to send notifications via email to data owners and it stores their notification settings.

#### Owner

The owner module stores the user profiles (currently containing the users' language preference).

#### Server

This module contains configuration files and classes for authentication and authorization  and for the database.  It also contains the main class and therefore depends on all other modules to bundle the application as an executable jar.

### Extensions Modules

#### Disease warning module

The disease warning module is an example for a domain specific extension of the core KST Platform backend. For our demonstrator, we chose the agricultural domain.

The disease warning module offers users (farmers) a service (the _Disease Warning Service_) that enables farmers to report an infection/ disease on one of their fields and anonymously broadcasts a warning to other farmers who are subscribed to the service and have fields in the area that might be in danger.

The _disease warning module_ depends on the following modules:

- The common module.
- The event module.
- The data storage module.

The reason for the dependency on the _data storage module_ is that the _disease warning service_ needs to create new data items of type _disease warning_.

### Adding a Module

To extend the KST Platform backend, you shoud add a new module as follows:

1. Create a new directory for the module in the root directory of the repository and give it the same name as the module. The naming convention is to use lower case letters only and hyphens.
2. Place a _build.gradle_ file in the newly created directory. The  _build.gradle_ file contains the required dependencies for your module.  You should add the _common module_. And, if you want to use the eventbus you should also add the _event module_ as a dependency. Furthermore, you should take care yourself of adding any other spring dependencies you may need. See the _build.gradle_ file in the _disease-warning module_ for an example.
3. Inside the module directory create the following folders:
    1. src/main/java
    2. src/test/java
4. Edit the _settings.gradle_ file in the root directory of the repository and add an include statement for your project (add a line with the following content: include '<_your-module-name_>').
5. Edit the _build.gradle_ file in the server module to include your dependency in the final jar (add implementation project (':<_your-module-name_>') inside the dependency section).

For the package structure, please have a look at the next section.

### Module Structure

This page describes the structure of a single module.  Each module corresponds to a gradle subproject.

The source code of a module  (except for the modules: common, event,  and server) is always organized in the same way for better maintainability and understanding. The following figure illustrates the modules structure:

![KST Platform Backend Module Structure](SVG/KST%20Platform%20Backend%20Module%20Structure.svg)