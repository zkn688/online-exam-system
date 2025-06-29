package com.java.exam.entity;


import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exam implements Serializable {


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键 考试id", example = "1")
    private Integer examId;

    @ApiModelProperty(value = "考试名称", example = "小学一年级考试")
    private String examName;

    @ApiModelProperty(value = "考试描述", example = "这是一场考试的描述")
    private String examDesc;

    @ApiModelProperty(value = "考试类型1公开,2需密码", example = "1")
    private Integer type;

    @ApiModelProperty(value = "考试密码,当type=2时候存在", example = "12345")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String password;

    @ApiModelProperty(value = "考试时间", example = "125(分钟)")
    private Integer duration;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "考试开始时间", example = "2020-11-01")
    @TableField(strategy = FieldStrategy.IGNORED)
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "考试结束时间", example = "2020-12-01")
    @TableField(strategy = FieldStrategy.IGNORED)
    private Date endTime;

    @ApiModelProperty(value = "考试总分", example = "100")
    private Integer totalScore;

    @ApiModelProperty(value = "考试及格线", example = "60")
    private Integer passScore;

    @ApiModelProperty(value = "考试状态 1有效 2无效", example = "1")
    private Integer status;
}
