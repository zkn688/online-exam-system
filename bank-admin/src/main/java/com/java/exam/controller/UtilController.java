package com.java.exam.controller;


import com.java.exam.utils.CodeUtil;
import com.java.exam.utils.FileUtils;
import com.java.exam.utils.HttpResult;
import com.java.exam.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "工具类接口")
@RequestMapping("/util")
@RequiredArgsConstructor
public class UtilController {

    private final RedisUtil redisUtil;


    @Value("${file.imgPath}")
    private String imgPath;

    @GetMapping("/getCodeImg")
    @ApiOperation(value = "获取验证码图片流")
    public void getIdentifyImage(@RequestParam("id") String id, HttpServletResponse response) throws IOException {
        //设置不缓存图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);
        //指定生成的响应图片
        response.setContentType("image/jpeg");
        CodeUtil code = new CodeUtil();
        BufferedImage image = code.getIdentifyImg();
        code.getG().dispose();
        //将图形验证码IO流传输至前端
        ImageIO.write(image, "JPEG", response.getOutputStream());
        redisUtil.set(id, code.getCode(),5, TimeUnit.MINUTES);
    }

    @GetMapping("/getCode")
    @ApiOperation(value = "获取验证码")
    public HttpResult getCode(@RequestParam("id") String id) {
        return HttpResult.success(redisUtil.get(id).toString());
    }

    @GetMapping("/getImg/{url}")
    @ApiOperation(value = "获取图片")
    public void getCode(@PathVariable("url") String url, HttpServletResponse response) {
        FileUtils.getImg(imgPath+url,response);
    }
}
