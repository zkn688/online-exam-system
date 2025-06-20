package com.java.exam.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionQueryVo {

    private String questionType;
    private String questionBank;
    private String questionContent;
    private String professionIds;
    private Integer pageNo;
    private Integer pageSize;

}
