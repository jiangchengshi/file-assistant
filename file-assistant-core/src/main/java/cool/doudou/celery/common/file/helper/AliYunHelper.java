package cool.doudou.celery.common.file.helper;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import cool.doudou.celery.common.file.Constant;
import cool.doudou.celery.common.file.entity.FileResult;
import cool.doudou.celery.common.file.properties.AliYunProperties;
import cool.doudou.celery.common.file.util.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * AliYunHelper
 *
 * @author jiangcs
 * @since 2022/3/6
 */
@Slf4j
@ConditionalOnProperty(name = "file.storage-mode", havingValue = "aliYun")
public class AliYunHelper implements FileHelper {
    private AliYunProperties aliYunProperties;
    private OSSClient ossClient;

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
            String key = UUID.randomUUID().toString().replaceAll("-", "");
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.addUserMetadata("category", category);
            ossClient.putObject(aliYunProperties.getBucketName(), key, file.getInputStream(), objectMetadata);
            FileResult.ok(key, filename, file.getContentType());
        } catch (IOException e) {
            log.error("upload exception: ", e);
        }
        return null;
    }

    @Override
    public void download(String key, HttpServletResponse response) {
        download(key, Constant.CATEGORY_DEFAULT, response);
    }

    @Override
    public void download(String key, String category, HttpServletResponse response) {
        OSSObject ossObject = ossClient.getObject(aliYunProperties.getBucketName(), key);

        try {
            IoUtil.setContentDisposition4Download(response, key);
            IoUtil.write(ossObject.getObjectContent(), response.getOutputStream());
        } catch (Exception e) {
            log.error("文件下载异常: ", e);
        }
    }

    @Override
    public void preview(String key, HttpServletResponse response) {
        preview(key, Constant.CATEGORY_DEFAULT, response);
    }

    @Override
    public void preview(String key, String category, HttpServletResponse response) {
        OSSObject ossObject = ossClient.getObject(aliYunProperties.getBucketName(), key);

        try {
            IoUtil.setContentDisposition4Preview(response, key);
            IoUtil.write(ossObject.getObjectContent(), response.getOutputStream());
        } catch (Exception e) {
            log.error("文件预览异常: ", e);
        }
    }

    @Override
    public boolean delete(String key) {
        return delete(key, Constant.CATEGORY_DEFAULT);
    }

    @Override
    public boolean delete(String key, String category) {
        try {
            ossClient.deleteObject(aliYunProperties.getBucketName(), key);
        } catch (Exception e) {
            log.error("文件删除异常: ", e);
        }
        return false;
    }

    @Autowired
    public void setAliYunProperties(AliYunProperties aliYunProperties) {
        this.aliYunProperties = aliYunProperties;
    }

    @Autowired
    public void setOssClient(OSSClient ossClient) {
        this.ossClient = ossClient;
    }
}
