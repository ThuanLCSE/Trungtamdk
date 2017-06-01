package home.smart.thuans.centraldevice.http.pojoDto;

/**
 * Created by Sam on 4/14/2017.
 */
public class CustomerRemoteDeviceDTO {
    private int remoteDeviceId;
    private int smartHouseId;
    private int quantity;

    public CustomerRemoteDeviceDTO() {
    }

    public CustomerRemoteDeviceDTO(int remoteDeviceId, int smartHouseId, int quantity) {
        this.remoteDeviceId = remoteDeviceId;
        this.smartHouseId = smartHouseId;
        this.quantity = quantity;
    }

    public int getRemoteDeviceId() {
        return remoteDeviceId;
    }

    public void setRemoteDeviceId(int remoteDeviceId) {
        this.remoteDeviceId = remoteDeviceId;
    }

    public int getSmartHouseId() {
        return smartHouseId;
    }

    public void setSmartHouseId(int smartHouseId) {
        this.smartHouseId = smartHouseId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
