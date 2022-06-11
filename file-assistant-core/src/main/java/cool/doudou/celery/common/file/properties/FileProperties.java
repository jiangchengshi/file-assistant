package cool.doudou.celery.common.file.properties;

import cool.doudou.celery.common.file.enums.StorageModeEnum;
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
    private String storageMode = StorageModeEnum.LOCAL.name();
}
