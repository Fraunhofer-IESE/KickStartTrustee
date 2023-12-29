# Core Concepts

## Data Items

A Data Trustee handles data that is provided by Data Providers and can be consumed by Data Consumers.

We modeled the data handled by the  _KST Platform_  as so-called "**data items**". A  **data item**  is a piece of data combined with a unique identifier (issued by  _KST Platform_) and metadata.

### Data Item Type

A Data Trustee might handle different types of data. To distinguish different types of data, in  _KST Platform,_ we modeled information about the type of the data to be part of the metadata of the data item.

The operator of a Data Trustee might decide whether to support arbitrary data or to limit the supported types of data.  _KST Platform_ currently supports only structured data items and a fixed (but extensible) set of types of data. The operator can also specify a JSON-Schema for a data type to make sure that data items of a certain type follow a defined structure.  _KST Platform_  publishes the list of supported data item types and their JSON-Schemas via its API (see  _catalog_  endpoints).

The Data Provider has to specify the data item type upon providing the data to the  _KST Platform_.  _KST Platform_  will check whether the type of data is supported and whether the provided data is valid according to the schema (in case there is a JSON-Schema configured for the type).

We made this design decision for multiple reasons:

- Facilitate the handling of the data.
- Enable consent management and access control on the data item type level (Consent requests, consents, permissions are on the level of the type of the data).  

- Enable consumption of data items by their type (Data Consumers can consume data of a certain type, e.g., consume the data with type "seeding_work_records").  

- Help the Operator of the Data Trustee to limit the type of data that can be handled by their platform (e.g., a data trustee in the field of agriculture might not want to have medical data about the employees handled by the system as that would have legal implications).
- Reduce uncertainty and ambiguity about the data by defining the data types that are supported by the Data Trustee. Define explicitly what (types of) data can be provided to/ consumed from the Data Trustee.
- Increase interoperability by prescribing the structure and semantics of the data.
- Ensure that the provided data conforms to a certain structure.
- Provide reliability for the Data Consumers with regard to the structure and semantics of the consumed data.

For technical details about how to configure the supported data item types in our _KST Platform_, please have a look at  [TODO](<>)

## Consent Management

Please have a look at the [Consent Management](<../../Quality Concepts/Security/Consent Management/>) for details.
