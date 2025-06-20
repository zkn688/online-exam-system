package com.java.exam.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.java.exam.entity.Question;
import com.java.exam.utils.PageResponse;
import com.java.exam.vo.QuestionImportVo;
import com.java.exam.vo.QuestionQueryVo;
import com.java.exam.vo.QuestionVo;

import java.util.List;

public interface QuestionService extends IService<Question> {

    PageResponse<Question> getQuestion(QuestionQueryVo queryVo);

    QuestionVo getQuestionVoById(Integer id);

    PageResponse<QuestionVo> getQuestionVoByIds(List<Integer> ids);

    void deleteQuestionByIds(String questionIds);

    void addQuestion(QuestionVo questionVo);

    void updateQuestion(QuestionVo questionVo);

    void importQuestion(QuestionImportVo questionImportVo);
}
