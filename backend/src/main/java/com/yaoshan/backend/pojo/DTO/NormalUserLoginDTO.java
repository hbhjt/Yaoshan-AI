package com.yaoshan.backend.pojo.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class NormalUserLoginDTO implements Serializable {

    private String phone;
    private String password;
}
