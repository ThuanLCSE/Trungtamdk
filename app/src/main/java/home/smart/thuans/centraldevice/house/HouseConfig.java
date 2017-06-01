package home.smart.thuans.centraldevice.house;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import home.smart.thuans.centraldevice.device.ConnectedDevice;
import home.smart.thuans.centraldevice.utils.DeviceConstant;

/**
 * Created by Thuans on 4/19/2017.
 */

public class HouseConfig {
    public static final long DELAY_AUTO_PROTECT = 5*1000;
    private String arduinoAddress;
    private static volatile HouseConfig singletonHouse = null;


    private HouseConfig() { }

    public static HouseConfig getInstance() {
        if(singletonHouse == null) {
            synchronized(HouseConfig.class) {
                if(singletonHouse == null) {
                    singletonHouse= new HouseConfig();
                    singletonHouse.setFaceDetected(0);
                    singletonHouse.setVoiceContent("");
                    singletonHouse.devices = new ArrayList<>();
                    singletonHouse.setGuestIsAquaintance(true);
                    singletonHouse.setDetectThiefMoment(null);
                }
            }

        }
        return singletonHouse;
    }
    private int faceDetected;
    private byte[] faceImage;
    private String tokenKey;
    private String voiceContent;
    private String botName;
    private String botRole;
    private String ownerName;
    private String ownerRole;
    private String botTypeName;
    private boolean guestIsAquaintance;
    private Date detectThiefMoment;
    private List<ConnectedDevice> devices;

    public Date getDetectThiefMoment() {
        return detectThiefMoment;
    }

    public void setDetectThiefMoment(Date detectThiefMoment) {
        this.detectThiefMoment = detectThiefMoment;
    }

    public List<ConnectedDevice> getDevices() {
        return devices;
    }
    public void changeValueByPort(String port, String value){
        for (ConnectedDevice device: devices){
            if (device.getPort().equals(port)){
                device.setValue(value);
            }
        }
    }
    public void changeStateByPort(String port, String state){
        for (ConnectedDevice device: devices){
            if (device.getPort().equals(port)){
                device.setState(state);
            }
        }
    }
    public boolean mainDoorOpen(){
        ConnectedDevice doorSensor = getDeviceByPort(DeviceConstant.MAIN_DOOR_SENSOR_PORT);
        if (doorSensor.getValue().equals(DeviceConstant.DOOR_OPEN)){
            return true;
        }
        return false;
    }
    public ConnectedDevice getDeviceByPort(String port){
        for (ConnectedDevice device: devices){
            if (device.getPort().equals(port)){
                return device;
            }
        }
        return null;
    }

    public static HouseConfig getSingletonHouse() {
        return singletonHouse;
    }


    public String getArduinoAddress() {
        return arduinoAddress;
    }

    public void setArduinoAddress(String arduinoAddress) {
        this.arduinoAddress = arduinoAddress;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public void setDevices(List<ConnectedDevice> devices) {
        this.devices = devices;
    }

    public String getBotTypeName() {
        return botTypeName;
    }

    public void setBotTypeName(String botTypeName) {
        this.botTypeName = botTypeName;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getBotRole() {
        return botRole;
    }

    public void setBotRole(String botRole) {
        this.botRole = botRole;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerRole() {
        return ownerRole;
    }

    public void setOwnerRole(String ownerRole) {
        this.ownerRole = ownerRole;
    }

    public int getFaceDetected() {
        return faceDetected;
    }

    public void setFaceDetected(int faceDetected) {
        this.faceDetected = faceDetected;
    }

    public byte[] getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(byte[] faceImage) {
        this.faceImage = faceImage;
    }

    public String getVoiceContent() {
        return voiceContent;
    }

    public void setVoiceContent(String voiceContent) {
        this.voiceContent = voiceContent;
    }

    public void setGuestIsAquaintance(boolean guestIsAquaintance) {
        singletonHouse.guestIsAquaintance = guestIsAquaintance;
    }

    public boolean guestIsAquaintance() {
        return singletonHouse.guestIsAquaintance;
    }
}
