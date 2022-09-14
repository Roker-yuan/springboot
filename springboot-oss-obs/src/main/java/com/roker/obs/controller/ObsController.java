package com.roker.obs.controller;


import cn.hutool.crypto.SecureUtil;
import com.obs.services.model.PutObjectResult;
import com.roker.obs.domain.FileUploadStatus;
import com.roker.obs.domain.R;
import com.roker.obs.handler.WebSocketHandler;
import com.roker.obs.util.FileUtil;
import com.roker.obs.util.HweiOBSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("huawei/obs")
public class ObsController {
    @Autowired
    private HweiOBSUtil hweiOBSUtil;

    @Autowired
    private WebSocketHandler webSocketHandler;
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 文件上传
     * @param uploadFile
     * @return
     */
    @PostMapping("fileUpload")
    public R fileUpload(MultipartFile uploadFile){
        //讲文件转换为md5,实现唯一性
        String fileString = FileUtil.MultipartFileToString(uploadFile);
        String fileMd5 = SecureUtil.md5(fileString);
        //reids是否存在
        if (redisTemplate.hasKey(fileMd5)){
            //存在返回名称
            String  fileUrl = (String) redisTemplate.opsForValue().get(fileMd5);
            return R.success(fileUrl);
        }else{
            //不存在上传并放入redis
            PutObjectResult putObjectResult = hweiOBSUtil.fileUpload(uploadFile, uploadFile.getOriginalFilename());
            if (putObjectResult!=null){
                redisTemplate.opsForValue().set(fileMd5,putObjectResult.getObjectUrl());
                return R.success(putObjectResult.getObjectKey());
            }else{
                return R.error();
            }

        }
    }

    /**
     * 查询上传进度
     * @param fileName
     */
    @GetMapping("realFilePlan")
    public void redalFilePlan(String fileName) {
        FileUploadStatus fileUploadPlan = hweiOBSUtil.getFileUploadPlan(fileName);
        webSocketHandler.sendAllMessage(fileUploadPlan.getPct());
    }

    /**
     * 下载文件
     * @param request
     * @param response
     * @param fileName
     * @return
     */
    @GetMapping("fileDownload")
    public R fileDownload(HttpServletRequest request, HttpServletResponse response, String fileName){
        int i = hweiOBSUtil.fileDownload(request, response, fileName);
        if(i==0){
            return null;
        }
        return R.error();
    }



}
