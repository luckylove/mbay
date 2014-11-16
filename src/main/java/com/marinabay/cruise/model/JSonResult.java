package com.marinabay.cruise.model;

/**
 * User: son.nguyen
 * Date: 9/24/14
 * Time: 10:29 PM
 */
public class JSonResult<T> {

    private boolean success;
    private String errorMsg;
    private int errorCode;
    private T result;

    public static JSonResult ofError(String errorMsg) {
        JSonResult rs = new JSonResult();
        rs.setSuccess(false);
        rs.setErrorMsg(errorMsg);
        return rs;
    }

    public static JSonResult ofError(String errorMsg, int code) {
        JSonResult rs = new JSonResult();
        rs.setSuccess(false);
        rs.setErrorMsg(errorMsg);
        rs.setErrorCode(code);
        return rs;
    }

    public static <E> JSonResult ofSuccess(E result) {
        JSonResult<E> rs = new JSonResult<E>();
        rs.setSuccess(true);
        rs.setResult(result);
        rs.setErrorCode(200);
        return rs;
    }



    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
