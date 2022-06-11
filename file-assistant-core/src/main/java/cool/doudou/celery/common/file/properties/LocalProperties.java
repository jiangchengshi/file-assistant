package cool.doudou.celery.common.file.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * LocalProperties
 *
 * @author jiangcs
 * @since 2022/3/6
 */
@Data
@ConfigurationProperties(prefix = "file.local")
public class LocalProperties {
    private String path = "/home/assets/file";
}
