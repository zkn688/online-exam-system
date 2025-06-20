package com.java.exam.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.exam.entity.Exam;
import com.java.exam.utils.PageResponse;
import com.java.exam.vo.AddExamByQuestionVo;
import com.java.exam.vo.AddSmartExamVo;
import com.java.exam.vo.ExamQueryVo;

import java.util.List;

public interface ExamService extends IService<Exam> {

    PageResponse<Exam> getExamPage(ExamQueryVo examQueryVo);

    AddExamByQuestionVo getExamInfoById(Integer examId);

    void operationExam(Integer type, String ids);

    void addExamBySmart(AddSmartExamVo addSmartExamVo);

    void addExamByQuestionList(AddExamByQuestionVo addExamByQuestionVo);

    void updateExamInfo(AddExamByQuestionVo addExamByQuestionVo);

    String getQuestionIdsByExamId(Integer examId);

    List<String> getExamPassRateEchartData();

    List<String> getExamNumbersEchartData();
}
