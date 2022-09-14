/*
package com.roker.obs.util;

import cn.hutool.core.io.FileUtil;
import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import com.xxx.web.exception.BizErrorException;
import com.xxx.web.exception.enums.BizErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

*/
/**
 * @author xxx
 * @createTime 2021/12/6 16:31
 * @description 华为OBS工具类
 *//*

@Slf4j
@Component
@RefreshScope
public class HuaweiOBSUtil {

    private static String endPoint;
    private static String ak;
    private static String sk;
    private static String bucketName;

    @Value("${obs.endPoint}")
    public void setEndPoint(String endPoint) {
        HuaweiOBSUtil.endPoint = endPoint;
    }

    @Value("${obs.ak}")
    public void setAk(String ak) {
        HuaweiOBSUtil.ak = ak;
    }

    @Value("${obs.sk}")
    public void setSk(String sk) {
        HuaweiOBSUtil.sk = sk;
    }

    @Value("${obs.bucketName}")
    public void setBucketName(String bucketName) {
        HuaweiOBSUtil.bucketName = bucketName;
    }
    */
/**
     * 上传File类型文件
     *
     * @param file
     * @return
     *//*

    public static String uploadFile(File file) {
        return getUploadFileUrl(file);
    }

    */
/**
     * 上传MultipartFile类型文件
     *
     * @param multipartFile
     * @return
     *//*

    public static String uploadFile(MultipartFile multipartFile) {
        return getUploadFileUrl(com.xxx.util.FileUtil.MultipartFileToFile(multipartFile));
    }

    private static String getUploadFileUrl(File file) {
        if (com.xxx.util.FileUtil.checkFileNotNull(file)) {
            String fileName = FileUtil.getName(file);
            log.info("上传图片:" + fileName);
            */
/*log.info("ak:" + ak);
            log.info("sk:" + sk);
            log.info("endPoint:" + endPoint);*//*

            ObsClient obsClient = new ObsClient(ak, sk, endPoint);
            try {
                //判断桶是否存在,不存在则创建
                if (!obsClient.headBucket(bucketName)) {
                    obsClient.createBucket(bucketName);
                }
                PutObjectRequest request = new PutObjectRequest();
                request.setBucketName(bucketName);
                request.setObjectKey(fileName);
                request.setFile(file);
                request.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
                PutObjectResult result = obsClient.putObject(request);
                String url = result.getObjectUrl();
                log.info("图片路径:" + url);
                return url;
            } catch (Exception e) {
                log.error("图片上传错误:{}", e);
                throw new BizErrorException(BizErrorCodeEnum.FILE_UPLOAD_FAILURE);
            }*/
/* finally {
                删除本地临时文件
                HuaweiOBSUtil.deleteTempFile(file);
            }*//*

        }
        return null;
    }


    */
/**
     * 上传图片自定义code
     *
     * @param ak
     * @param sk
     * @param endPoint
     * @param file
     * @return
     *//*

    public static String uploadFileByCode(String ak, String sk, String endPoint, String bucket, File file) {
        //String pathname = objectName;
        try {
            String fileName = FileUtil.getName(file);
            log.info("上传图片:" + fileName);
            ObsClient obsClient = new ObsClient(ak, sk, endPoint);
            //判断桶是否存在,不存在则创建
            if (!obsClient.headBucket(bucket)) {
                obsClient.createBucket(bucket);
            }
            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(bucket);
            request.setObjectKey(fileName);
            request.setFile(file);
            request.setAcl(AccessControlList.REST_CANNED_PUBLIC_READ);
            PutObjectResult result = obsClient.putObject(request);
            String url = result.getObjectUrl();
            log.info("文件名称:"+fileName+"图片路径:" + url);
            return url;
        } catch (Exception e) {
            log.error("图片上传错误:{}", e);
        } */
/*finally {
            HuaweiOBSUtil.deleteTempFile(file);
        }*//*

        return null;
    }

    */
/**
     * 删除本地临时文件
     *
     * @param file
     *//*

    public static void deleteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }
    */
/**
     * 根据文件地址获取名称下载File类型的文件
     * @param fileUrl
     * @return
     *//*

    public static MultipartFile downloadFileByUrl(String fileUrl){
        try {
            String fileName = getFilenameByUrl(fileUrl);
            // 创建ObsClient实例
            ObsClient obsClient = new ObsClient(ak, sk, endPoint);
            ObsObject obsObject = obsClient.getObject(bucketName, fileName);
            InputStream inputStream = obsObject.getObjectContent();
            //转成MultipartFile
            MultipartFile multipartFile = InputStreamConvertMultipartFileUtil.getMultipartFile(inputStream, fileName);
            //File file = com.xxx.util.FileUtil.MultipartFileToFile(multipartFile);
            return multipartFile;

        } catch (ObsException e) {
            log.error("文件下载失败：{}", e.getMessage());
            throw new BizErrorException(BizErrorCodeEnum.GET_FILE_DOWNLOAD_URL_FAIL);
        }
    }

    */
/**
     * 批量n天删除之前的文件
     *
     * @param ak
     * @param sk
     * @param endPoint
     * @param bucket
     * @param requireHours
     *//*

    public static void batchDeleteForHoursago(String ak, String sk, String endPoint, String bucket, int requireHours) {
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        long currentTime = new Date().getTime();
        try {
            ListObjectsRequest listRequest = new ListObjectsRequest(bucket);
            listRequest.setMaxKeys(1000); // 每次至多返回1000个对象
            ObjectListing listResult;
            Date lastModified;
            long hourMillisecond = 1000 * 3600 * 1;
            // 分页查询
            do {
                List<KeyAndVersion> toDelete = new ArrayList<>();
                listResult = obsClient.listObjects(listRequest);
                for (ObsObject obsObject : listResult.getObjects()) {
                    lastModified = obsObject.getMetadata().getLastModified();
                    long diffs = (currentTime - lastModified.getTime()) / hourMillisecond; // 当前时间减去文件修改时间
                    if (diffs > requireHours
                            && (obsObject.getObjectKey().endsWith(".ts") || obsObject.getObjectKey().endsWith(".mp4"))) {
                        log.info("文件距现在{}小时，对象更改日期：{}，文件对象：{}", diffs, lastModified, obsObject.getObjectKey());
                        toDelete.add(new KeyAndVersion(obsObject.getObjectKey()));
                    }
                }
                // 设置下次列举的起始位置
                listRequest.setMarker(listResult.getNextMarker());

                //批量删除文件
                log.info("待删除的OBS对象数量：{}", toDelete.size());
                if (!CollectionUtils.isEmpty(toDelete)) {
                    DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(bucket);
                    deleteRequest.setQuiet(true); // 设置为quiet模式,只返回删除失败的对象
                    deleteRequest.setKeyAndVersions(toDelete.toArray(new KeyAndVersion[toDelete.size()]));
                    DeleteObjectsResult deleteResult = obsClient.deleteObjects(deleteRequest);
                    if (!CollectionUtils.isEmpty(deleteResult.getErrorResults())) {
                        log.error("删除失败的OBS对象数量：{}", deleteResult.getErrorResults().size());
                    }
                }
            } while (listResult.isTruncated());

        } catch (Exception e) {
            log.error("华为OBS批量删除异常", e);
        } finally {
            try {
                obsClient.close();
            } catch (IOException e) {
                log.error("华为OBS关闭客户端失败", e);
            }
        }
    }

    */
/**
     * 删除单个对象
     *
     * @param ak
     * @param sk
     * @param endPoint
     * @param bucket
     * @param objectKey
     * @return
     *//*

    public static boolean deleteFile(String ak, String sk, String endPoint, String bucket, String objectKey) {
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        DeleteObjectResult deleteObjectResult = obsClient.deleteObject(bucket, objectKey);
        boolean deleteMarker = deleteObjectResult.isDeleteMarker();
        try {
            obsClient.close();
        } catch (IOException e) {
            log.error("华为OBS关闭客户端失败", e);
        }
        return deleteMarker;
    }

    */
/**
     * 查询桶内已使用空间大小
     *
     * @param ak
     * @param sk
     * @param endPoint
     * @param bucket
     * @return 单位字节
     *//*

    public static long getBucketUseSize(String ak, String sk, String endPoint, String bucket) {
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);
        BucketStorageInfo storageInfo = obsClient.getBucketStorageInfo(bucket);
        log.info("{} 桶内对象数：{}  已使用的空间大小B:{} GB:{}", bucket, storageInfo.getObjectNumber(), storageInfo.getSize(), storageInfo.getSize() / 1024 / 1024 / 1024);
        try {
            obsClient.close();
        } catch (IOException e) {
            log.error("华为OBS关闭客户端失败", e);
        }
        return storageInfo.getSize();
    }


    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    */
/**
     * 根据下载地址url获取文件名称
     * @param url
     * @throws IOException
     *//*

    public static String getFilenameByUrl(String url){
        String fileName= null;
        try {
            //url编码处理，中文名称会变成百分号编码
            String decode = URLDecoder.decode(url, "utf-8");
            fileName = decode.substring(decode.lastIndexOf("/")+1);
            log.info("fileName :" + fileName);
        } catch (UnsupportedEncodingException e) {
            log.error("getFilenameByUrl() called with exception => 【url = {}】", url,e);
            e.printStackTrace();
        }
        return fileName;
    }
}

*/
