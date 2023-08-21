package com.wzt.reggie.controller;


import com.wzt.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 图片的上传与下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    //  细节的，要和前端发送的请求文件名一致，都是file
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
//        file是个临时文件，会自动清理掉，需要转存

//        log.info("file:{}",file.toString());

//        转存文件，存在问题，没有新建菜品，但是上传文件依旧会转存至缓存
//        XXXXXX.jpg,但是存在问题，原始的文件名可能重复，需要随机生成文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//      32位的随机字符
        String filename = UUID.randomUUID().toString() + suffix;

//      细节：创建文件夹，如果目录不存在，需要创建目录
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdir();
        }


        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(filename);
    }

    /**
     * 将请求的文件，从本地传输给浏览器，即回显上传的图片以提升用户的体验
     *
     * @param name
     * @param response
     */
    @GetMapping("download")
    public void download(String name, HttpServletResponse response) {

        try {
//         将文件的内容读入，输入流
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            ServletOutputStream outputStream = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
