package cool.doudou.file.assistant.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * LocalProperties
 *
 * @author jiangcs
 * @since 2022/3/6
 */
@Data
public class LocalProperties {
    private String path = "/home/assets/file";
}
