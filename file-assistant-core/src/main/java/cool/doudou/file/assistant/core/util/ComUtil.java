package cool.doudou.file.assistant.core.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
     */
    public static MultipartFile file2MultipartFile(File file) {
        return new CommonsMultipartFile(createFileItem(file));
    }

    private static FileItem createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(file.getName(), "text/plain", true, file.getName());
        int bytesRead;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }
}
