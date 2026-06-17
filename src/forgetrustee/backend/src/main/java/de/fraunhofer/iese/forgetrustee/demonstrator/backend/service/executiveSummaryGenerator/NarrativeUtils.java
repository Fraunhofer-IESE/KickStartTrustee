/*
 * Copyright (C) 2026 Fraunhofer IESE
 *
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.executiveSummaryGenerator;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.DataTrusteeModelCore;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.domain.datatrusteemodel.helperDTO.TitleDescriptionDTO;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ConsentEnteredBy;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.shared.vocabulary.legal.ProcessingBasis;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NarrativeUtils {

  public static String safe(String value, String fallback) {
    return isBlank(value) ? fallback : value;
  }

  public static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  public static boolean isPresent(String value) {
    return !isBlank(value);
  }

  public static boolean isRelevantEnum(Enum<?> e) {
    if (e == null) {
      return false;
    }

    String name = e.name();

    return !name.equals("CUSTOM")
            && !name.equals("NONE")
            && !name.equals("NOT_PROVIDED")
            && !name.equals("UNKNOWN")
            && !name.equals("AMBIGUOUS")
            && !name.equals("AMIGIOUS");
  }

  public static String yesNoUnknown(String value) {
    if (isBlank(value)) {
      return "keine Angabe";
    }

    final String v = value.trim().toLowerCase();

    return switch (v) {
      case "true", "ja" -> "ja";
      case "false", "nein" -> "nein";
      default -> value;
    };
  }

  public static String enumLabelDe(Enum<?> e, String fallback) {
    if (!isRelevantEnum(e)) {
      return fallback;
    }

    try {
      final Method m = e.getClass().getMethod("getLabelDe");
      final Object result = m.invoke(e);

      if (result instanceof final String s && !s.isBlank()) {
        return s;
      }
    } catch (final Exception ignored) {
    }

    return e.name();
  }

  public static String enumSetLabelsDe(EnumSet<?> set, String delimiter) {
    if (set == null || set.isEmpty()) {
      return "keine Angabe";
    }

    String result = set.stream()
            .filter(NarrativeUtils::isRelevantEnum)
            .map(e -> enumLabelDe(e, ""))
            .filter(s -> s != null && !s.isBlank())
            .collect(Collectors.joining(delimiter));

    return result.isBlank() ? "keine Angabe" : result;
  }

  public static String enumSetLabelsDeOrEmpty(EnumSet<?> set, String delimiter) {
    if (set == null || set.isEmpty()) {
      return "";
    }

    return naturalJoin(
            set.stream()
                    .filter(NarrativeUtils::isRelevantEnum)
                    .map(e -> enumLabelDe(e, ""))
                    .filter(s -> s != null && !s.isBlank())
                    .toList()
    );
  }

  public static boolean hasRelevantValues(EnumSet<?> set) {
    return set != null && set.stream().anyMatch(NarrativeUtils::isRelevantEnum);
  }

  public static String processingBasesToString(
          Map<ProcessingBasis, List<TitleDescriptionDTO>> map,
          String delimiter
  ) {
    if (map == null || map.isEmpty()) {
      return "keine Angabe";
    }

    return naturalJoin(
            map.keySet()
                    .stream()
                    .filter(NarrativeUtils::isRelevantEnum)
                    .map(e -> enumLabelDe(e, ""))
                    .filter(s -> s != null && !s.isBlank())
                    .toList()
    );

  }

  public static String titleDescriptionListToString(List<TitleDescriptionDTO> list) {
    if (list == null || list.isEmpty()) {
      return "";
    }

    return list.stream()
            .map(td -> {
              final String title = td.getTitle() != null ? td.getTitle() : "";
              final String desc = td.getDescription() != null ? td.getDescription() : "";

              if (!title.isBlank() && !desc.isBlank()) {
                return "„" + title + "“ (" + desc + ")";
              }

              if (!title.isBlank()) {
                return "„" + title + "“";
              }

              return desc;
            })
            .filter(s -> !s.isBlank())
            .collect(Collectors.collectingAndThen(
                    Collectors.toList(),
                    NarrativeUtils::naturalJoin
            ));
  }

  public static String processingBasisDetailsToString(
          Map<ProcessingBasis, List<TitleDescriptionDTO>> map
  ) {
    if (map == null || map.isEmpty()) {
      return "";
    }

    return map.entrySet()
            .stream()
            .filter(e -> isRelevantEnum(e.getKey()))
            .map(e -> {
              final String basis = enumLabelDe(e.getKey(), "");
              final String details = titleDescriptionListToString(e.getValue());

              if (details == null || details.isBlank()) {
                return basis;
              }

              return basis + ": " + details;
            })
            .filter(s -> s != null && !s.isBlank())
            .collect(Collectors.joining("; "));
  }

  public static String mapRoleToLabel(
          ConsentEnteredBy role,
          DataTrusteeModelCore core
  ) {
    if (role == null || core == null) {
      return "keine Angabe";
    }

    return switch (role) {
      case RIGHTS_OWNER -> safe(core.getRightsHolderName(), "Rechteinhaber");
      case DATA_OWNER -> safe(core.getDataOwnerName(), "Datengeber");
      default -> "keine Angabe";
    };
  }

  public static void appendSentence(StringBuilder sb, String sentence) {
    if (isBlank(sentence)) {
      return;
    }

    sb.append(sentence.trim());

    if (!sentence.endsWith(".")
            && !sentence.endsWith("!")
            && !sentence.endsWith("?")) {
      sb.append(".");
    }

    sb.append(" ");
  }

  public static String naturalJoin(
          List<String> items
  ) {

    if (items == null || items.isEmpty()) {
      return "";
    }

    final List<String> filtered =
            items.stream()
                    .filter(s -> s != null && !s.isBlank())
                    .toList();

    if (filtered.isEmpty()) {
      return "";
    }

    if (filtered.size() == 1) {
      return filtered.get(0);
    }

    if (filtered.size() == 2) {
      return filtered.get(0)
              + " und "
              + filtered.get(1);
    }

    return String.join(
            ", ",
            filtered.subList(0, filtered.size() - 1)
    ) + " und "
            + filtered.get(filtered.size() - 1);
  }

  public static String booleanNarrative(
          String value,
          String trueText,
          String falseText,
          String unknownText
  ) {

    if (value == null || value.isBlank()) {
      return unknownText;
    }

    String normalized =
            value.trim().toLowerCase();

    switch (normalized) {

      case "true":
      case "ja":
        return trueText;

      case "false":
      case "nein":
        return falseText;

      default:
        return unknownText;
    }
  }

  public static String dataSensitivityNarrative(
          String personalData,
          String specialPersonalData,
          String tradeSecrets
  ) {

    boolean personal =
            "ja".equalsIgnoreCase(
                    yesNoUnknown(personalData)
            );

    boolean special =
            "ja".equalsIgnoreCase(
                    yesNoUnknown(specialPersonalData)
            );

    boolean trade =
            "ja".equalsIgnoreCase(
                    yesNoUnknown(tradeSecrets)
            );

    // -------------------------------------------------
    // Keine personenbezogenen Daten
    // -------------------------------------------------

    if (!personal && !special && trade) {

      return "Die Verarbeitung umfasst keine personenbezogenen oder besonderen personenbezogenen Daten, beinhaltet jedoch Geschäfts- bzw. Betriebsgeheimnisse";
    }

    if (!personal && !special && !trade) {

      return "Die Verarbeitung umfasst weder personenbezogene Daten noch Geschäfts- bzw. Betriebsgeheimnisse";
    }

    // -------------------------------------------------
    // Personenbezogene Daten vorhanden
    // -------------------------------------------------

    if (personal && special && trade) {

      return "Die Verarbeitung umfasst personenbezogene Daten, besondere Kategorien personenbezogener Daten sowie Geschäfts- bzw. Betriebsgeheimnisse";
    }

    if (personal && special) {

      return "Die Verarbeitung umfasst personenbezogene Daten einschließlich besonderer Kategorien personenbezogener Daten";
    }

    if (personal && trade) {

      return "Die Verarbeitung umfasst personenbezogene Daten sowie Geschäfts- bzw. Betriebsgeheimnisse";
    }

    if (personal) {

      return "Die Verarbeitung umfasst personenbezogene Daten";
    }

    // -------------------------------------------------
    // Nur Geschäftsgeheimnisse
    // -------------------------------------------------

    if (trade) {

      return "Die Verarbeitung beinhaltet Geschäfts- bzw. Betriebsgeheimnisse";
    }

    // -------------------------------------------------
    // Fallback
    // -------------------------------------------------

    return "Zu den verarbeiteten Datenarten liegen nur eingeschränkte Angaben vor";
  }
}
