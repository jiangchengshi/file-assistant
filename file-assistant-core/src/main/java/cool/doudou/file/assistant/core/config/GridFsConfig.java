package cool.doudou.file.assistant.core.config;

import cool.doudou.file.assistant.core.properties.GridFsProperties;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * GridFsConfig
 *
 * @author jiangcs
 * @since 2022/3/5
 */
@AllArgsConstructor
@ConditionalOnProperty(name = "file.storage-mode", havingValue = "gridFs")
public class GridFsConfig {
    private GridFsProperties gridFsProperties;

    @Bean
    public MongoClient mongoClient() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(gridFsProperties.getServerUri() + "/" + gridFsProperties.getDatabase()))
                .build();
        return MongoClients.create(settings);
    }

    @Bean
    public GridFSBucket gridFsBucket(MongoClient mongoClient) {
        return GridFSBuckets.create(mongoClient.getDatabase(gridFsProperties.getDatabase()), gridFsProperties.getBucketName());
    }
}
