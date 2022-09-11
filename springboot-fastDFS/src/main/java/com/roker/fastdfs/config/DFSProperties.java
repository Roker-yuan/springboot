package com.roker.fastdfs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @作者: Roker
 * @时间: 2022/9/11 16:16
 * @Copyright: Don`t be the same,be better!
 * @Description: $描述$
 */

@Component
@ConfigurationProperties(prefix = "fdfs")
@Data
public class DFSProperties {
    private String sourcePath;
}
