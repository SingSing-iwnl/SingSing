package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("admin/product")
public class FileUploadController {
        //上传文件不能写死 要动态获取路径 ----->注意文件服务器可能会更改ip地址 就不能将ip写死在代码中 所以应当放在匹配文件里:软编码
    @Value("${fileServer.url}")
    private String fileServerUrl;
    //上传文件
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) throws Exception, IOException {
        //1.读取Tracker.conf配置文件
        String configFile = this.getClass().getResource("/tracker.conf").getFile();
        String path=null;
        //判断
        if (configFile!=null){
            //2初始化
            ClientGlobal.init(configFile);
            //3.创建一个trackerClient
            TrackerClient trackerClient = new TrackerClient();
            //4.创建一个trackerServer
            TrackerServer trackerServer = trackerClient.getConnection();
            //5.创建一个stotageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,null);
            //上传
            //第一个参数表示上传文件的字节数组,第二个参数表示后缀名
            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            path = storageClient1.upload_appender_file1(file.getBytes(), extension, null);

        }
        return Result.ok(fileServerUrl+path);
    }
}
