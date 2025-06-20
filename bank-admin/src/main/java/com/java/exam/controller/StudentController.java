package com.java.exam.controller;

import com.java.exam.entity.ExamRecord;
import com.java.exam.service.ExamQuestionService;
import com.java.exam.service.ExamRecordService;
import com.java.exam.service.QuestionService;
import com.java.exam.utils.HttpResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Api(tags = "学生权限相关的接口")
@RequestMapping(value = "/student")
public class StudentController {

    private final ExamRecordService examRecordService;

    private final QuestionService questionService;

    private final ExamQuestionService examQuestionService;

    @GetMapping("/getMyGrade")
    @ApiOperation("获取个人成绩分页)")
    public HttpResult getMyGrade(String username, Integer pageNo, Integer pageSize, @RequestParam(required = false) Integer examId) {
        return HttpResult.success(examRecordService.getUserGrade(username, examId, pageNo, pageSize));
    }

    @PostMapping("/addExamRecord")
    @ApiOperation("保存考试记录信息,返回保存记录的id")
    public HttpResult addExamRecord(@RequestBody ExamRecord examRecord, HttpServletRequest request) {
        return HttpResult.success(examRecordService.addExamRecord(examRecord, request));
    }

    @GetMapping("/getQuestionById/{id}")
    @ApiOperation("根据id获取题目信息")
    public HttpResult getQuestionById(@PathVariable("id") Integer id) {
        return HttpResult.success(questionService.getQuestionVoById(id));
    }

    @GetMapping("/getQuestionByIds")
    @ApiOperation("根据id集合获取题目信息")
    public HttpResult getQuestionById(@RequestParam("ids") List<Integer> ids) {
        return HttpResult.success(questionService.getQuestionVoByIds(ids));
    }

    @GetMapping("/getExamRecordById/{recordId}")
    @ApiOperation("根据考试的记录id查询用户考试的信息")
    public HttpResult getExamRecordById(@PathVariable Integer recordId) {
        return HttpResult.success(examRecordService.getExamRecordById(recordId));
    }

    @GetMapping("/getExamQuestionByExamId/{examId}")
    @ApiOperation("根据考试id查询考试中的每一道题目id和分值")
    public HttpResult getExamQuestionByExamId(@PathVariable Integer examId) {
        return HttpResult.success(examQuestionService.getExamQuestionByExamId(examId));
    }

}
