package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.datatrusteemodel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import de.fraunhofer.iese.forgetrustee.demonstrator.backend.config.BackendProperties;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.upload.DataTrusteeModelUploadRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.upload.DataTrusteeModelUploadResponseDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardCoreRequestDto;
import de.fraunhofer.iese.forgetrustee.demonstrator.backend.dto.controller.rest.datatrusteemodel.wizard.request.DataTrusteeWizardRequestDto;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

class DataTrusteeModelUploadServiceTest {

  private static final Pattern UPLOAD_DIRECTORY_PATTERN = Pattern.compile(
      "^forgetrustee-datatrusteemodel-\\d{17}-Acme-Demo-Model-.*$");

  private final ObjectMapper objectMapper = new ObjectMapper();

  private static final DataTrusteeModelUploadNotificationService NO_OP_NOTIFICATION =
      (uploadRequest, uploadResponse) -> {
      };

  @Test
  void storesWizardDataAndImageInTemporaryDirectory() throws IOException {
    final Path uploadBaseDirectory = Files.createTempDirectory("forgetrustee-upload-base-");
    final DataTrusteeWizardRequestDto wizardData = DataTrusteeWizardRequestDto
        .builder()
        .core(DataTrusteeWizardCoreRequestDto
            .builder()
            .dataTrusteeName("Acme / Demo: Model?")
            .build())
        .build();

    final DataTrusteeModelUploadRequestDto uploadRequest = DataTrusteeModelUploadRequestDto
        .builder()
        .wizardData(wizardData)
        .name("Max Mustermann")
        .email("max@example.com")
        .honeypot("")
        .build();

    final MockMultipartFile image = new MockMultipartFile("image", "demo.png", "image/png",
        this.createPngBytes());

    final AtomicBoolean notificationCalled = new AtomicBoolean(false);
    final DataTrusteeModelUploadService service = this.newService(uploadBaseDirectory,
        350L * 1024L, (uploadRequestDto, uploadResponseDto) -> notificationCalled.set(true));

    final DataTrusteeModelUploadResponseDto response = service
        .storeInTemporaryDirectory(uploadRequest, image);

    final Path wizardFile = Path.of(response.getWizardDataFilePath());
    final Path imageFile = Path.of(response.getImageFilePath());
    final String uploadDirectoryName = wizardFile.getParent().getFileName().toString();
    final Path infoFile = wizardFile.getParent().resolve("info.txt");

    assertThat(wizardFile).exists();
    assertThat(imageFile).exists();
    assertThat(infoFile).exists();
    assertThat(response.getDataTrusteeName()).isEqualTo("Acme / Demo: Model?");
    assertThat(response.getWizardDataFileName()).isEqualTo("Acme-Demo-Model.json");
    assertThat(response.getImageFileName()).isEqualTo("Acme-Demo-Model-image.png");
    assertThat(response.getImageContentType()).isEqualTo("image/png");
    assertThat(response.getWizardDataBytesWritten()).isPositive();
    assertThat(response.getImageBytesWritten()).isPositive();
    assertThat(response.getUploadDirectory()).isEqualTo(wizardFile.getParent().toString());
    assertThat(wizardFile).startsWith(uploadBaseDirectory);
    assertThat(imageFile.getParent()).isEqualTo(wizardFile.getParent());
    assertThat(uploadDirectoryName).matches(UPLOAD_DIRECTORY_PATTERN);
    final String infoContent = Files.readString(infoFile);
    assertThat(infoContent).contains("uploadDate=");
    assertThat(infoContent).contains(response.getCreatedAt());
    assertThat(infoContent).contains("name=Max Mustermann");
    assertThat(infoContent).contains("email=max@example.com");
    assertThat(notificationCalled.get()).isTrue();

    Files.deleteIfExists(wizardFile);
    Files.deleteIfExists(imageFile);
    Files.deleteIfExists(infoFile);
    Files.deleteIfExists(wizardFile.getParent());
    Files.deleteIfExists(uploadBaseDirectory);
  }

  @Test
  void rejectsNonImageFiles() {
    final Path uploadBaseDirectory;
    try {
      uploadBaseDirectory = Files.createTempDirectory("forgetrustee-upload-base-");
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
    final DataTrusteeWizardRequestDto wizardData = DataTrusteeWizardRequestDto.builder().build();
    final DataTrusteeModelUploadRequestDto uploadRequest = DataTrusteeModelUploadRequestDto
        .builder()
        .wizardData(wizardData)
        .build();
    final MockMultipartFile file = new MockMultipartFile("image", "note.txt", "text/plain",
        "hello".getBytes());
    final DataTrusteeModelUploadService service = this.newService(uploadBaseDirectory,
        350L * 1024L);

    assertThatThrownBy(() -> service
        .storeInTemporaryDirectory(uploadRequest, file))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("must be an image");

    try {
      Files.deleteIfExists(uploadBaseDirectory);
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Test
  void rejectsOversizedImages() throws IOException {
    final Path uploadBaseDirectory = Files.createTempDirectory("forgetrustee-upload-base-");
    final DataTrusteeWizardRequestDto wizardData = DataTrusteeWizardRequestDto.builder().build();
    final DataTrusteeModelUploadRequestDto uploadRequest = DataTrusteeModelUploadRequestDto
        .builder()
        .wizardData(wizardData)
        .build();
    final long maxUploadSizeBytes = 300L * 1024L;
    final byte[] bytes = new byte[(int) maxUploadSizeBytes + 1];
    bytes[0] = (byte) 0x89;
    final MockMultipartFile file = new MockMultipartFile("image", "big.png", "image/png", bytes);
    final DataTrusteeModelUploadService service = this.newService(uploadBaseDirectory,
        maxUploadSizeBytes);

    assertThatThrownBy(() -> service
        .storeInTemporaryDirectory(uploadRequest, file))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("exceeds maximum size");

    Files.deleteIfExists(uploadBaseDirectory);
  }

  @Test
  void rejectsFilledHoneypot() throws IOException {
    final Path uploadBaseDirectory = Files.createTempDirectory("forgetrustee-upload-base-");
    final DataTrusteeWizardRequestDto wizardData = DataTrusteeWizardRequestDto.builder().build();
    final DataTrusteeModelUploadRequestDto uploadRequest = DataTrusteeModelUploadRequestDto
        .builder()
        .wizardData(wizardData)
        .honeypot("bot-detected")
        .build();
    final MockMultipartFile image = new MockMultipartFile("image", "demo.png", "image/png",
        this.createPngBytes());
    final DataTrusteeModelUploadService service = this.newService(uploadBaseDirectory,
        350L * 1024L);

    assertThatThrownBy(() -> service
        .storeInTemporaryDirectory(uploadRequest, image))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid upload payload");

    Files.deleteIfExists(uploadBaseDirectory);
  }

  @Test
  void keepsUploadSuccessfulWhenNotificationFails() throws IOException {
    final Path uploadBaseDirectory = Files.createTempDirectory("forgetrustee-upload-base-");
    final DataTrusteeWizardRequestDto wizardData = DataTrusteeWizardRequestDto
        .builder()
        .core(DataTrusteeWizardCoreRequestDto.builder().dataTrusteeName("Acme Model").build())
        .build();
    final DataTrusteeModelUploadRequestDto uploadRequest = DataTrusteeModelUploadRequestDto
        .builder()
        .wizardData(wizardData)
        .name("Max Mustermann")
        .email("max@example.com")
        .build();
    final MockMultipartFile image = new MockMultipartFile("image", "demo.png", "image/png",
        this.createPngBytes());

    final DataTrusteeModelUploadService service = this.newService(uploadBaseDirectory,
        350L * 1024L, (uploadRequestDto, uploadResponseDto) -> {
          throw new IllegalStateException("mail failed");
        });

    final DataTrusteeModelUploadResponseDto response = service
        .storeInTemporaryDirectory(uploadRequest, image);

    final Path wizardFile = Path.of(response.getWizardDataFilePath());
    final Path imageFile = Path.of(response.getImageFilePath());
    assertThat(wizardFile).exists();
    assertThat(imageFile).exists();

    Files.deleteIfExists(wizardFile);
    Files.deleteIfExists(imageFile);
    Files.deleteIfExists(wizardFile.getParent().resolve("info.txt"));
    Files.deleteIfExists(wizardFile.getParent());
    Files.deleteIfExists(uploadBaseDirectory);
  }

  private DataTrusteeModelUploadService newService(Path uploadBaseDirectory,
      long maxUploadSizeBytes) {
    return this.newService(uploadBaseDirectory, maxUploadSizeBytes, NO_OP_NOTIFICATION);
  }

  private DataTrusteeModelUploadService newService(Path uploadBaseDirectory,
      long maxUploadSizeBytes, DataTrusteeModelUploadNotificationService notificationService) {
    final BackendProperties properties = new BackendProperties();
    final BackendProperties.DataTrusteeModelProperties dataTrusteeModelProperties = new BackendProperties.DataTrusteeModelProperties();
    dataTrusteeModelProperties.setUploadBasePath(uploadBaseDirectory.toString());
    dataTrusteeModelProperties.setMaxUploadSizeBytes(maxUploadSizeBytes);
    properties.setDataTrusteeModel(dataTrusteeModelProperties);
    return new DataTrusteeModelUploadService(this.objectMapper, properties,
        notificationService);
  }

  private byte[] createPngBytes() throws IOException {
    final BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, 0xFF336699);

    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(image, "png", outputStream);
    return outputStream.toByteArray();
  }
}
