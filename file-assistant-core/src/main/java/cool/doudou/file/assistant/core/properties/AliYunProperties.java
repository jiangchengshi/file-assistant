package cool.doudou.file.assistant.core.properties;

import lombok.Data;

/**
 * AliYunProperties
 *
 * @author jiangcs
 * @since 2022/3/8
 */
@Data
public class AliYunProperties {
    private String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    private String accessKeyId = "admin";
    private String accessKeySecret = "1234.abcd";
    private String bucketName = "default";
}
