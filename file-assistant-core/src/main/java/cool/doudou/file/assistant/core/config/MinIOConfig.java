package cool.doudou.file.assistant.core.config;

import cool.doudou.file.assistant.core.properties.MinIoProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * MinIOConfig
 *
 * @author jiangcs
 * @since 2022/3/8
 */
@AllArgsConstructor
@ConditionalOnProperty(name = "file.storage-mode", havingValue = "minIO")
public class MinIOConfig {
    private MinIoProperties minIoProperties;

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minIoProperties.getEndpoint())
                .credentials(minIoProperties.getAccessKey(), minIoProperties.getSecretKey())
                .build();

        try {
            // 若不存在Bucket，则初始化创建
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minIoProperties.getBucketName()).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minIoProperties.getBucketName())
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return minioClient;
    }
}
