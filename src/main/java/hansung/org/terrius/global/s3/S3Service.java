package hansung.org.terrius.global.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile image) {
        return uploadImage(image, "images");
    }

    public String uploadImage(MultipartFile image, String directory) {
        validateImage(image);

        String key = createObjectKey(image.getOriginalFilename(), directory);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(getContentType(image))
                .contentLength(image.getSize())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));
        } catch (IOException e) {
            throw new IllegalStateException("S3 이미지 업로드에 실패했습니다.", e);
        }

        return getFileUrl(key);
    }

    public void deleteFile(String fileUrl) {
        String key = extractObjectKey(fileUrl);
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    private void validateImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 비어있습니다.");
        }

        String contentType = image.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }
    }

    private String createObjectKey(String originalFilename, String directory) {
        String extension = getExtension(originalFilename);
        String normalizedDirectory = normalizeDirectory(directory);
        return normalizedDirectory + "/" + UUID.randomUUID() + extension;
    }

    private String normalizeDirectory(String directory) {
        if (directory == null || directory.isBlank()) {
            return "images";
        }
        return directory.replaceAll("^/+", "").replaceAll("/+$", "");
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    private String getContentType(MultipartFile image) {
        return Objects.requireNonNullElse(image.getContentType(), DEFAULT_CONTENT_TYPE);
    }

    private String getFileUrl(String key) {
        URL url = s3Client.utilities()
                .getUrl(builder -> builder.bucket(bucket).key(key));
        return url.toString();
    }

    private String extractObjectKey(String fileUrl) {
        String path = URI.create(fileUrl).getPath();

        if (path == null || path.isBlank() || path.equals("/")) {
            throw new IllegalArgumentException("S3 파일 URL 형식이 올바르지 않습니다.");
        }

        if (path.startsWith("/" + bucket + "/")) {
            return path.substring(bucket.length() + 2);
        }

        return path.substring(1);
    }
}
