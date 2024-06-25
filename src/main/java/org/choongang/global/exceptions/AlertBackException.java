package org.choongang.global.exceptions;

public class AlertBackException extends AlertException {
    private String target;

    public AlertBackException(String message, int status, String target) {
        super(message, status);
        this.target = target;
    }

    public AlertBackException(String message, int status) {
        this(message, status, "self");
    }

    public String getTarget() {
        return target;
    }
}
