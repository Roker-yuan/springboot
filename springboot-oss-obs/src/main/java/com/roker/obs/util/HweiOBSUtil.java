package com.roker.obs.util;

import com.obs.services.ObsClient;
import com.obs.services.model.*;

import com.roker.obs.config.HweiOBSConfig;
import com.roker.obs.domain.FileUploadStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class HweiOBSUtil {
    @Autowired
    private HweiOBSConfig hweiOBSConfig;


    /**
     * 文件上传
     * @param uploadFile 上传的文件
     * @param FileName 文件名称
     * @return 返回的路径
     */
    public PutObjectResult fileUpload(MultipartFile uploadFile, String FileName){
        ObsClient obsClient=null;
        try {
            //创建实例
            obsClient = hweiOBSConfig.getInstance();
            //获取文件信息
            InputStream inputStream = uploadFile.getInputStream();
            UploadFileRequest request1 = new UploadFileRequest(hweiOBSConfig.getBucketName(), FileName);
            long available = inputStream.available();
            PutObjectRequest request = new PutObjectRequest(hweiOBSConfig.getBucketName(), java.lang.String.valueOf(FileName), inputStream);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(available);
            request.setMetadata(objectMetadata);
            //设置公共读
            request.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
            PutObjectResult putObjectResult = obsClient.putObject(request);
            return putObjectResult;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //销毁实例
            hweiOBSConfig.destroy(obsClient);
        }
        return null;
    }
    /**
     * 获取文件上传进度
     * @param objectName
     * @return
     */
    public FileUploadStatus getFileUploadPlan(String objectName){
        ObsClient obsClient=null;
        FileUploadStatus fileUploadStatus = new FileUploadStatus();
        try {
            obsClient=hweiOBSConfig.getInstance();
            GetObjectRequest request = new GetObjectRequest(hweiOBSConfig.getBucketName(), objectName);
            request.setProgressListener(new ProgressListener() {
                @Override
                public void progressChanged(ProgressStatus status) {
                    //上传的平均速度
                    fileUploadStatus.setAvgSpeed(status.getAverageSpeed());
                    //上传的百分比
                    fileUploadStatus.setPct(String.valueOf(status.getTransferPercentage()));
                }
            });
            // 每下载1MB数据反馈下载进度
            request.setProgressInterval(1024*1024L);
            ObsObject obsObject = obsClient.getObject(request);
            // 读取对象内容
            InputStream input= obsObject.getObjectContent();
            byte[] b = new byte[1024];
            ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            int len;
            while ((len=input.read(b))!=-1 ){
                //将每一次的数据写入缓冲区
                byteArrayOutputStream.write(b,0,len);
            }
            byteArrayOutputStream.close();
            input.close();
            return fileUploadStatus;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            hweiOBSConfig.destroy(obsClient);
        }
        return null;
    }

    /**
     * 文件下载
     * @param request
     * @param response
     * @param fileName 文件名称
     * @return
     */
    public int fileDownload(HttpServletRequest request, HttpServletResponse response, String fileName){
        try {
            ObsClient obsClient = hweiOBSConfig.getInstance();
            ObsObject obsObject = obsClient.getObject(hweiOBSConfig.getBucketName(), fileName);
            InputStream input = obsObject.getObjectContent();
            //缓冲文件输出流
            BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            //设置让浏览器弹出下载提示框，而不是直接在浏览器中打开
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            IOUtils.copy(input,outputStream);
            outputStream.flush();
            outputStream.close();
            input.close();
            return 0;
        }catch (Exception e){
            log.error("文件下载失败：{}",e.getMessage());
            return 1;
        }

    }
}
