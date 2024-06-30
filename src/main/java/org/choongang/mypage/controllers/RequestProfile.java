package org.choongang.mypage.controllers;

import lombok.Data;

@Data
public class RequestProfile {
    private String userName;
    private String password;
    private String confirmPassword;
}
