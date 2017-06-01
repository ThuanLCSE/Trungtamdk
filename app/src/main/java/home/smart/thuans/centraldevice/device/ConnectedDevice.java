package home.smart.thuans.centraldevice.device;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Thuans on 4/16/2017.
 */
public class ConnectedDevice {
    public static final String DEFAULT_VALUE = "...";
    public static final String STATE_OFF = "off";
    @SerializedName("id")
    private String id;
    @SerializedName("port")
    private String port;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;
    @SerializedName("feature")
    private String feature;
    @SerializedName("value")
    private String value;
    @SerializedName("state")
    private String state;
    @SerializedName("lastUpdate")
    private Date lastUpdate;
    private List<String> secondaryName;

    public List<String> getSecondaryName() {
        return secondaryName;
    }

    public void addSecondaryName(String secondaryName) {
        this.secondaryName.add(secondaryName);
    }

    public  ConnectedDevice(){
        this.secondaryName = new ArrayList<>();
    }

    public  ConnectedDevice(String name,String port, String state, String value, String feature){
        this.name = name;
        this.port = port;
        this.state = state;
        this.value = value;
        this.feature = feature;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        secondaryName.add(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getState() {
        return state;
    }

    public void switchState(){
        if (this.state.equals("on")){
            this.state = "off";
        }
        if (this.state.equals("tắt")){
            this.state = "bật";
        }
    }

    public void setState(String state) {
        this.state = state;
    }
}
