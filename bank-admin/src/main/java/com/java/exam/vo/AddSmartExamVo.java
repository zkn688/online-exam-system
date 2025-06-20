package com.java.exam.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddSmartExamVo {

    private Integer examDuration;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;

    private String examDesc;

    private String examName;

    @ApiModelProperty(value = "考试难度", example = "0")
    private Integer level;

    @ApiModelProperty(value = "考试课程id", example = "1")
    private String pid;

    @ApiModelProperty(value = "判断题分数", example = "20")
    private Integer judgeScore;

    @ApiModelProperty(value = "多选题分数", example = "20")
    private Integer multipleScore;

    @ApiModelProperty(value = "简答题分数", example = "20")
    private Integer shortScore;

    @ApiModelProperty(value = "单选题分数", example = "20")
    private Integer singleScore;

    @ApiModelProperty(value = "判断题数量", example = "20")
    private Integer judgeNum;

    @ApiModelProperty(value = "填空题分数", example = "20")
    private Integer fillingScore;

    @ApiModelProperty(value = "填空题数量", example = "20")
    private Integer fillingNum;

    @ApiModelProperty(value = "多选题数量", example = "20")
    private Integer multipleNum;

    @ApiModelProperty(value = "简答题数量", example = "20")
    private Integer shortNum;

    @ApiModelProperty(value = "单选题数量", example = "20")
    private Integer singleNum;

    @ApiModelProperty(value = "通过分数", example = "20")
    private Integer passScore;

    @ApiModelProperty(value = "总分数", example = "20")
    private Integer totalScore;

    private Integer status;

    private Integer type;

    private String password;

}
