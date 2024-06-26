package org.choongang.admin.advices;

import org.choongang.global.config.annotations.ControllerAdvice;
import org.choongang.global.config.annotations.ModelAttribute;

@ControllerAdvice("org.choongang.admin")
public class AdminControllerAdvice {

    @ModelAttribute
    public String testValue() {
        System.out.println("testValue!!");
        return "test";
    }
}
