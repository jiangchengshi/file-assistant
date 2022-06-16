package cool.doudou.file.assistant.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MinIoProperties
 *
 * @author jiangcs
 * @since 2022/3/8
 */
@Data
@ConfigurationProperties(prefix = "file.min-io")
public class MinIoProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
