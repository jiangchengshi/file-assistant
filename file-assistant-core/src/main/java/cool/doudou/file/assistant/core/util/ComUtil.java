package cool.doudou.file.assistant.core.util;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
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

    /**
     * File 2 MultipartFile
     *
     * @param file 文件
     * @return 上传格式文件
     * @throws Exception 异常
     */
    public static MultipartFile file2MultipartFile(File file) throws Exception {
        return new MockMultipartFile(file.getName(), new FileInputStream(file));
    }
}
