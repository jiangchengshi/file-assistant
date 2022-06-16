package cool.doudou.file.assistant.core.properties;

import cool.doudou.file.assistant.core.enums.StorageModeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FileProperties
 *
 * @author jiangcs
 * @since 2022/2/20
 */
@Data
@ConfigurationProperties(prefix = "file")
public class FileProperties {
    /**
     * 存储模式
     */
    private String storageMode = StorageModeEnum.LOCAL.name();
}
