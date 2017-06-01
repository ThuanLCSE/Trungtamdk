package home.smart.thuans.centraldevice.http.pojoDto;

/**
 * Created by Thuans on 4/18/2017.
 */
public class CommandDTO {
    private String deviceId;
    private String state;
    private String content;

    public CommandDTO(){

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
