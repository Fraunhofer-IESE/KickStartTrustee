# Data Items

The system currently only supports structured data items to be uploaded. The data provider has to specify a data item type upon upload. It is the data trustee operators responsibility to specify which data item types are supported. Optional the operator can also specify a JSON-Schema to make shure that data items of a certain type follow a defined structure. This can be configured in the data items section in the application.yml file. This file located at `server/src/main/resources/`. 
