package cool.doudou.celery.common.file.helper;

import cool.doudou.celery.common.file.Constant;
import cool.doudou.celery.common.file.entity.FileResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * MinIoHelper
 *
 * @author jiangcs
 * @since 2022/3/6
 */
@Slf4j
@ConditionalOnProperty(name = "file.storage-mode", havingValue = "minIO")
public class MinIoHelper implements FileHelper {
    @Override
    public FileResult upload(MultipartFile file) {
        return upload(file, Constant.CATEGORY_DEFAULT);
    }

    @Override
    public FileResult upload(MultipartFile file, String category) {
        return null;
    }

    @Override
    public void download(String key, HttpServletResponse response) {
        download(key, Constant.CATEGORY_DEFAULT, response);
    }

    @Override
    public void download(String key, String category, HttpServletResponse response) {

    }

    @Override
    public void preview(String key, HttpServletResponse response) {
        preview(key, Constant.CATEGORY_DEFAULT, response);
    }

    @Override
    public void preview(String key, String category, HttpServletResponse response) {

    }

    @Override
    public boolean delete(String key) {
        return delete(key, Constant.CATEGORY_DEFAULT);
    }

    @Override
    public boolean delete(String key, String category) {
        return false;
    }
}
