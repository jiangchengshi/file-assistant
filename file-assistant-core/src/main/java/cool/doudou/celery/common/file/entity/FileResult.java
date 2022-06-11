package cool.doudou.celery.common.file.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * FileResult
 *
 * @author jiangcs
 * @since 2022/2/16
 */
@Accessors(chain = true)
@Data
public class FileResult {
    /**
     * 文件键值
     */
    private String key;
    /**
     * 文件名称
     */
    private String filename;
    /**
     * 文件内容类型
     */
    private String contentType;

    public static FileResult ok(String key, String filename, String contentType) {
        return new FileResult()
                .setKey(key)
                .setFilename(filename)
                .setContentType(contentType);
    }
}
