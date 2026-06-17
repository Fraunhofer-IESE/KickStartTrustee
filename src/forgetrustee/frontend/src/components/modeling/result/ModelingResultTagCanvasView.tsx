/*
 Copyright 2026 Fraunhofer IESE

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import { Box, Button, Grid, Heading, Image, Text } from "grommet";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import CustomTag from "../common/CustomTag";
import ColumnBackgroundSvg from "./ColumnBackgroundSvg";

import ModelingResultTagCanvasSection from "./ModelingResultTagCanvasSection";
import placeholder from "../../../assets/placeholder.svg";
import ModelingResultExportModal from "./ModelingResultExportModal";
import ModelingResultExecutiveSummaryModal from "./ModelingResultExecutiveSummaryModal";
import { useNavigate } from "react-router-dom";
import { routes } from "../../../config/routes";
import type { DataTrusteeWizardBuildResponseDto } from "../../../types/generated";
import type { MetadataFieldDto, MetadataSectionDto } from "../../../types/generated";
import { useListModules } from "../../../api/generated/metadata-controller/metadata-controller";
import { createModelExportZip, downloadBlob } from "../common/modelExportZip";
import ModelingResultShaclReportModal from "./ModelingResultShaclReportModal";
import ModelingResultSubmissionModal from "./ModelingResultSubmissionModal";
import {
  SESSION_COMPARE_ITEM_ID,
  setCompareSessionSelectedIds,
} from "../../compare/compareSession";
import HtmlContent from "../../shared/HtmlContent";

type ModelingResultTagCanvasViewProps = {
  result: DataTrusteeWizardBuildResponseDto;
  onBack: () => void;
  allowExportAndSubmission?: boolean;
  columnWidthsPercent?: [number, number, number, number];
  columnGapPx?: number;
  columnBorderRadiusPx?: number;
  avatarDataUrl?: string | null;
  avatarFileName?: string | null;
};

type TagCanvasTagGroup = {
  title: string;
  tags: string[];
};

type TagCanvasSubSection = {
  title: string;
  groups: TagCanvasTagGroup[];
};

type TagCategory = "core" | "data" | "legal" | "objectives" | "business";

type SelectedChipInfo = {
  label: string;
  category: TagCategory;
};

const normalizeTagLabel = (value: unknown): string =>
  String(value).replaceAll("_", " ").replace(/\s+/g, " ").trim();

const normalizeCommentText = (value: unknown): string | null => {
  if (typeof value !== "string") {
    return null;
  }

  const normalizedValue = value.trim();
  if (!normalizedValue || normalizedValue.toLowerCase() === "missing ontology comment") {
    return null;
  }

  return normalizedValue;
};

const extractCommentText = (value: unknown): string | null => {
  if (value === null || value === undefined) {
    return null;
  }

  if (typeof value === "object" && !Array.isArray(value)) {
    return normalizeCommentText((value as { comment?: unknown }).comment);
  }

  return null;
};

const extractScalarTagValue = (value: unknown): string | null => {
  if (value === null || value === undefined || value === "") {
    return null;
  }

  if (typeof value === "object" && !Array.isArray(value)) {
    const candidate =
      (value as { value?: unknown; name?: unknown; title?: unknown; label?: unknown }).value ??
      (value as { value?: unknown; name?: unknown; title?: unknown; label?: unknown }).name ??
      (value as { value?: unknown; name?: unknown; title?: unknown; label?: unknown }).title ??
      (value as { value?: unknown; name?: unknown; title?: unknown; label?: unknown }).label;

    return candidate === null || candidate === undefined || candidate === ""
      ? null
      : String(candidate);
  }

  return String(value);
};

const formatProcessingBaseLabel = (value: unknown): string | null => {
  const scalarValue = extractScalarTagValue(value);
  if (!scalarValue) {
    return null;
  }

  const normalizedValue = scalarValue.trim();
  if (!normalizedValue) {
    return null;
  }

  const labelByValue: Record<string, string> = {
    CONSENT: "Einwilligung",
    CONTRACT_FULFILLMENT: "Vertragserfüllung",
    LEGITIMATE_INTEREST: "Berechtigtes Interesse",
    LEGAL_OBLIGATION: "Gesetzliche Verpflichtung",
    VITAL_INTERESTS: "Lebenswichtige Interessen",
    PUBLIC_TASK: "Wahrnehmung einer Aufgabe im öffentlichen Interesse",
    PUBLIC_INTEREST: "Öffentliches Interesse",
  };

  return labelByValue[normalizedValue] ?? normalizeTagLabel(normalizedValue);
};

const formatProcessingBasisLabel = (basis: unknown): string | null => {
  if (!basis || typeof basis !== "object" || Array.isArray(basis)) {
    return formatProcessingBaseLabel(basis);
  }

  const entry = basis as {
    label?: unknown;
    value?: unknown;
    comment?: unknown;
    details?: unknown;
    title?: unknown;
    description?: unknown;
  };

  return (
    normalizeCommentText(entry.label) ??
    formatProcessingBaseLabel(entry.value) ??
    formatProcessingBaseLabel(entry.title) ??
    formatProcessingBaseLabel(entry.description)
  );
};

const toTagContent = (value: unknown): string | null => {
  if (value === null || value === undefined || value === "") {
    return null;
  }

  if (Array.isArray(value)) {
    const items = value
      .map((item) => extractScalarTagValue(item))
      .filter((item): item is string => Boolean(item))
      .map((item) => normalizeTagLabel(item));
    return items.length > 0 ? items.join(", ") : null;
  }

  const scalarValue = extractScalarTagValue(value);
  return scalarValue ? normalizeTagLabel(scalarValue) : null;
};

const toTagList = (value: unknown): string[] => {
  if (value === null || value === undefined || value === "") {
    return [];
  }

  if (Array.isArray(value)) {
    return value
      .map((item) => extractScalarTagValue(item))
      .filter((item): item is string => Boolean(item))
      .map((item) => normalizeTagLabel(item));
  }

  const normalized = extractScalarTagValue(value);
  return normalized ? [normalized] : [];
};

const splitProcessingBases = (value: unknown): { bases: string[]; details: string[] } => {
  if (!value) {
    return { bases: [], details: [] };
  }

  if (Array.isArray(value)) {
    const bases = value
      .map((item) => formatProcessingBasisLabel(item))
      .filter((item): item is string => Boolean(item));

    const details = value
      .flatMap((item) => {
        if (!item || typeof item !== "object" || Array.isArray(item)) {
          return [];
        }
        const entry = item as { details?: unknown; comment?: unknown; description?: unknown };
        if (Array.isArray(entry.details)) {
          return entry.details.flatMap((detail) => {
            if (!detail || typeof detail !== "object" || Array.isArray(detail)) {
              return [];
            }
            const detailEntry = detail as { title?: unknown; description?: unknown };
            return [toTagContent(detailEntry.title), toTagContent(detailEntry.description)];
          });
        }

        return [normalizeCommentText(entry.comment), toTagContent(entry.description)];
      })
      .filter((item): item is string => Boolean(item));

    return { bases, details };
  }

  return { bases: toTagList(value), details: [] };
};

const toDetailTagList = (
  value:
    | {
        title?: string;
        description?: string;
      }[]
    | undefined,
): string[] => {
  if (!value) {
    return [];
  }

  return value
    .flatMap((item) => [item.title, item.description])
    .map((item) => toTagContent(item))
    .filter((item): item is string => Boolean(item));
};

const createTagGroup = (
  title: string,
  tags: Array<string | null | undefined>,
): TagCanvasTagGroup | null => {
  const normalizedTags = tags.filter((tag): tag is string => Boolean(tag));

  if (normalizedTags.length === 0) {
    return null;
  }

  return { title, tags: normalizedTags };
};

const createSubSection = (
  title: string,
  ...groups: Array<TagCanvasTagGroup | null | undefined>
): TagCanvasSubSection | null => {
  const normalizedGroups = groups.filter((group): group is TagCanvasTagGroup =>
    Boolean(group && group.tags.length > 0),
  );

  if (normalizedGroups.length === 0) {
    return null;
  }

  return { title, groups: normalizedGroups };
};

const ModelingResultTagCanvasView = ({
  result,
  onBack,
  allowExportAndSubmission = true,
  columnWidthsPercent = [15, 25, 35, 25],
  columnGapPx = 8,
  columnBorderRadiusPx = 4,
  avatarDataUrl = null,
  avatarFileName = null,
}: ModelingResultTagCanvasViewProps) => {
  const backgroundBoxRef = useRef<HTMLDivElement | null>(null);
  const navigate = useNavigate();
  const { data: moduleMetadataList } = useListModules();

  const [backgroundSize, setBackgroundSize] = useState({ width: 0, height: 0 });
  const [showExportModal, setShowExportModal] = useState(false);
  const [showShaclReportModal, setShowShaclReportModal] = useState(false);
  const [showSubmissionModal, setShowSubmissionModal] = useState(false);
  const [showExecutiveSummaryModal, setShowExecutiveSummaryModal] = useState(false);
  const [isExporting, setIsExporting] = useState(false);
  const [selectedChip, setSelectedChip] = useState<SelectedChipInfo | null>(null);
  const core = result.dataTrusteeModel?.core;
  const dataSection = result.dataTrusteeModel?.data;
  const legalSection = result.dataTrusteeModel?.legal;
  const objectives = result.dataTrusteeModel?.objectives;
  const business = result.dataTrusteeModel?.business;

  const fieldMetadataByName = useMemo(() => {
    const byField = new Map<
      string,
      {
        options: Map<string, string>;
        infoText: string | null;
      }
    >();

    const pickInfoText = (...values: Array<string | null | undefined>) => {
      for (const value of values) {
        const normalizedValue = value?.trim();
        if (normalizedValue) {
          return normalizedValue;
        }
      }

      return null;
    };

    const registerOption = (fieldName: string, value: unknown, label: unknown) => {
      const normalizedField = fieldName.trim();
      const normalizedValue = String(value ?? "").trim();
      const normalizedLabel = String(label ?? "").trim();

      if (!normalizedField || !normalizedValue || !normalizedLabel) {
        return;
      }

      const existingField = byField.get(normalizedField) ?? {
        options: new Map<string, string>(),
        infoText: null,
      };
      existingField.options.set(normalizedValue, normalizedLabel);
      byField.set(normalizedField, existingField);
    };

    const registerField = (
      fieldId: string,
      field: MetadataFieldDto,
      inheritedInfoText: string | null,
    ) => {
      const fieldKeys = [fieldId, field.name].filter((key): key is string => Boolean(key?.trim()));
      const infoText = pickInfoText(field.helpText, field.tooltip, inheritedInfoText);

      for (const key of fieldKeys) {
        const normalizedKey = key.trim();
        const existingField = byField.get(normalizedKey) ?? {
          options: new Map<string, string>(),
          infoText: null,
        };

        byField.set(normalizedKey, {
          options: existingField.options,
          infoText: existingField.infoText ?? infoText,
        });
      }

      if (!field.options || field.options.length === 0) {
        return;
      }

      for (const option of field.options) {
        for (const key of fieldKeys) {
          registerOption(key, option.value, option.label);
        }
      }
    };

    const walkSection = (
      section: MetadataSectionDto | undefined,
      inheritedInfoText: string | null,
    ) => {
      if (!section) {
        return;
      }

      const sectionInfoText = pickInfoText(section.helpText, section.tooltip, inheritedInfoText);

      for (const [fieldId, field] of Object.entries(section.fields ?? {})) {
        registerField(fieldId, field, sectionInfoText);
      }

      for (const subsection of Object.values(section.subsections ?? {})) {
        walkSection(subsection, sectionInfoText);
      }
    };

    for (const moduleMetadata of moduleMetadataList ?? []) {
      const moduleInfoText = pickInfoText(moduleMetadata.helpText, moduleMetadata.tooltip);

      for (const [fieldId, field] of Object.entries(moduleMetadata.fields ?? {})) {
        registerField(fieldId, field, moduleInfoText);
      }

      for (const section of Object.values(moduleMetadata.sections ?? {})) {
        walkSection(section, moduleInfoText);
      }
    }

    return byField;
  }, [moduleMetadataList]);

  const extractScalarTagValue = (value: unknown): string | null => {
    if (value === null || value === undefined || value === "") {
      return null;
    }

    if (typeof value === "object" && !Array.isArray(value)) {
      const candidate =
        (value as { value?: unknown; name?: unknown; title?: unknown; label?: unknown }).value ??
        (value as { value?: unknown; name?: unknown; title?: unknown; label?: unknown }).name ??
        (value as { value?: unknown; name?: unknown; title?: unknown; label?: unknown }).title ??
        (value as { value?: unknown; name?: unknown; title?: unknown; label?: unknown }).label;

      return candidate === null || candidate === undefined || candidate === ""
        ? null
        : String(candidate);
    }

    return String(value);
  };

  const resolveMetadataLabel = useCallback(
    (fieldName: string | undefined, value: unknown): string | null => {
      const scalarValue = extractScalarTagValue(value);
      if (!scalarValue) {
        return null;
      }

      const normalizedValue = scalarValue.trim();
      if (!normalizedValue) {
        return null;
      }

      const mappedLabel = fieldName
        ? fieldMetadataByName.get(fieldName)?.options.get(normalizedValue)
        : undefined;

      return mappedLabel ? mappedLabel.trim() : normalizeTagLabel(normalizedValue);
    },
    [fieldMetadataByName],
  );

  const toMetadataTagContent = (value: unknown, fieldName?: string): string | null => {
    if (value === null || value === undefined || value === "") {
      return null;
    }

    if (Array.isArray(value)) {
      const items = value
        .map((item) => resolveMetadataLabel(fieldName, item))
        .filter((item): item is string => Boolean(item));
      return items.length > 0 ? items.join(", ") : null;
    }

    return resolveMetadataLabel(fieldName, value);
  };

  const toMetadataTagList = (value: unknown, fieldName?: string): string[] => {
    if (value === null || value === undefined || value === "") {
      return [];
    }

    if (Array.isArray(value)) {
      return value
        .map((item) => resolveMetadataLabel(fieldName, item))
        .filter((item): item is string => Boolean(item));
    }

    const resolved = resolveMetadataLabel(fieldName, value);
    return resolved ? [resolved] : [];
  };

  const rightsHolderName = toTagContent(core?.rightsHolderName);
  const dataOwnerName =
    extractScalarTagValue(core?.rightsHolderIsRepresented)?.trim().toLowerCase() === "true"
      ? toTagContent(core?.dataOwnerName)
      : rightsHolderName;
  const dataTrusteeName = toTagContent(core?.dataTrusteeName);
  const dataConsumerName = toTagContent(core?.dataConsumerName);
  const rightsHolderIsRepresented = extractScalarTagValue(core?.rightsHolderIsRepresented)
    ?.trim()
    .toLowerCase();
  const containPersonalInformation = extractScalarTagValue(core?.containPersonalInformation)
    ?.trim()
    .toLowerCase();

  const rightsHolderTags = [
    toTagContent("Rechteinhaber"),
    toMetadataTagContent(core?.rightsHolderAffiliation, "rightsHolderAffiliation"),
    rightsHolderIsRepresented === "true"
      ? "Wird vertreten"
      : rightsHolderIsRepresented === "false"
        ? "Wird nicht vertreten"
        : null,
  ].filter((tag): tag is string => Boolean(tag));

  const dataOwnerTags = [
    toTagContent("Dateninhaber"),
    toMetadataTagContent(core?.dataCategoryName, "dataCategoryName"),
    containPersonalInformation === "true"
      ? "Personenbezug"
      : containPersonalInformation === "false"
        ? "Kein Personenbezug"
        : null,
  ].filter((tag): tag is string => Boolean(tag));

  const dataTrusteeTags = [
    toTagContent("Datentreuhänder"),
    toMetadataTagContent(dataSection?.architectureType, "architectureType"),
    toMetadataTagContent(business?.businessModel, "businessModel"),
  ].filter((tag): tag is string => Boolean(tag));

  const dataConsumerTags = [
    toTagContent("Datennutzer"),
    toMetadataTagContent(core?.dataConsumerAffiliation, "dataConsumerAffiliation"),
    ...toMetadataTagList(objectives?.motivationDataConsumer, "motivationDataConsumer"),
  ].filter((tag): tag is string => Boolean(tag));

  const dataTrusteeGoalTags = toMetadataTagList(objectives?.dataTrusteeGoals, "dataTrusteeGoals");
  const businessDomainTags = toMetadataTagList(business?.businessDomains, "businessDomains");
  const fundingSourceTags = toMetadataTagList(business?.fundingSources, "fundingSources");
  const businessFormTag = toMetadataTagContent(business?.businessModel, "businessModel");
  const dataProcessingBases = splitProcessingBases(legalSection?.processingBases);

  const sourceSystemSubSections = [
    createSubSection("System", createTagGroup("Quelle", [toTagContent(dataSection?.sourceSystem)])),
    createSubSection(
      "Empfang",
      createTagGroup(
        "Empfangstechniken",
        toMetadataTagList(dataSection?.receptionTechnologies, "receptionTechnologies"),
      ),
      createTagGroup("Empfangsfrequenz", [
        toMetadataTagContent(dataSection?.receptionFrequency, "receptionFrequency"),
      ]),
    ),
    createSubSection(
      "Schutz",
      createTagGroup(
        "Sicherheitsmaßnahmen",
        toMetadataTagList(dataSection?.receptionSecurityMeasures, "receptionSecurityMeasures"),
      ),
      createTagGroup(
        "Benutzerdefinierte Maßnahmen",
        toDetailTagList(dataSection?.customReceptionSecurityTechniques),
      ),
    ),
  ].filter((section): section is TagCanvasSubSection => Boolean(section));

  const implementationSubSections = [
    createSubSection(
      "Datenverarbeitung",
      createTagGroup(
        "Datenaufbereitung",
        toMetadataTagList(dataSection?.preparationTechniques, "preparationTechniques"),
      ),
      createTagGroup(
        "Benutzerdefinierte Aufbereitung",
        toDetailTagList(dataSection?.customPreparationTechniques),
      ),
      createTagGroup("Datenspeicherung", [
        toMetadataTagContent(dataSection?.storageTechnique, "storageTechnique"),
      ]),
      createTagGroup("Speicherdauer", [
        toMetadataTagContent(dataSection?.storageRetention, "storageRetention"),
      ]),
      createTagGroup(
        "Datenauswertung",
        toMetadataTagList(dataSection?.analysisTechniques, "analysisTechniques"),
      ),
      createTagGroup(
        "Benutzerdefinierte Auswertung",
        toDetailTagList(dataSection?.customAnalysisTechniques),
      ),
    ),
  ].filter((section): section is TagCanvasSubSection => Boolean(section));

  const dataSubSections = [
    createSubSection(
      "Grundlagen",
      createTagGroup("Verarbeitungsgrundlagen", dataProcessingBases.bases),
    ),
    createSubSection("Details", createTagGroup("Rechtliche Details", dataProcessingBases.details)),
    createSubSection(
      "Einwilligung",
      createTagGroup("Einwilligungsart", [
        toMetadataTagContent(legalSection?.consentType, "consentType"),
      ]),
      createTagGroup("Erfasst durch", [
        toMetadataTagContent(legalSection?.consentEnteredBy, "consentEnteredBy"),
      ]),
      createTagGroup("Einholung durch", [toTagContent(legalSection?.obtainingConsentBy)]),
    ),
  ].filter((section): section is TagCanvasSubSection => Boolean(section));

  const targetSystemSubSections = [
    createSubSection("System", createTagGroup("Ziel", [toTagContent(dataSection?.targetSystem)])),
    createSubSection(
      "Weitergabe",
      createTagGroup(
        "Weitergabetechniken",
        toMetadataTagList(dataSection?.forwardingTechniques, "forwardingTechniques"),
      ),
      createTagGroup("Weitergabefrequenz", [
        toMetadataTagContent(dataSection?.forwardingFrequency, "forwardingFrequency"),
      ]),
    ),
    createSubSection(
      "Schutz",
      createTagGroup(
        "Sicherheitsmaßnahmen",
        toMetadataTagList(dataSection?.forwardingSecurityMeasures, "forwardingSecurityMeasures"),
      ),
      createTagGroup(
        "Benutzerdefinierte Maßnahmen",
        toDetailTagList(dataSection?.customForwardingSecurityTechniques),
      ),
    ),
  ].filter((section): section is TagCanvasSubSection => Boolean(section));

  const selectedChipInfoTextByTagId = useMemo(() => {
    const infoByTagId = new Map<string, string>();

    const registerInfoText = (
      category: TagCategory,
      label: string | null | undefined,
      comment: unknown,
    ) => {
      const normalizedLabel = label?.trim();
      const normalizedComment = normalizeCommentText(comment);

      if (!normalizedLabel || !normalizedComment) {
        return;
      }

      const key = `${category}:${normalizedLabel}`;
      if (!infoByTagId.has(key)) {
        infoByTagId.set(key, normalizedComment);
      }
    };

    const registerCommentedValue = (category: TagCategory, fieldName: string, value: unknown) => {
      const label = resolveMetadataLabel(fieldName, value);
      registerInfoText(category, label, extractCommentText(value));
    };

    const registerCommentedList = (
      category: TagCategory,
      fieldName: string,
      values: unknown[] | undefined,
    ) => {
      for (const value of values ?? []) {
        const label = resolveMetadataLabel(fieldName, value);
        if (!label) {
          continue;
        }

        registerInfoText(category, label, extractCommentText(value));
      }
    };

    registerCommentedValue("core", "rightsHolderAffiliation", core?.rightsHolderAffiliation);
    registerCommentedValue("core", "dataCategoryName", core?.dataCategoryName);
    registerInfoText(
      "core",
      rightsHolderIsRepresented === "true"
        ? "Wird vertreten"
        : rightsHolderIsRepresented === "false"
          ? "Wird nicht vertreten"
          : null,
      extractCommentText(core?.rightsHolderIsRepresented),
    );
    registerCommentedValue("data", "architectureType", dataSection?.architectureType);
    registerCommentedValue("business", "businessModel", business?.businessModel);
    registerCommentedValue("objectives", "dataConsumerAffiliation", core?.dataConsumerAffiliation);
    registerCommentedList(
      "objectives",
      "motivationDataConsumer",
      objectives?.motivationDataConsumer as unknown[] | undefined,
    );
    registerCommentedList(
      "objectives",
      "dataTrusteeGoals",
      objectives?.dataTrusteeGoals as unknown[] | undefined,
    );
    registerCommentedList(
      "business",
      "businessDomains",
      business?.businessDomains as unknown[] | undefined,
    );
    registerCommentedList(
      "business",
      "fundingSources",
      business?.fundingSources as unknown[] | undefined,
    );
    registerCommentedValue("data", "sourceSystem", dataSection?.sourceSystem);
    registerCommentedList(
      "data",
      "receptionTechnologies",
      dataSection?.receptionTechnologies as unknown[] | undefined,
    );
    registerCommentedValue("data", "receptionFrequency", dataSection?.receptionFrequency);
    registerCommentedList(
      "data",
      "receptionSecurityMeasures",
      dataSection?.receptionSecurityMeasures as unknown[] | undefined,
    );
    registerCommentedList(
      "data",
      "preparationTechniques",
      dataSection?.preparationTechniques as unknown[] | undefined,
    );
    registerCommentedValue("data", "storageTechnique", dataSection?.storageTechnique);
    registerCommentedValue("data", "storageRetention", dataSection?.storageRetention);
    registerCommentedList(
      "data",
      "analysisTechniques",
      dataSection?.analysisTechniques as unknown[] | undefined,
    );
    registerCommentedList(
      "data",
      "forwardingTechniques",
      dataSection?.forwardingTechniques as unknown[] | undefined,
    );
    registerCommentedValue("data", "forwardingFrequency", dataSection?.forwardingFrequency);
    registerCommentedList(
      "data",
      "forwardingSecurityMeasures",
      dataSection?.forwardingSecurityMeasures as unknown[] | undefined,
    );
    registerCommentedValue("data", "targetSystem", dataSection?.targetSystem);
    registerCommentedValue("business", "paymentMethodDataOwner", business?.paymentMethodDataOwner);
    registerCommentedValue(
      "business",
      "paymentMethodDataConsumer",
      business?.paymentMethodDataConsumer,
    );
    registerCommentedValue("legal", "consentType", legalSection?.consentType);
    registerCommentedValue("legal", "consentEnteredBy", legalSection?.consentEnteredBy);

    for (const basis of legalSection?.processingBases ?? []) {
      const baseLabel = formatProcessingBasisLabel(basis);
      const basisEntry =
        basis && typeof basis === "object" && !Array.isArray(basis)
          ? (basis as {
              comment?: unknown;
              details?: unknown;
            })
          : undefined;

      registerInfoText("legal", baseLabel, basisEntry?.comment);

      if (Array.isArray(basisEntry?.details)) {
        for (const detail of basisEntry.details) {
          if (!detail || typeof detail !== "object" || Array.isArray(detail)) {
            continue;
          }

          const detailEntry = detail as { title?: unknown; description?: unknown };
          registerInfoText(
            "legal",
            toTagContent(detailEntry.title) ?? null,
            detailEntry.description,
          );
          registerInfoText(
            "legal",
            toTagContent(detailEntry.description) ?? null,
            detailEntry.description,
          );
        }
      }
    }

    return infoByTagId;
  }, [
    business?.businessDomains,
    business?.businessModel,
    business?.fundingSources,
    business?.paymentMethodDataConsumer,
    business?.paymentMethodDataOwner,
    core?.dataCategoryName,
    core?.dataConsumerAffiliation,
    core?.rightsHolderIsRepresented,
    core?.rightsHolderAffiliation,
    dataSection?.analysisTechniques,
    dataSection?.architectureType,
    dataSection?.forwardingFrequency,
    dataSection?.forwardingSecurityMeasures,
    dataSection?.forwardingTechniques,
    dataSection?.preparationTechniques,
    dataSection?.receptionFrequency,
    dataSection?.receptionSecurityMeasures,
    dataSection?.receptionTechnologies,
    dataSection?.sourceSystem,
    dataSection?.storageRetention,
    dataSection?.storageTechnique,
    dataSection?.targetSystem,
    legalSection?.consentEnteredBy,
    legalSection?.consentType,
    legalSection?.processingBases,
    objectives?.dataTrusteeGoals,
    objectives?.motivationDataConsumer,
    resolveMetadataLabel,
    rightsHolderIsRepresented,
  ]);

  useEffect(() => {
    const element = backgroundBoxRef.current;
    if (!element) {
      return;
    }

    const updateSize = () => {
      const rect = element.getBoundingClientRect();
      const nextSize = {
        width: Math.max(0, Math.round(rect.width)),
        height: Math.max(0, Math.round(Math.max(rect.height, element.scrollHeight))),
      };

      setBackgroundSize((currentSize) => {
        if (currentSize.width === nextSize.width && currentSize.height === nextSize.height) {
          return currentSize;
        }

        return nextSize;
      });
    };

    updateSize();

    const observer = new ResizeObserver(() => {
      updateSize();
    });

    observer.observe(element);
    return () => observer.disconnect();
  }, []);

  const handleConfirmExport = async () => {
    if (isExporting) {
      return;
    }

    setIsExporting(true);

    const exportBundle = await createModelExportZip({
      modelName: extractScalarTagValue(core?.dataTrusteeName),
      formData: result.request,
      resultData: result.dataTrusteeModel,
      executiveSummary: result.executiveSummary,
      ttl: result.ttl,
      validation: result.shaclReport,
      avatarDataUrl,
      avatarFileName,
    });

    if (!exportBundle) {
      setIsExporting(false);
      setShowExportModal(false);
      return;
    }

    downloadBlob(exportBundle.blob, exportBundle.fileName);

    setShowExportModal(false);
    setIsExporting(false);
  };

  const selectedTagId = selectedChip ? `${selectedChip.category}:${selectedChip.label}` : null;
  const selectedChipInfoText = selectedTagId
    ? (selectedChipInfoTextByTagId.get(selectedTagId) ?? null)
    : null;

  const handleChipToggle = (label: string, category: TagCategory) => {
    const chipId = `${category}:${label}`;
    if (selectedTagId === chipId) {
      setSelectedChip(null);
      return;
    }

    setSelectedChip({ label, category });
  };

  const renderInteractiveTag = (tag: string, key: string, category: TagCategory) => (
    <CustomTag
      key={key}
      label={tag}
      selected={selectedTagId === `${category}:${tag}`}
      onClick={() => handleChipToggle(tag, category)}
    />
  );

  return (
    <Box
      gap="large"
      animation={{ type: "fadeIn", duration: 300 }}
      direction="column"
    >
      <Box
        direction="row"
        fill="horizontal"
        gap="medium"
      >
        <Box width="small">
          <Image
            src={avatarDataUrl || placeholder}
            fit="contain"
            style={{ borderRadius: 12 }}
            fill="horizontal"
          />
        </Box>
        <Box
          flex
          direction="column"
        >
          <Box
            direction="row"
            gap="small"
            align="center"
            wrap
          >
            <Heading
              level={2}
              margin="none"
            >
              {toTagContent(core?.dataTrusteeName)}
            </Heading>
            {businessFormTag
              ? renderInteractiveTag(
                  `Geschäftsform: ${businessFormTag}`,
                  `header-business-form-${businessFormTag}`,
                  "business",
                )
              : null}
          </Box>
          <Box>{toTagContent(core?.dataTrusteeDescription)}</Box>
        </Box>
        <Box
          direction="row"
          gap="small"
        >
          <Box
            direction="column"
            gap="xsmall"
            align="start"
            width="small"
          >
            <Text weight="bold">Ziele</Text>
            {dataTrusteeGoalTags.map((tag) =>
              renderInteractiveTag(tag, `goal-${tag}`, "objectives"),
            )}
          </Box>
          <Box
            direction="column"
            gap="xsmall"
            align="start"
            width="small"
          >
            <Text weight="bold">Domänen</Text>
            {businessDomainTags.map((tag) =>
              renderInteractiveTag(tag, `domain-${tag}`, "business"),
            )}
          </Box>
          <Box
            direction="column"
            gap="xsmall"
            align="start"
            width="small"
          >
            <Text weight="bold">Gefördert durch</Text>
            {fundingSourceTags.map((tag) =>
              renderInteractiveTag(tag, `funding-${tag}`, "business"),
            )}
          </Box>
        </Box>
      </Box>
      <Box
        direction="row"
        gap="medium"
      >
        {/* canvas */}
        <Box
          ref={backgroundBoxRef}
          style={{ position: "relative" }}
          gap="small"
          flex
          alignSelf="stretch"
          height={{ min: "auto" }}
        >
          {/* Erste Zeile: Allgemeine Infos */}
          <Grid columns={["15%", "25%", "35%", "25%"]}>
            <Box
              pad={{ horizontal: "small" }}
              margin="small"
              direction="column"
              gap="xsmall"
              justify="start"
              align="start"
            >
              {rightsHolderName ? <Text weight="bold">{rightsHolderName}</Text> : null}
              {rightsHolderTags.map((tag) =>
                renderInteractiveTag(tag, `rights-holder-${tag}`, "core"),
              )}
            </Box>
            <Box
              margin="small"
              pad={{ horizontal: "small" }}
              direction="column"
              gap="xsmall"
              justify="start"
              align="start"
            >
              {dataOwnerName ? (
                <Text weight="bold">{dataOwnerName}</Text>
              ) : (
                <Text>Dateninhaber: Kein Name angegeben</Text>
              )}
              {dataOwnerTags.map((tag) => renderInteractiveTag(tag, `data-owner-${tag}`, "core"))}
            </Box>
            <Box
              pad={{ horizontal: "small" }}
              margin="small"
              direction="column"
              gap="xsmall"
              justify="start"
              align="start"
            >
              {dataTrusteeName ? <Text weight="bold">{dataTrusteeName}</Text> : null}
              {dataTrusteeTags.map((tag) =>
                renderInteractiveTag(tag, `data-trustee-${tag}`, "data"),
              )}
            </Box>
            <Box
              pad={{ horizontal: "small" }}
              margin="small"
              direction="column"
              gap="xsmall"
              justify="start"
              align="start"
            >
              {dataConsumerName ? <Text weight="bold">{dataConsumerName}</Text> : null}
              {dataConsumerTags.map((tag) =>
                renderInteractiveTag(tag, `data-consumer-${tag}`, "objectives"),
              )}
            </Box>
          </Grid>

          {/* Zweite Zeile: Zahlungsmodelle */}
          <Grid columns={["27.5%", "25%", "10%", "25%", "12.5%"]}>
            <Box />
            <Box
              align="center"
              fill="horizontal"
              pad={{ horizontal: "small" }}
              background="background"
            >
              {renderInteractiveTag(
                toMetadataTagContent(business?.paymentMethodDataOwner, "paymentMethodDataOwner") ||
                  "Kein Zahlungsmodell angegeben",
                "payment-owner",
                "business",
              )}
            </Box>
            <Box />
            <Box
              align="center"
              fill="horizontal"
              pad={{ horizontal: "small" }}
              background="background"
            >
              {renderInteractiveTag(
                toMetadataTagContent(
                  business?.paymentMethodDataConsumer,
                  "paymentMethodDataConsumer",
                ) || "Kein Zahlungsmodell angegeben",
                "payment-consumer",
                "business",
              )}
            </Box>
          </Grid>

          {/* Dritte Zeile: Weitere Details */}
          <Grid columns={["22.5%", "25%", "20%", "20%", "12.5%"]}>
            <Box />
            <Box
              align="center"
              fill="horizontal"
              pad="small"
            >
              <ModelingResultTagCanvasSection
                headlineTag="Quellsystem"
                headlinTagPosition="left"
                tagCategory="data"
                selectedTagId={selectedTagId}
                onTagClick={handleChipToggle}
                subSections={sourceSystemSubSections}
              />
            </Box>
            <Box
              align="center"
              fill="horizontal"
              round="xsmall"
              pad="small"
            >
              {/* Datenverarbeitung */}
              <ModelingResultTagCanvasSection
                tagCategory="data"
                selectedTagId={selectedTagId}
                onTagClick={handleChipToggle}
                subSections={implementationSubSections}
              />

              {/* Daten */}
              <ModelingResultTagCanvasSection
                tagCategory="data"
                selectedTagId={selectedTagId}
                onTagClick={handleChipToggle}
                subSections={dataSubSections}
              />
            </Box>
            <Box
              align="center"
              fill="horizontal"
              pad="small"
            >
              <ModelingResultTagCanvasSection
                headlineTag="Zielsystem"
                headlinTagPosition="right"
                tagCategory="data"
                selectedTagId={selectedTagId}
                onTagClick={handleChipToggle}
                subSections={targetSystemSubSections}
              />
            </Box>
            <Box />
          </Grid>

          {/* Background */}
          <Box
            style={{
              position: "absolute",
              inset: 0,
              pointerEvents: "none",
              zIndex: -1,
            }}
          >
            {backgroundSize.width > 0 && backgroundSize.height > 0 ? (
              <ColumnBackgroundSvg
                columnWidthsPercent={columnWidthsPercent}
                width={backgroundSize.width}
                height={backgroundSize.height}
                gapPx={columnGapPx}
                borderRadiusPx={columnBorderRadiusPx}
              />
            ) : null}
          </Box>
        </Box>
        {/* Info */}
        <Box
          width="20%"
          direction="column"
          gap="small"
          alignSelf="flex-start"
          style={{
            position: "sticky",
            top: 24,
            height: "calc(100vh - 48px)",
          }}
        >
          <Box
            flex
            direction="column"
            background="background"
            gap="small"
            style={{
              overflowY: "auto",
            }}
          >
            {selectedChip ? (
              <Box gap="small">
                <Box>
                  <Text weight="bold">{selectedChip.label}</Text>
                </Box>
                <Box>
                  {selectedChipInfoText ? (
                    <HtmlContent>{selectedChipInfoText}</HtmlContent>
                  ) : (
                    <Text>Kein Kommentar vorhanden.</Text>
                  )}
                </Box>
              </Box>
            ) : (
              <Text color="text-weak">Für mehr Details tätigen Sie eine Auswahl.</Text>
            )}
          </Box>

          {/* Button Box */}
          <Box
            direction="column"
            gap="xsmall"
            alignSelf="end"
            style={{
              marginTop: "auto",
              position: "sticky",
              bottom: 24,
              background: "var(--grommet-global-color-background)",
              paddingTop: 8,
            }}
          >
            {allowExportAndSubmission && (
              <Button
                primary
                label="Model bearbeiten"
                onClick={onBack}
              />
            )}
            <Button
              primary
              label="Vergleich starten"
              onClick={() => {
                setCompareSessionSelectedIds([SESSION_COMPARE_ITEM_ID]);
                navigate(routes.dataTrusteeCompare.path);
              }}
            />
            <Button
              primary
              label="Report ansehen"
              onClick={() => setShowShaclReportModal(true)}
            />
            {result.executiveSummary ? (
              <Button
                primary
                label="Executive Summary"
                onClick={() => setShowExecutiveSummaryModal(true)}
              />
            ) : null}
            {allowExportAndSubmission && (
              <Button
                primary
                label="Zur Freigabe einreichen"
                onClick={() => setShowSubmissionModal(true)}
              />
            )}
            {allowExportAndSubmission && (
              <Button
                primary
                label="Exportieren"
                onClick={() => setShowExportModal(true)}
              />
            )}
          </Box>
        </Box>
      </Box>
      <ModelingResultExportModal
        open={showExportModal}
        onClose={() => setShowExportModal(false)}
        onConfirm={handleConfirmExport}
        isProcessing={isExporting}
      />
      <ModelingResultShaclReportModal
        open={showShaclReportModal}
        report={result.shaclReport}
        onClose={() => setShowShaclReportModal(false)}
      />
      <ModelingResultExecutiveSummaryModal
        open={showExecutiveSummaryModal}
        executiveSummary={result.executiveSummary}
        onClose={() => setShowExecutiveSummaryModal(false)}
      />
      <ModelingResultSubmissionModal
        open={showSubmissionModal}
        result={result}
        avatarDataUrl={avatarDataUrl}
        avatarFileName={avatarFileName}
        onClose={() => setShowSubmissionModal(false)}
      />
    </Box>
  );
};

export default ModelingResultTagCanvasView;
