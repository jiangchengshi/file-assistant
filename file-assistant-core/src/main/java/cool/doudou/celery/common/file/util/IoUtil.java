package cool.doudou.celery.common.file.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * IoUtil
 *
 * @author jiangcs
 * @since 2022/3/6
 */
public class IoUtil {
    /**
     * 设置请求响应下载文件 Content-Disposition
     *
     * @param response 请求响应
     * @param filename 文件名
     */
    public static void setContentDisposition4Download(HttpServletResponse response, String filename) {
        response.setHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode(filename, StandardCharsets.UTF_8));
    }

    /**
     * 设置请求响应预览文件 Content-Disposition
     *
     * @param response 请求响应
     * @param filename 文件名
     */
    public static void setContentDisposition4Preview(HttpServletResponse response, String filename) {
        response.setHeader("Content-Disposition", "filename=" + filename);
    }

    /**
     * 从输入流 写入 输出流
     *
     * @param is 输入流
     * @param os 输出流
     * @throws IOException IO异常
     */
    public static void write(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while (-1 != (length = is.read(buffer))) {
            os.write(buffer, 0, length);
        }
    }
}
