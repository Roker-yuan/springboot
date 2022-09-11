package com.roker.springbootminio.controller;

import com.roker.springbootminio.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
public class MinioController {
    @Autowired
    private MinioUtil minioUtil;
    @Value("${spring.minio.url}")
    private String address;
    @Value("${spring.minio.bucket-name}")
    private String bucketName;

    @PostMapping("/upload")
    public Object upload(MultipartFile file) {
       
        List<String> upload = minioUtil.upload(new MultipartFile[]{file});

        return address+"/"+bucketName+"/"+upload.get(0);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> upload(@RequestParam("fileName") String fileName) {
        return  minioUtil.download(fileName);
    }

    @GetMapping("/preview")
    public String preview(@RequestParam("fileName") String fileName) {
        return  minioUtil.getPreviewUrl(fileName,null);
    }



}
