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

package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.datatrusteemodel;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.upload.DataTrusteeModelUploadRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.upload.DataTrusteeModelUploadResponseDto;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataTrusteeModelUploadMailNotificationService
    implements DataTrusteeModelUploadNotificationService {

  private static final String UPLOAD_MAIL_SUBJECT = "Neues Datentreuhandmodell hochgeladen";

  private final JavaMailSender mailSender;

  private final BackendProperties backendProperties;

  @Override
  public void notifyAboutUpload(DataTrusteeModelUploadRequestDto uploadRequest,
      DataTrusteeModelUploadResponseDto uploadResponse) {
    final BackendProperties.MailProperties mailProperties = this.backendProperties.getMail();
    if (mailProperties == null || !mailProperties.isEnabled()) {
      return;
    }

    final List<String> recipients = this.resolveRecipients(mailProperties);
    if (recipients.isEmpty()) {
      log.debug("Skipping upload notification mail because no recipients are configured");
      return;
    }

    final String body = this.buildMailBody(uploadRequest, uploadResponse);
    if (mailProperties.isIncludeUploadAttachments()) {
      this.sendMailWithAttachments(mailProperties, recipients, body, uploadResponse);
      return;
    }
    this.sendPlainTextMail(mailProperties, recipients, body);
  }

  private void sendPlainTextMail(BackendProperties.MailProperties mailProperties,
      List<String> recipients, String body) {
    final SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(recipients.toArray(String[]::new));
    message.setSubject(UPLOAD_MAIL_SUBJECT);
    message.setText(body);
    this.applyFromAddress(mailProperties, message);
    this.mailSender.send(message);
  }

  private void sendMailWithAttachments(BackendProperties.MailProperties mailProperties,
      List<String> recipients, String body, DataTrusteeModelUploadResponseDto uploadResponse) {
    final MimeMessage message = this.mailSender.createMimeMessage();
    try {
      final MimeMessageHelper helper =
          new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
      helper.setTo(recipients.toArray(String[]::new));
      helper.setSubject(UPLOAD_MAIL_SUBJECT);
      helper.setText(body, false);
      this.applyFromAddress(mailProperties, helper);

      this.addAttachmentIfPresent(helper, uploadResponse.getWizardDataFilePath(),
          uploadResponse.getWizardDataFileName(), MediaType.APPLICATION_JSON_VALUE);
      this.addAttachmentIfPresent(helper, uploadResponse.getImageFilePath(),
          uploadResponse.getImageFileName(), uploadResponse.getImageContentType());
    } catch (MessagingException ex) {
      throw new IllegalStateException("Failed to prepare upload notification mail", ex);
    }

    this.mailSender.send(message);
  }

  private void addAttachmentIfPresent(MimeMessageHelper helper, String filePath,
      String attachmentName, String contentType) throws MessagingException {
    if (filePath == null || filePath.isBlank()) {
      return;
    }

    final Path path = Path.of(filePath);
    if (!Files.exists(path)) {
      log.warn("Skipping missing upload mail attachment {}", path);
      return;
    }

    final String resolvedName = (attachmentName == null || attachmentName.isBlank())
        ? path.getFileName().toString()
        : attachmentName;
    helper.addAttachment(resolvedName, new FileSystemResource(path.toFile()),
        this.resolveContentType(contentType));
  }

  private String resolveContentType(String contentType) {
    if (contentType == null || contentType.isBlank()) {
      return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }
    return contentType;
  }

  private void applyFromAddress(BackendProperties.MailProperties mailProperties,
      SimpleMailMessage message) {
    this.resolveFromAddress(mailProperties).ifPresent(message::setFrom);
  }

  private void applyFromAddress(BackendProperties.MailProperties mailProperties,
      MimeMessageHelper helper) throws MessagingException {
    final Optional<String> fromAddress = this.resolveFromAddress(mailProperties);
    if (fromAddress.isPresent()) {
      helper.setFrom(fromAddress.get());
    }
  }

  private Optional<String> resolveFromAddress(BackendProperties.MailProperties mailProperties) {
    if (mailProperties.getFrom() == null || mailProperties.getFrom().isBlank()) {
      return Optional.empty();
    }
    return Optional.of(mailProperties.getFrom().trim());
  }

  private List<String> resolveRecipients(BackendProperties.MailProperties mailProperties) {
    final List<String> configuredRecipients = mailProperties.getRecipients();
    if (configuredRecipients == null) {
      return List.of();
    }
    return configuredRecipients.stream()
        .filter(recipient -> recipient != null && !recipient.isBlank())
        .map(String::trim)
        .distinct()
        .toList();
  }

  private String buildMailBody(DataTrusteeModelUploadRequestDto uploadRequest,
      DataTrusteeModelUploadResponseDto uploadResponse) {
    final String modelName = uploadResponse == null ? null : uploadResponse.getDataTrusteeName();
    final String requesterName = uploadRequest == null ? null : uploadRequest.getName();
    final String requesterEmail = uploadRequest == null ? null : uploadRequest.getEmail();

    return "Ein neues Datentreuhandmodell wurde hochgeladen.\n\n"
        + "Modelname: " + this.orUnknown(modelName) + "\n"
        + "Name: " + this.orUnknown(requesterName) + "\n"
        + "E-Mail: " + this.orUnknown(requesterEmail) + "\n";
  }

  private String orUnknown(String value) {
    if (value == null || value.isBlank()) {
      return "unbekannt";
    }
    return value.trim();
  }
}
