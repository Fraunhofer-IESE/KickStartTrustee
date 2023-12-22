type ConsentDataItemTypes = {
  consumedDataItemTypes: Array<string>;
  providedDataItemTypes: Array<string>;
};

const AppConsentDataItemTypes: Record<string, ConsentDataItemTypes> = {
  basic: {
    consumedDataItemTypes: ["field_data"],
    providedDataItemTypes: ["field_data"],
  },
  full: {
    consumedDataItemTypes: ["field_data", "disease_report", "disease_warning"],
    providedDataItemTypes: ["field_data", "disease_report"],
  },
};

export default AppConsentDataItemTypes;
