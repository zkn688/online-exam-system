package com.java.exam.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionImportVo {

    private Integer[] bankId;

    private Integer professionId;

    private String createPerson;

    private List<QuestionVo> questionData;
}
