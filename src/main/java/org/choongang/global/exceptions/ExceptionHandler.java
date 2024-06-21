package org.choongang.global.exceptions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface ExceptionHandler {
    Class<Exception> value();
}
