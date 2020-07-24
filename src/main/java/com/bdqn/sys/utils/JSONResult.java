package com.bdqn.sys.utils;

public class JSONResult {

    /**
     * 执行的结果是否成功
     * true:成功
     * false:失败
     */
    private Boolean success;

    /**
     * 执行后返回的结果信息
     */
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONResult(){}

    /**
     * 带参构造方法
     * @param success   执行是否成功
     * @param message   返回的结果信息
     */
    public JSONResult(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
