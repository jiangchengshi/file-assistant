package cool.doudou.file.assistant.core.config;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import cool.doudou.file.assistant.core.properties.AliYunProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * AliYunConfig
 *
 * @author jiangcs
 * @since 2022/3/8
 */
@AllArgsConstructor
@ConditionalOnProperty(name = "file.storage-mode", havingValue = "aliYun")
public class AliYunConfig {
    private AliYunProperties aliYunProperties;

    @Bean
    public OSS ossClient() {
        OSS ossClient = new OSSClientBuilder().build(
                aliYunProperties.getEndpoint(),
                aliYunProperties.getAccessKeyId(),
                aliYunProperties.getAccessKeySecret()
        );

        try {
            // 若不存在Bucket，则初始化创建
            if (!ossClient.doesBucketExist(aliYunProperties.getBucketName())) {
                ossClient.createBucket(aliYunProperties.getBucketName());
            }
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
        }

        return ossClient;
    }
}
