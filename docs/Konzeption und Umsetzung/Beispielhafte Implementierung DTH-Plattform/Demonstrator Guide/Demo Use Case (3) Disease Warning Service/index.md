# Demo Use Case (3) Disease Warning Service

## Scenario Flow

The following diagrams illustrates the use case flow:

![Scenario Flow](<../SVG/Use Case 3 - Onboard Farmer on KST Platform.svg>)

![Scenario Flow](<../SVG/Use Case 3 - Onboard Farmer on KST Basic FMIS.svg>)

![Scenario Flow](<../SVG/Use Case 3 - Disease Warning Service Flow.svg>)

Following is a details description about the use case scenario flow:

1. Onboarding the farmer on  _KST Platform_:  

    - The farmer signs in to _KST Platform_.
    - The farmer configures the desired settings for the “Disease Warning Service”:
        - Subscribe as contributor: for sharing disease report.
        - Subscribe as a consumer: for receiving disease warnings (store it in thier account on  _KST Platform_). in this case, the farmer can additionally subscribe for receiving email notification with the warnings.

    The farmer can subscribe for none, one, or all options.

2. The farmer uses _Basic_ _KST FMIS_ to create a disease report (to do so, the farmer should first provide  _Basic KST FMIS_  with a  Full Access consent). The disease report consists of the following:
    1. Date.
    2. Disease Name.
    3. Disease Severity: {low, medium, high}.
    4. Infected Field: the name of the infected field.
    5. Confidential: a Boolean value. {True: the report is confidential and should not be used by the Disease Warning Services at the KST-Platform to generate warnings; False: the report can be used by the Disease Warning Services at the KST-Platform to generate warnings}.
3. _Basic KST FMIS_  sends a request to  _KST Platform_  to push the disease report to the farmer’s account.
4. _KST Platform_  checks the validity of the payload (e.g., is the report generated for a field that is owned by the reporter farmer?).
5. If check in step 4 passes,  _KST Platform_  stores the report at the farmer’s account.
6. _KST Platform_  checks the farmer’s settings for the “Disease Warning Service” (Did the farmer subscribe to sharing disease reports?)
7. If 6 passes,  _KST Platform_  checks the  _Confidentiality_ of the report (Confidential == false).
8. If 7 passes,“Disease Warning Service” module calculates the range of the endangered fields (currently, any field that is in the distance range of 10 KM away from the infected field).
9. _“Disease Warning Service”_  module generates a warning for endangered fields under the condition that the owner farmer is subscribed to receiving disease warnings.
10. _“Disease Warning Service”_  module sends an email that contains the warning for each of the farmers (in 9) that are subscribed to receiving warnings emails.
11. The warning consists of the following:

- Date.
- Disease Name.
- Disease Severity: {low, medium, high}.
- Endangered Field: a list of endangered fields {field1, field 2,…}.
