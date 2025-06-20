package com.java.exam.dto;

import lombok.Data;

@Data
public class LoginDto {

    private String username;

    private String password;

    private Integer roleId;
}
