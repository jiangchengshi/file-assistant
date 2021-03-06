package cool.doudou.file.assistant.core;

import cool.doudou.file.assistant.core.entity.FileResult;
import cool.doudou.file.assistant.core.helper.LocalHelper;
import cool.doudou.file.assistant.core.helper.MinIOHelper;
import cool.doudou.file.assistant.core.properties.LocalProperties;
import cool.doudou.file.assistant.core.properties.MinIoProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;

/**
 * FileAssistantTest
 *
 * @author jiangcs
 * @since 2022/6/15
 */
public class FileAssistantTest {
    @Test
    public void uploadLocal() {
        try {
            ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");

            LocalHelper localHelper = new LocalHelper();
            localHelper.setLocalProperties(applicationContext.getBean("localProperties", LocalProperties.class));

            File file = new File("/Users/jiangcs/Downloads/1.txt");
            MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", new FileInputStream(file));
            FileResult fileResult = localHelper.upload(multipartFile, "test");
            System.out.println(fileResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadMinIO() {
        try {
            ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");

            MinIoProperties minIoProperties = applicationContext.getBean("minIoProperties", MinIoProperties.class);
            MinIOHelper minIOHelper = new MinIOHelper();
            minIOHelper.setMinIoProperties(minIoProperties);
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minIoProperties.getEndpoint())
                    .credentials(minIoProperties.getAccessKey(), minIoProperties.getSecretKey())
                    .build();
            minIOHelper.setMinioClient(minioClient);

            // ????????????Bucket?????????????????????
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minIoProperties.getBucketName()).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minIoProperties.getBucketName())
                        .build());
            }

            File file = new File("/Users/jiangcs/Downloads/1.txt");
            MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", new FileInputStream(file));
            FileResult fileResult = minIOHelper.upload(multipartFile, "test");
            System.out.println(fileResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downloadMinIO() {
        try {
            ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");

            MinIoProperties minIoProperties = applicationContext.getBean("minIoProperties", MinIoProperties.class);
            MinIOHelper minIOHelper = new MinIOHelper();
            minIOHelper.setMinIoProperties(minIoProperties);
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minIoProperties.getEndpoint())
                    .credentials(minIoProperties.getAccessKey(), minIoProperties.getSecretKey())
                    .build();
            minIOHelper.setMinioClient(minioClient);

            minIOHelper.download("82d9e06399b149deb21ef2c0792574df.txt", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
