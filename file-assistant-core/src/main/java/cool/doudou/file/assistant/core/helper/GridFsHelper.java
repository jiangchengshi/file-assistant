package cool.doudou.file.assistant.core.helper;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import cool.doudou.file.assistant.core.Constant;
import cool.doudou.file.assistant.core.entity.FileResult;
import cool.doudou.file.assistant.core.util.ComUtil;
import cool.doudou.file.assistant.core.util.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * GridFs
 *
 * @author jiangcs
 * @since 2022/2/16
 */
@Slf4j
@ConditionalOnProperty(name = "file.storage-mode", havingValue = "gridFs")
public class GridFsHelper implements FileHelper {
    private GridFSBucket gridFsBucket;

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
            throw new IllegalArgumentException("文件名字为空");
        }

        try {
            // 存储至 gridFs
            ObjectId objectId = gridFsBucket.uploadFromStream(
                    filename,
                    multipartFile.getInputStream(),
                    new GridFSUploadOptions().metadata(new Document("category", category))
            );
            return FileResult.ok(objectId.toString(), filename, multipartFile.getContentType());
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
            this.check(key, category);

            IoUtil.setContentDisposition4Download(response, key);
            gridFsBucket.downloadToStream(new ObjectId(key), response.getOutputStream());
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
            this.check(key, category);

            IoUtil.setContentDisposition4Preview(response, key);
            gridFsBucket.downloadToStream(new ObjectId(key), response.getOutputStream());
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
            this.check(key, category);

            gridFsBucket.delete(new ObjectId(key));
            return true;
        } catch (Exception e) {
            throw new RuntimeException("文件删除异常 ", e);
        }
    }

    /**
     * 检查文件是否存在
     *
     * @param key      文件键值
     * @param category 分类
     * @throws Exception 异常
     */
    public void check(String key, String category) throws Exception {
        GridFSFindIterable gridFsIterable = gridFsBucket.find(
                new BasicDBObject("_id", new ObjectId(key))
                        .append("metadata.category", category)
        );
        GridFSFile gridFsFile = gridFsIterable.first();
        if (gridFsFile == null) {
            throw new FileNotFoundException("文件不存在：" + key);
        }
    }

    @Autowired
    public void setGridFsBucket(GridFSBucket gridFsBucket) {
        this.gridFsBucket = gridFsBucket;
    }
}