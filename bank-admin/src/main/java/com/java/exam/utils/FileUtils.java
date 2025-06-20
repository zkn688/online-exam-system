package com.java.exam.utils;


import com.java.exam.exception.BaseException;
import com.java.exam.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

public class FileUtils {

    public static String uploadImg(String filepath, MultipartFile file) {
        if (file.isEmpty()) {
            throw new BaseException(ErrorCode.E_500011);
        }
        File folder = new File(filepath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String name = file.getOriginalFilename();
        String filename = "img-"+ UUID.randomUUID() + "-" + System.currentTimeMillis()+ name.substring(name.lastIndexOf("."));;
        try {
            file.transferTo(new File(filepath + filename));
        } catch (IOException e) {
            throw new BaseException(ErrorCode.E_500012);
        }
        return filename;
    }

    public static void getImg(String filepath, HttpServletResponse response) {

        try {
            // 取得输出流
            OutputStream os = response.getOutputStream();
            response.setContentType("image/png");
            //读取流
            File f = new File(filepath);
            if (!f.isFile()) {
                throw new BaseException(ErrorCode.E_500016);
            }
            BasicFileAttributes basicFileAttributes = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
            if (!basicFileAttributes.isRegularFile()) {
                throw new BaseException(ErrorCode.E_500016);
            }
            FileInputStream fis;
            fis = new FileInputStream(f);

            long size = basicFileAttributes.size();
            byte[] data = new byte[(int) size];
            fis.read(data, 0, (int) size);
            fis.close();
            response.setContentType("image/png");
            OutputStream out = response.getOutputStream();
            out.write(data);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new BaseException(ErrorCode.E_500017);
        }
    }

}

