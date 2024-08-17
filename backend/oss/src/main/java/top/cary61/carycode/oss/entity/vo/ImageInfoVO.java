package top.cary61.carycode.oss.entity.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageInfoVO {

    // 访问图片所使用的路径
    private String path;
}
