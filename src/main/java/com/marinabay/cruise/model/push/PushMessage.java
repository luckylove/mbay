package com.marinabay.cruise.model.push;

/**
 * User: son.nguyen
 * Date: 12/11/14
 * Time: 10:31 PM
 */
public class PushMessage {

    private String message;
    private String token;
    private String keyStore;
    private String keyPass;

    public static PushMessage getInstance(String message, String token, String keyStore, String keyPass){
        PushMessage msg = new PushMessage();
        msg.setMessage(message);
        msg.setToken(token);
        msg.setKeyStore(keyStore);
        msg.setKeyPass(keyPass);
        return msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyPass() {
        return keyPass;
    }

    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }
}
