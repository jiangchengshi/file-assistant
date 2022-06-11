package cool.doudou.file.assistant.boot.starter.auto.configuration;

import cool.doudou.celery.common.file.config.AliYunConfig;
import cool.doudou.celery.common.file.config.GridFsConfig;
import cool.doudou.celery.common.file.enums.StorageModeEnum;
import cool.doudou.celery.common.file.helper.*;
import cool.doudou.celery.common.file.properties.AliYunProperties;
import cool.doudou.celery.common.file.properties.FileProperties;
import cool.doudou.celery.common.file.properties.GridFsProperties;
import cool.doudou.celery.common.file.properties.LocalProperties;
import cool.doudou.celery.common.file.helper.*;
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
@EnableConfigurationProperties({FileProperties.class, LocalProperties.class, GridFsProperties.class, AliYunProperties.class})
@Import({GridFsConfig.class, AliYunConfig.class})
@Configuration
public class FileAutoConfiguration {
    private FileProperties fileProperties;

    @ConditionalOnMissingBean(FileHelper.class)
    @Bean
    public FileHelper fileHelper() {
        switch (StorageModeEnum.getByName(fileProperties.getStorageMode())) {
            case GRID_FS:
                return new GridFsHelper();
            case ALI_YUN:
                return new AliYunHelper();
            case MINIO:
                return new MinIoHelper();
            default:
                return new LocalHelper();
        }
    }

    @Autowired
    public void setFileProperties(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }
}
