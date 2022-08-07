package cool.doudou.file.assistant.core.helper;

import cool.doudou.file.assistant.core.Constant;
import cool.doudou.file.assistant.core.entity.FileResult;
import cool.doudou.file.assistant.core.properties.LocalProperties;
import cool.doudou.file.assistant.core.util.ComUtil;
import cool.doudou.file.assistant.core.util.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * 本地
 *
 * @author jiangcs
 * @since 2022/2/22
 */
@Slf4j
public class LocalHelper implements FileHelper {
    private LocalProperties localProperties;

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

            Path parentPath = Paths.get(localProperties.getPath(), category);
            if (Files.notExists(parentPath)) {
                Files.createDirectories(parentPath);
            }
            Path path = Files.createFile(parentPath.resolve(key));

            // 复制至 服务器本地
            Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return FileResult.ok(key, filename, multipartFile.getContentType());
        } catch (Exception e) {
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
            Path path = this.check(key, category);
            Resource resource = new UrlResource(path.toUri());

            IoUtil.setContentDisposition4Download(response, key);
            IoUtil.write(resource.getInputStream(), response.getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException("文件下载异常", e);
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
            throw new RuntimeException("文件预览异常", e);
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
            throw new RuntimeException("文件删除异常", e);
        }
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
