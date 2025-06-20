package com.java.exam.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.exam.entity.ExamQuestion;

public interface ExamQuestionService extends IService<ExamQuestion> {

    ExamQuestion getExamQuestionByExamId(Integer examId);

}
