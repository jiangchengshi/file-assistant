package cool.doudou.celery.common.file.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * StorageModeEnum
 *
 * @author jiangcs
 * @since 2022/3/6
 */
@AllArgsConstructor
public enum StorageModeEnum {
    /**
     * 服务器本地
     */
    LOCAL("local"),
    /**
     * MongoDB：GridFS
     */
    GRID_FS("gridFs"),
    /**
     * 阿里云
     */
    ALI_YUN("aliYun"),
    /**
     * MinIO
     */
    MINIO("minIO");

    private String name;

    public static StorageModeEnum getByName(String name) {
        Optional<StorageModeEnum> optionalFirst = Arrays.stream(StorageModeEnum.values())
                .filter((e) -> e.name.equalsIgnoreCase(name))
                .findFirst();
        return optionalFirst.orElse(null);
    }
}
