# Introduction

## Business Context

### Project Title

KickStartTrustee.

### Project motivation

In this project we define the concept of a “_Data Trustee_”, delineate its aspects, and develop a set of rules and requirements for a sound implementation of data trustee platforms.

### **Overall project goals**

The main goal of the project is to develop  _"KST DataTrustee Framework"_:  an open source framework (a set of guidelines) that supports its users in the creation of data trustee platforms for any business domain.

## System Overview

### System Name

_KickStartTrustee Platform_  (in short,  _KST Platform_).

### Motivation for the system

_KickStartTrustee Platform_  is an open source generic data trustee platform which can be configured and extended to support different domains.

### System goals

The main goal of the  _KickStartTrustee Platform_  is to demonstrate how the set of rules and guidelines, provided by our  _KST DataTrustee Framework_,  can be used for realizing an actual data trustee platform. The system acts as a blueprint for developing data trustee platforms. It is developed in a modular way, and thus, it can be configured, adjusted, and extended to create different customized data trustees to support different domains.

Our own developed set of modules that make up the system can be used by developers to accelerate the implementation of their own data trustee platforms. The system demonstrates how this set of modules can be bundled together to form a data trustee. Furthermore, it demonstrates how a generic data trustee can be configured and extended to support a specific domain (using the agricultural domain as an example).

### References to detailed requirements documents

The requirements document can be found here: [Generischer Anforderungskatalog](<../../../Generischer Anforderungskatalog/>)

### Key Terms and Entities

- Data owner: Data subject.
- Data Provider: A system that provides data to the _KST Platform_ account(s) of one or several data owners.
- Data Consumer: A system that consumes data from the _KST Platform_ account(s) of one or several data owners.
- Data Prosumer: A system that provides data to- and consumes data from- the _KST Platform_ account(s) of one or several data owners.

## Constraints

### Technical

Use Open Source Software whenever possible.

## Stakeholders

### Internal

- Project Manager.
- Requirements Engineer.
- System Architect.
- Platform (Lead) Developer/ Maintainer.
- Platform Tester.

### External

Direct or indirect system users:

- KST DataTrustee Framework  user (Indirect user, that uses the system as an example system):
  - A data trustee platform Project Manager.
  - A data trustee platform Requirement Engineer.
  - A data trustee platform System Architect.
  - A data trustee platform (Lead) Developer/ Maintainer.
  - A data trustee platform Tester.
- KickStartTrustee Platform user (Direct user, uses the system or interacts with it):
  - Data Subject (e.g., a farmer).
  - Data Provider, Consumer, and Prosumer (e.g., an agricultural Farm Management Information System (FMIS), an agricultural service provider).
  - Data User (e.g., an employee at an agricultural service provider company, a subcontractor).
  - Data Trustee Platform Manager (operator, admin).

## Document Goal

This is the architecture document of the open source  _KickStartTrustee Platform_. It provides details about the architectural decisions made during the development of the system.
