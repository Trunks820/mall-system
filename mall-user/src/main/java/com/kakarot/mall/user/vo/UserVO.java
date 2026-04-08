package com.kakarot.mall.user.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    private Long id;

    private String nickName;

    private String avatarUrl;

    private String phone;
}
