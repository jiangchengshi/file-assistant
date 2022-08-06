package cool.doudou.file.assistant.boot.starter.auto.configuration;

import cool.doudou.file.assistant.core.config.AliYunConfig;
import cool.doudou.file.assistant.core.config.GridFsConfig;
import cool.doudou.file.assistant.core.config.MinIOConfig;
import cool.doudou.file.assistant.core.enums.StorageModeEnum;
import cool.doudou.file.assistant.core.helper.*;
import cool.doudou.file.assistant.core.properties.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * FileAutoConfiguration
 *
 * @author jiangcs
 * @since 2022/2/16
 */
@EnableConfigurationProperties({FileProperties.class, GridFsProperties.class, AliYunProperties.class, MinIoProperties.class, LocalProperties.class})
@Import({GridFsConfig.class, AliYunConfig.class, MinIOConfig.class})
@Configuration
public class FileAutoConfiguration {
    private FileProperties fileProperties;

    @ConditionalOnMissingBean
    @Bean
    public FileHelper fileHelper() {
        switch (StorageModeEnum.getByName(fileProperties.getStorageMode())) {
            case GRID_FS:
                return new GridFsHelper();
            case ALI_YUN:
                return new AliYunHelper();
            case MINIO:
                return new MinIOHelper();
            default:
                return new LocalHelper();
        }
    }

    @Autowired
    public void setFileProperties(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }
}
