package com.java.exam.controller;


import com.java.exam.dto.UpdateUserInfoDto;
import com.java.exam.service.*;
import com.java.exam.utils.HttpResult;
import com.java.exam.utils.SecurityUtils;
import com.java.exam.vo.ExamQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "三个角色公共的相关接口")
@RequestMapping(value = "/public")
public class PublicController {

    private final NoticeService noticeService;

    private final ExamService examService;

    private final QuestionBankService questionBankService;

    private final UserRoleService userRoleService;

    private final UserService userService;


    @PostMapping("/getExamInfo")
    @ApiOperation("根据信息查询考试的信息")
    public HttpResult getExamInfo(@RequestBody ExamQueryVo examQueryVo) {
        return HttpResult.success(examService.getExamPage(examQueryVo));
    }

    @GetMapping("/getExamInfoById")
    @ApiOperation("根据考试id查询考试的信息和题目列表")
    public HttpResult getExamInfoById(@RequestParam Integer examId) {
        return HttpResult.success(examService.getExamInfoById(examId));
    }

    @GetMapping("/allExamInfo")
    @ApiOperation("查询考试所有信息")
    public HttpResult allExamInfo() {
        return HttpResult.success(examService.list(null));
    }

    @GetMapping("/getBankHaveQuestionSumByType")
    @ApiOperation("获取题库中所有题目类型的数量")
    public HttpResult getBankHaveQuestionSumByType(@RequestParam(required = false) String bankName,
                                                   Integer pageNo, Integer pageSize) {
        return HttpResult.success(questionBankService.getBankHaveQuestionSumByType(bankName, pageNo, pageSize));
    }

    @GetMapping("/getQuestionByBankIdAndType")
    @ApiOperation("根据题库id和题目类型获取题目信息")
    public HttpResult getQuestionByBankIdAndType(Integer bankId, Integer type) {
        return HttpResult.success(questionBankService.getQuestionByBankIdAndType(bankId, type));
    }

    @GetMapping("/getQuestionByBank")
    @ApiOperation("根据题库获取所有的题目信息")
    public HttpResult getQuestionByBank(Integer bankId) {
        return HttpResult.success(questionBankService.getQuestionsByBankId(bankId));
    }

    @GetMapping("/getCurrentNewNotice")
    @ApiOperation("获取当前系统最新的公告")
    public HttpResult getCurrentNewNotice() {
        return HttpResult.success(noticeService.getCurrentNotice());
    }


    @GetMapping("/getMenu")
    @ApiOperation(value = "获取不同用户的权限菜单")
    public HttpResult getMenu() {
        return HttpResult.success(userRoleService.getMenuInfo(SecurityUtils.getRoleId()));
    }

    @GetMapping("/getCurrentUser")
    @ApiOperation("供给普通用户查询个人信息使用")
    public HttpResult getCurrentUser() {
        return HttpResult.success(userService.getById(SecurityUtils.getUserId()));
    }

    @PostMapping("/updateCurrentUser")
    @ApiOperation("供给用户更改个人信息")
    public HttpResult updateCurrentUser(@RequestBody UpdateUserInfoDto updateUserInfoDto) {
        return HttpResult.success(userService.updateUserInfo(updateUserInfoDto));
    }

    @GetMapping("/checkToken")
    @ApiOperation("验证用户token接口")
    public HttpResult checkToken() {
        return HttpResult.success(SecurityUtils.getLoginUser());
    }
}
