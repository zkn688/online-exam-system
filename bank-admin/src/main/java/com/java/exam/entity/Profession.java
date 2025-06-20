package com.java.exam.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("profession")
public class Profession {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer pid;

    private String title;

    private Integer weight;

    private Integer createUser;

    private Integer createTime;

}
