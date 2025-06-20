package com.java.exam.controller;


import com.java.exam.entity.QuestionBank;
import com.java.exam.loggin.Log;
import com.java.exam.service.*;
import com.java.exam.utils.FileUtils;
import com.java.exam.utils.HttpResult;
import com.java.exam.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "老师权限相关的接口")
@RequestMapping(value = "/teacher")
public class TeacherController {


    private final ExamService examService;

    private final UserService userService;

    private final QuestionService questionService;

    private final ExamRecordService examRecordService;

    private final QuestionBankService questionBankService;


    @Value("${file.imgPath}")
    private String imgPath;

    @GetMapping("/getQuestionBank")
    @ApiOperation("获取所有题库信息")
    public HttpResult getQuestionBank() {
        return HttpResult.success(questionBankService.getAllQuestionBanks());
    }

    @GetMapping("/getQuestion")
    @ApiOperation("分页获取题目信息")
    public HttpResult getQuestion(QuestionQueryVo queryVo) {
        return HttpResult.success(questionService.getQuestion(queryVo));
    }

    @Log("删除试题")
    @GetMapping("/deleteQuestion")
    @ApiOperation("根据id批量删除")
    public HttpResult deleteQuestion(String questionIds) {
        questionService.deleteQuestionByIds(questionIds);
        return HttpResult.success();
    }

    @GetMapping("/addBankQuestion")
    @ApiOperation("将问题加入题库")
    public HttpResult addBankQuestion(String questionIds, String banks) {
        questionBankService.addQuestionToBank(questionIds, banks);
        return HttpResult.success();
    }

    @GetMapping("/removeBankQuestion")
    @ApiOperation("将问题从题库移除")
    public HttpResult removeBankQuestion(String questionIds, String banks) {
        questionBankService.removeBankQuestion(questionIds, banks);
        return HttpResult.success();
    }

    @PostMapping("/addQuestion")
    @ApiOperation("添加试题")
    public HttpResult addQuestion(@RequestBody QuestionVo questionVo) {
        questionService.addQuestion(questionVo);
        return HttpResult.success();
    }

    @PostMapping("/importQuestion")
    @ApiOperation("批量导入试题")
    public HttpResult addQuestion(@RequestBody QuestionImportVo questionImportVo) {
        questionService.importQuestion(questionImportVo);
        return HttpResult.success();
    }

    @PostMapping("/updateQuestion")
    @ApiOperation("更新试题")
    public HttpResult updateQuestion(@RequestBody QuestionVo questionVo) {
        questionService.updateQuestion(questionVo);
        return HttpResult.success();
    }

    @Log("删除题库")
    @GetMapping("/deleteQuestionBank")
    @ApiOperation("删除题库并去除所有题目中的包含此题库的信息")
    public HttpResult deleteQuestionBank(String ids) {
        questionBankService.deleteQuestionBank(ids);
        return HttpResult.success();
    }

    @PostMapping("/addQuestionBank")
    @ApiOperation("添加题库信息")
    public HttpResult addQuestionBank(@RequestBody QuestionBank questionBank) {
        questionBankService.addQuestionBank(questionBank);
        return HttpResult.success();
    }


    @Log("更改考试状态")
    @GetMapping("/operationExam/{type}")
    @ApiOperation("操作考试的信息表(type 1启用 2禁用 3删除)")
    public HttpResult operationExam(@PathVariable("type") Integer type, String ids) {
        examService.operationExam(type, ids);
        return HttpResult.success();
    }

    @PostMapping("/addExamByBank")
    @ApiOperation("智能添加考试")
    public HttpResult addExamByBank(@RequestBody AddSmartExamVo addSmartExamVo) {
        examService.addExamBySmart(addSmartExamVo);
        return HttpResult.success();
    }

    @PostMapping("/addExamByQuestionList")
    @ApiOperation("根据题目列表添加考试")
    public HttpResult addExamByQuestionList(@RequestBody AddExamByQuestionVo addExamByQuestionVo) {
        examService.addExamByQuestionList(addExamByQuestionVo);
        return HttpResult.success();
    }

    @PostMapping("/updateExamInfo")
    @ApiOperation("更新考试的信息")
    public HttpResult updateExamInfo(@RequestBody AddExamByQuestionVo addExamByQuestionVo) {
        examService.updateExamInfo(addExamByQuestionVo);
        return HttpResult.success();
    }

    @GetMapping("/getExamRecord")
    @ApiOperation("获取考试记录信息")
    public HttpResult getExamRecord(RecordVo recordVo) {
        return HttpResult.success(examRecordService.getExamRecord(recordVo));
    }

    @GetMapping("/getUserById/{userId}")
    @ApiOperation("根据用户id查询用户信息")
    public HttpResult getUserById(@PathVariable Integer userId) {
        return HttpResult.success(userService.getById(userId));
    }

    @GetMapping("/getUserByIds")
    @ApiOperation("根据用户ids查询用户信息")
    public HttpResult getUserByIds(@RequestParam("userIds") List<Integer> userIds) {
        return HttpResult.success(userService.getUserInfoByIds(userIds));
    }

    @GetMapping("/setObjectQuestionScore")
    @ApiOperation("设置考试记录的客观题得分,设置总分为逻辑得分+客观题")
    public HttpResult setObjectQuestionScore(Integer totalScore, Integer examRecordId) {
        examRecordService.setObjectQuestionScore(totalScore, examRecordId);
        return HttpResult.success();
    }

    @GetMapping("/getQuestionIdsByExamId")
    @ApiOperation("根据考试id查询考试的题目id")
    public HttpResult getQuestionIdsByExamId(@RequestParam Integer examId) {
        return HttpResult.success(examService.getQuestionIdsByExamId(examId));
    }

    @PostMapping("/uploadQuestionImage")
    @ApiOperation("接受前端上传的图片,返回图片地址")
    public HttpResult uploadQuestionImage(MultipartFile file) {
        return HttpResult.success(FileUtils.uploadImg(imgPath,file));
    }

    @GetMapping("/getExamPassRate")
    @ApiOperation("提供每一门考试的通过率数据(echarts绘图)")
    public HttpResult getExamPassRate() {
        return HttpResult.success(examService.getExamPassRateEchartData());
    }

    @GetMapping("/getExamNumbers")
    @ApiOperation("提供每一门考试的考试次数(echarts绘图)")
    public HttpResult getExamNumbers() {
        return HttpResult.success(examService.getExamNumbersEchartData());
    }

}
