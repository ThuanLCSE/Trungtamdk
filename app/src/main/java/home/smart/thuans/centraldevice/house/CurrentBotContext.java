package home.smart.thuans.centraldevice.house;

import home.smart.thuans.centraldevice.artificialBot.DetectIntent;
import home.smart.thuans.centraldevice.device.ConnectedDevice;

/**
 * Created by Thuans on 5/2/2017.
 */

public class CurrentBotContext {
    private static volatile CurrentBotContext instance = null;
    private DetectIntent detected;
    private ConnectedDevice deviceTarget;

    public ConnectedDevice getDeviceTarget() {
        return deviceTarget;
    }

    public DetectIntent getDetected() {
        return detected;
    }

    public void setDetected(DetectIntent detected) {
        this.detected = detected;
    }

    public void setDeviceTarget(ConnectedDevice deviceTarget) {
        this.deviceTarget = deviceTarget;
    }

    private CurrentBotContext() { }

    public static CurrentBotContext getInstance() {
        if(instance == null) {
            synchronized(CurrentBotContext.class) {
                if(instance == null) {
                    instance= new CurrentBotContext();
                }
            }

        }
        return instance;
    }
}
