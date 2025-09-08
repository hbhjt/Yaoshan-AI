package com.yaoshan.backend.pojo.DTO;

import lombok.Data;


@Data
public class UserRegisterDTO {
    private String phone; //手机号
    private String password; //密码
    private String confirmPassword; //确认密码
    private String nickname; //昵称
}
