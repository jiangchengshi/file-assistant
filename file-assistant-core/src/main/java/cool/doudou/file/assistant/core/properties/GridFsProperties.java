package cool.doudou.file.assistant.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * GridFsProperties
 *
 * @author jiangcs
 * @since 2022/3/6
 */
@Data
public class GridFsProperties {
    private String serverUri = "mongodb://admin:1234.abcd@127.0.0.1:6379";
    private String database = "files";
    private String bucketName = "default";

    private Integer connectionsPerHost = 8;
    private Integer minConnectionsPerHost = 3;
    private Integer threadsAllowedToBlockForConnectionMultiplier = 4;
    private Integer connectTimeout = 1000;
    private Integer maxWaitTime = 1500;
    private Boolean socketKeepAlive = true;
    private Integer socketTimeout = 1500;
}
