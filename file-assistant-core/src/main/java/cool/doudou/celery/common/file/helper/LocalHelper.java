package cool.doudou.celery.common.file.helper;

import cool.doudou.celery.common.file.Constant;
import cool.doudou.celery.common.file.entity.FileResult;
import cool.doudou.celery.common.file.properties.LocalProperties;
import cool.doudou.celery.common.file.util.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * LocalHelper
 *
 * @author jiangcs
 * @since 2022/2/22
 */
@Slf4j
public class LocalHelper implements FileHelper {
    private LocalProperties localProperties;

    @Override
    public FileResult upload(MultipartFile file) {
        return upload(file, Constant.CATEGORY_DEFAULT);
    }

    @Override
    public FileResult upload(MultipartFile file, String category) {
        try {
            Path parentPath = Paths.get(localProperties.getPath(), category);
            if (Files.notExists(parentPath)) {
                Files.createDirectories(parentPath);
            }

            String filename = file.getOriginalFilename();
            if (filename == null) {
                throw new RuntimeException("文件名称异常");
            }

            String key = UUID.randomUUID().toString().replaceAll("-", "");
            Path path = Files.createFile(parentPath.resolve(key));

            // 复制至 服务器本地
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return FileResult.ok(key, filename, file.getContentType());
        } catch (Exception e) {
            log.error("文件上传异常: ", e);
        }
        return null;
    }

    @Override
    public void download(String key, HttpServletResponse response) {
        download(key, Constant.CATEGORY_DEFAULT, response);
    }

    @Override
    public void download(String key, String category, HttpServletResponse response) {
        try {
            Path path = this.check(key, category);
            Resource resource = new UrlResource(path.toUri());

            IoUtil.setContentDisposition4Download(response, key);
            IoUtil.write(resource.getInputStream(), response.getOutputStream());
        } catch (Exception e) {
            log.error("文件下载异常: {}", e.getMessage());
        }
    }

    @Override
    public void preview(String key, HttpServletResponse response) {
        preview(key, Constant.CATEGORY_DEFAULT, response);
    }

    @Override
    public void preview(String key, String category, HttpServletResponse response) {
        try {
            Path path = this.check(key, category);
            Resource resource = new UrlResource(path.toUri());

            IoUtil.setContentDisposition4Preview(response, key);
            IoUtil.write(resource.getInputStream(), response.getOutputStream());
        } catch (Exception e) {
            log.error("文件预览异常: {}", e.getMessage());
        }
    }

    @Override
    public boolean delete(String key) {
        return delete(key, Constant.CATEGORY_DEFAULT);
    }

    @Override
    public boolean delete(String key, String category) {
        try {
            Path path = this.check(key, category);

            return Files.deleteIfExists(path);
        } catch (Exception e) {
            log.error("文件删除异常: {}", e.getMessage());
        }
        return false;
    }

    /**
     * 检查文件是否存在
     *
     * @param key      文件键值
     * @param category 分类
     * @return Path
     * @throws Exception 异常
     */
    public Path check(String key, String category) throws Exception {
        Path parentPath = Paths.get(localProperties.getPath(), category);
        if (Files.notExists(parentPath)) {
            throw new RuntimeException("父目录不存在");
        }

        Path path = parentPath.resolve(key);
        if (Files.notExists(path)) {
            throw new RuntimeException("文件不存在");
        }

        return path;
    }

    @Autowired
    public void setLocalProperties(LocalProperties localProperties) {
        this.localProperties = localProperties;
    }
}
