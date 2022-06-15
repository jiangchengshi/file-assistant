package cool.doudou.file.assistant.core.util;

import java.util.UUID;

/**
 * ComUtil
 *
 * @author jiangcs
 * @since 2022/6/15
 */
public class ComUtil {
    /**
     * 获取 文件存储Key
     *
     * @param filename  文件名
     * @param hasSuffix 是否包含后缀名
     * @return String
     */
    public static String getFileKey(String filename, boolean hasSuffix) {
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        if (hasSuffix) {
            key += filename.substring(filename.lastIndexOf("."));
        }
        return key;
    }
}
