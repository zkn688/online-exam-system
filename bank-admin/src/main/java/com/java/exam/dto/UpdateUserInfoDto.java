package com.java.exam.dto;

import lombok.Data;

@Data
public class UpdateUserInfoDto {

    private String username;

    private String password;

    private String trueName;

    private String email;

    private Integer roleId;
}
