package com.java.exam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDto {

    private String username;
    private String password;
    private Integer roleId;
    private String trueName;
    private String email;

}
