package cool.doudou.celery.common.file.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * AliYunProperties
 *
 * @author jiangcs
 * @since 2022/3/8
 */
@Data
@ConfigurationProperties(prefix = "file.ali-yun")
public class AliYunProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
}
