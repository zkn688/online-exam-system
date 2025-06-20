package com.java.exam.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVo {

    private Integer questionType;

    private Integer questionId;

    private Integer professionId;

    private Integer questionLevel;

    private Integer[] bankId;

    private String questionContent;

    private String[] images;

    private String analysis;

    private String createPerson;

    private Answer[] Answer;

    //答案对象
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Answer {

        private Integer id;

        private String isTrue;

        private String answer;

        private String analysis;
    }
}
