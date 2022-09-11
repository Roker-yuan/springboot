package com.roker.fastdfs.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.roker.fastdfs.config.NonStaticResourceHttpRequestHandler;
import com.roker.fastdfs.utils.FastDFSUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@AllArgsConstructor
public class FastDFSController {
    private final NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;


    @Autowired
    private FastDFSUtil fastDFSUtil;

    @RequestMapping(value = "/uploadFiles",headers="content-type=multipart/form-data", method = RequestMethod.POST)

    public StorePath uploadFile(@RequestPart("file") MultipartFile file){
        StorePath upload = null;
        try {
            upload = fastDFSUtil.upload(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (upload == null) {
            System.out.println("上传失败！");
        }
        return upload;

    }

    /**
     * 文件删除
     */
    @RequestMapping(value = "/downloadByPath", method = RequestMethod.GET)
    public ResponseEntity<String> downloadByPath (@RequestParam("path")String path, HttpServletResponse response){

        try {
            fastDFSUtil.download(path,null,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("SUCCESS") ;
    }


    @GetMapping("/video")
    public void videoPreview(@RequestParam("path")String path,HttpServletRequest request, HttpServletResponse response) throws Exception {

        //假如我把视频1.mp4放在了static下的video文件夹里面
        //sourcePath 是获取resources文件夹的绝对地址
        //realPath 即是视频所在的磁盘地址
//        String sourcePath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
        String sourcePath = "http://39.105.212.1:9999/";
        String realPath = sourcePath +path;


        Path filePath = Paths.get(realPath );
        if (Files.exists(filePath)) {
            String mimeType = Files.probeContentType(filePath);
            if (!StringUtils.isEmpty(mimeType)) {
                response.setContentType(mimeType);
            }
            request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
            nonStaticResourceHttpRequestHandler.handleRequest(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        }
    }
}
