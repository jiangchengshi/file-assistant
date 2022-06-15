package cool.doudou.file.assistant.core.properties;

import lombok.Data;

/**
 * MinIoProperties
 *
 * @author jiangcs
 * @since 2022/3/8
 */
@Data
public class MinIoProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
}
