package cool.doudou.file.assistant.core.properties;

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
    private String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    private String accessKeyId = "admin";
    private String accessKeySecret = "1234.abcd";
    private String bucketName = "default";
}
