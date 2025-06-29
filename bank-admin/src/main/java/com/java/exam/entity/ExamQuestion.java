package com.java.exam.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("考试里的题目实体")
@TableName(value = "exam_question")
public class ExamQuestion implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键 考试题目表的id", example = "1")
    private Integer id;

    @ApiModelProperty(value = "问题的id字符串", example = "1,2,3")
    private String questionIds;

    @ApiModelProperty(value = "考试的id", example = "1")
    private Integer examId;

    @ApiModelProperty(value = "考试中每一题的分数", example = "1,2,3")
    private String scores;

}
