package com.java.exam.controller;


import com.java.exam.dto.LoginDto;
import com.java.exam.dto.RegisterDto;
import com.java.exam.service.UserService;
import com.java.exam.utils.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/common")
@Api(tags = "(学生,教师,管理员)通用相关接口")
public class CommonController {

    private final UserService userService;

    @ApiOperation("用户名合法查询接口")
    @GetMapping("/check/{username}")
    public HttpResult checkUsername(@PathVariable(value = "username") String username) {
        return HttpResult.success(userService.checkUsername(username));
    }

    @PostMapping("/login")
    @ApiOperation("用户登录接口")
    public HttpResult login(@RequestBody LoginDto loginDto) {
        return HttpResult.success(userService.login(loginDto));
    }

    @ApiOperation("用户注册接口")
    @PostMapping("/register")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "系统用户实体", required = true, dataType = "user", paramType = "body")
    })
    public HttpResult Register(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return HttpResult.success();
    }


}
