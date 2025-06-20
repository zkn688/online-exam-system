package com.java.exam.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordVo {

    private String examId;
    private Integer pageNo;
    private Integer pageSize;
    private Integer isMark;
    private String subsection;
}
