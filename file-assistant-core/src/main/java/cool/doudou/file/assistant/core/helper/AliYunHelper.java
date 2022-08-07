package cool.doudou.file.assistant.core.helper;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import cool.doudou.file.assistant.core.Constant;
import cool.doudou.file.assistant.core.entity.FileResult;
import cool.doudou.file.assistant.core.properties.AliYunProperties;
import cool.doudou.file.assistant.core.util.ComUtil;
import cool.doudou.file.assistant.core.util.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * 阿里云OSS
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
    public FileResult upload(MultipartFile multipartFile) {
        return upload(multipartFile, Constant.CATEGORY_DEFAULT);
    }

    @Override
    public FileResult upload(File file) {
        return upload(file, Constant.CATEGORY_DEFAULT);
    }

    @Override
    public FileResult upload(MultipartFile multipartFile, String category) {
        if (multipartFile == null) {
            throw new RuntimeException("文件异常");
        }
        String filename = multipartFile.getOriginalFilename();
        if (filename == null) {
            throw new RuntimeException("文件名称异常");
        }

        try {
            String key = ComUtil.getFileKey(filename, true);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.addUserMetadata("category", category);
            ossClient.putObject(aliYunProperties.getBucketName(), key, multipartFile.getInputStream(), objectMetadata);

            return FileResult.ok(key, filename, multipartFile.getContentType());
        } catch (IOException e) {
            throw new RuntimeException("文件上传异常 ", e);
        }
    }

    @Override
    public FileResult upload(File file, String category) {
        MultipartFile multipartFile;
        try {
            multipartFile = ComUtil.file2MultipartFile(file);
        } catch (Exception e) {
            throw new RuntimeException("文件转换异常");
        }

        return upload(multipartFile, category);
    }

    @Override
    public void download(String key, HttpServletResponse response) {
        download(key, Constant.CATEGORY_DEFAULT, response);
    }

    @Override
    public void download(String key, String category, HttpServletResponse response) {
        try {
            OSSObject ossObject = ossClient.getObject(aliYunProperties.getBucketName(), key);

            IoUtil.setContentDisposition4Download(response, key);
            IoUtil.write(ossObject.getObjectContent(), response.getOutputStream());
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
            OSSObject ossObject = ossClient.getObject(aliYunProperties.getBucketName(), key);

            IoUtil.setContentDisposition4Preview(response, key);
            IoUtil.write(ossObject.getObjectContent(), response.getOutputStream());
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
            ossClient.deleteObject(aliYunProperties.getBucketName(), key);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("文件删除异常 ", e);
        }
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
