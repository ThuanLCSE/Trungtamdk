package home.smart.thuans.centraldevice.http.pojoDto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sam on 4/15/2017.
 */
public class ResponseMessage {

    @SerializedName("message")
    private String message;

    @SerializedName("tokenKey")
    private String tokenKey;

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
