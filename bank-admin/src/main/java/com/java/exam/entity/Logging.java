package com.java.exam.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Logging {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("操作描述")
    private String operation;

    @ApiModelProperty("请求方法名")
    private String method;

    @ApiModelProperty("请求参数")
    private String params;

    @ApiModelProperty("执行时间")
    private Long time;

    @ApiModelProperty("操作用户")
    private String createUser;


    @ApiModelProperty("操作时间")
    private Date createTime;

}
