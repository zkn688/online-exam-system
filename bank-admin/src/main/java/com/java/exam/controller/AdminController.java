package com.java.exam.controller;


import com.java.exam.dto.AddUserDto;
import com.java.exam.entity.Notice;
import com.java.exam.loggin.Log;
import com.java.exam.service.LoggingService;
import com.java.exam.service.NoticeService;
import com.java.exam.service.UserRoleService;
import com.java.exam.service.UserService;
import com.java.exam.utils.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "管理员权限相关的接口")
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;

    private final UserRoleService userRoleService;

    private final NoticeService noticeService;

    private final LoggingService loggingService;

    @GetMapping("/getUser")
    @ApiOperation("分页获取用户信息")
    public HttpResult getUser(String loginName,String trueName,Integer pageNo, Integer pageSize) {
        return HttpResult.success(userService.getUser(loginName, trueName, pageNo, pageSize));
    }

    @Log("更改用户状态")
    @GetMapping("/handleUser/{type}")
    @ApiOperation("管理员操作用户: type=1(启用) 2(禁用) 3(删除) userIds(需要操作的用户id)")
    public HttpResult handleUser(@PathVariable("type") Integer type, String userIds) {
        userService.handlerUser(type, userIds);
        return HttpResult.success();
    }

    @PostMapping("/addUser")
    @ApiOperation("管理员用户新增用户")
    public HttpResult addUser(@RequestBody AddUserDto userDto) {
        userService.addUser(userDto);
        return HttpResult.success();
    }

    @GetMapping("/getRole")
    @ApiOperation("查询系统存在的所有角色信息")
    public HttpResult getRole() {
        return HttpResult.success(userRoleService.getUserRole());
    }

    @GetMapping("/getAllNotice")
    @ApiOperation("获取系统发布的所有公告")
    public HttpResult getAllNotice(String noticeContent,Integer pageNo, Integer pageSize) {
        return HttpResult.success(noticeService.getAllNotices(noticeContent, pageNo, pageSize));
    }

    @PostMapping("/publishNotice")
    @ApiOperation("发布新公告")
    public HttpResult publishNotice(@RequestBody Notice notice) {
        noticeService.publishNotice(notice);
        return HttpResult.success();
    }

    @Log("删除日志")
    @GetMapping("/deleteNotice")
    @ApiOperation("批量删除公告")
    public HttpResult deleteNotice(@RequestParam(name = "ids") String noticeIds) {
        noticeService.deleteNoticeByIds(noticeIds);
        return HttpResult.success();
    }

    @PostMapping("/updateNotice")
    @ApiOperation("更新公告")
    public HttpResult updateNotice(@RequestBody Notice notice) {
        noticeService.updateNotice(notice);
        return HttpResult.success();
    }

    @GetMapping("/getLogging")
    @ApiOperation("分页查询日志")
    public HttpResult getLogging(String content,Integer pageNo, Integer pageSize) {
        return HttpResult.success(loggingService.getAllLogging(content,pageNo,pageSize));
    }

    @GetMapping("/deleteLogging")
    @ApiOperation("批量删除公告，需超级管理员root权限，普通管理员不行")
    public HttpResult deleteLogging(String ids) {
        loggingService.deleteLoggingByIds(ids);
        return HttpResult.success();
    }

}
