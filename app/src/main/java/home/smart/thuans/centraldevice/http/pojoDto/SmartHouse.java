package home.smart.thuans.centraldevice.http.pojoDto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import home.smart.thuans.centraldevice.device.ConnectedDevice;

/**
 * Created by Thuans on 4/16/2017.
 */
public class SmartHouse {

    @SerializedName("id")
    private String id;

    @SerializedName("engineerId")
    private String engineerId;
    @SerializedName("botTypeId")
    private String botTypeId;
    @SerializedName("tokenKey")
    private String tokenKey;
    @SerializedName("centralStaticAddress")
    private String centralStaticAddress;
    @SerializedName("setupDate")
    private Date setupDate;
    @SerializedName("devices")
    private List<ConnectedDevice> devices;

    public List<ConnectedDevice> getDevices() {
        return devices;
    }

    public void setDevices(List<ConnectedDevice> devices) {
        this.devices = devices;
    }

    public String getBotTypeId() {
        return botTypeId;
    }

    public void setBotTypeId(String botTypeId) {
        this.botTypeId = botTypeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(String engineerId) {
        this.engineerId = engineerId;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getCentralStaticAddress() {
        return centralStaticAddress;
    }

    public void setCentralStaticAddress(String centralStaticAddress) {
        this.centralStaticAddress = centralStaticAddress;
    }

    public Date getSetupDate() {
        return setupDate;
    }

    public void setSetupDate(Date setupDate) {
        this.setupDate = setupDate;
    }
}
