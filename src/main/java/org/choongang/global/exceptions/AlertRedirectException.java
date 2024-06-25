package org.choongang.global.exceptions;

public class AlertRedirectException extends AlertException {

    private String redirectUrl;
    private String target;

    public AlertRedirectException(String message, String redirectUrl, int status, String target) {
        super(message, status);
        this.redirectUrl = redirectUrl;
        this.target = target;
    }

    public AlertRedirectException(String message, String redirectUrl, int status) {
        this(message, redirectUrl, status, "self");
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getTarget() {
        return target;
    }
}
