package org.choongang.member.controllers;

import lombok.Data;

@Data
public class RequestLogin {
    private String email;
    private String password;
    private boolean saveEmail;
    private String redirectUrl;
}
