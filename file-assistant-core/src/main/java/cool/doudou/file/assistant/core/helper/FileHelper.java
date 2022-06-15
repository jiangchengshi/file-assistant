package cool.doudou.file.assistant.core.helper;

import cool.doudou.file.assistant.core.entity.FileResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * FileHelper
 *
 * @author jiangcs
 * @since 2022/2/16
 */
public interface FileHelper {
    /**
     * 上传
     *
     * @param file 文件
     * @return FileResult
     */
    FileResult upload(MultipartFile file);

    /**
     * 上传
     *
     * @param file     文件
     * @param category 类别
     * @return FileResult
     */
    FileResult upload(MultipartFile file, String category);

    /**
     * 下载
     *
     * @param key      键值
     * @param response 请求响应
     */
    void download(String key, HttpServletResponse response);

    /**
     * 下载
     *
     * @param key      键值
     * @param category 类别
     * @param response 请求响应
     */
    void download(String key, String category, HttpServletResponse response);

    /**
     * 预览
     *
     * @param key      键值
     * @param response 请求响应
     */
    void preview(String key, HttpServletResponse response);

    /**
     * 预览
     *
     * @param key      键值
     * @param category 类别
     * @param response 请求响应
     */
    void preview(String key, String category, HttpServletResponse response);

    /**
     * 删除
     *
     * @param key 键值
     * @return true-成功；false-失败
     */
    boolean delete(String key);

    /**
     * 删除
     *
     * @param key      键值
     * @param category 类别
     * @return true-成功；false-失败
     */
    boolean delete(String key, String category);
}