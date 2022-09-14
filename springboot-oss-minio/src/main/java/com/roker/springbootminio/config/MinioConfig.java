package com.roker.springbootminio.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * minio的配置类
 */
@Configuration
@ConfigurationProperties(prefix = "spring.minio")
@Data
public class MinioConfig {
    private String accessKey;
    private String secretKey;
    private String url;
    private String bucketName;
    private Integer previewExpiry;

    /**
     * 使用配置属性绑定进行参数绑定,并初始化一个minio client对象放入容器中
     * @return
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}