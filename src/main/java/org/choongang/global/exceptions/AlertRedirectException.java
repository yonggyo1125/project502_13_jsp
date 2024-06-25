package org.choongang.global.exceptions;

public class AlertRedirectException extends AlertException {

    private String redirectUrl;

    public AlertRedirectException(String message, String redirectUrl, int status) {
        super(message, status);
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
