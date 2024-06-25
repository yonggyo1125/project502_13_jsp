package org.choongang.member.controllers;

import lombok.Data;

@Data
public class RequestJoin {
    private String email;
    private String password;
    private String confirmPassword;
    private String userName;
    private boolean termsAgree;
}
