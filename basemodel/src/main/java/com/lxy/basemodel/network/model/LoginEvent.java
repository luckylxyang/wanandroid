package com.lxy.basemodel.network.model;

/**
 * Creator : lxy
 * date: 2019/3/3
 */

public class LoginEvent {

    private boolean hasSuccess;

    public LoginEvent(boolean hasSuccess) {
        this.hasSuccess = hasSuccess;
    }

    public boolean isHasSuccess() {
        return hasSuccess;
    }

    public void setHasSuccess(boolean hasSuccess) {
        this.hasSuccess = hasSuccess;
    }
}
