package cool.doudou.file.assistant.core.helper;

import cool.doudou.file.assistant.core.Constant;
import cool.doudou.file.assistant.core.entity.FileResult;
import cool.doudou.file.assistant.core.properties.MinIoProperties;
import cool.doudou.file.assistant.core.util.ComUtil;
import cool.doudou.file.assistant.core.util.IoUtil;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * MinIOHelper
 *
 * @author jiangcs
 * @since 2022/3/6
 */
@Slf4j
@ConditionalOnProperty(name = "file.storage-mode", havingValue = "minIO")
public class MinIOHelper implements FileHelper {
    private MinIoProperties minIoProperties;
    private MinioClient minioClient;

    @Override
    public FileResult upload(MultipartFile file) {
        return upload(file, Constant.CATEGORY_DEFAULT);
    }

    @Override
    public FileResult upload(MultipartFile file, String category) {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new RuntimeException("文件名称异常");
        }

        try {
            String key = ComUtil.getFileKey(filename, true);
            Map<String, String> userMetadata = new HashMap<>(1);
            userMetadata.put("category", category);
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(minIoProperties.getBucketName())
                    .object(key)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .userMetadata(userMetadata)
                    .build();
            // 文件名称相同会覆盖
            minioClient.putObject(objectArgs);

            return FileResult.ok(key, filename, file.getContentType());
        } catch (Exception e) {
            throw new RuntimeException("文件上传异常 ", e);
        }
    }

    @Override
    public void download(String key, HttpServletResponse response) {
        download(key, Constant.CATEGORY_DEFAULT, response);
    }

    @Override
    public void download(String key, String category, HttpServletResponse response) {
        try {
            GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(minIoProperties.getBucketName())
                    .object(key).build();
            GetObjectResponse getObjectResponse = minioClient.getObject(objectArgs);

            IoUtil.setContentDisposition4Download(response, key);
            IoUtil.write(new ByteArrayInputStream(getObjectResponse.readAllBytes()), response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("文件下载异常 ", e);
        }
    }

    @Override
    public void preview(String key, HttpServletResponse response) {
        preview(key, Constant.CATEGORY_DEFAULT, response);
    }

    @Override
    public void preview(String key, String category, HttpServletResponse response) {
        try {
            GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(minIoProperties.getBucketName())
                    .object(key).build();
            GetObjectResponse getObjectResponse = minioClient.getObject(objectArgs);

            IoUtil.setContentDisposition4Preview(response, key);
            IoUtil.write(new ByteArrayInputStream(getObjectResponse.readAllBytes()), response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("文件预览异常 ", e);
        }
    }

    @Override
    public boolean delete(String key) {
        return delete(key, Constant.CATEGORY_DEFAULT);
    }

    @Override
    public boolean delete(String key, String category) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minIoProperties.getBucketName()).object(key).build());
            return true;
        } catch (Exception e) {
            throw new RuntimeException("文件删除异常 ", e);
        }
    }

    @Autowired
    public void setMinIoProperties(MinIoProperties minIoProperties) {
        this.minIoProperties = minIoProperties;
    }

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }
}
