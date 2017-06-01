package home.smart.thuans.centraldevice.http.pojoDto;

import java.util.Date;
import java.util.List;

/**
 * Created by Sam on 4/14/2017.
 */
public class OrderDTO {
    private String id;
    private String customerId;
    private String username;
    private String state;
    private Date orderDay;
    private double total;
    private ShipInfoDTO shipInfo;
    private List<OrderItemDTO> devices;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getOrderDay() {
        return orderDay;
    }

    public void setOrderDay(Date orderDay) {
        this.orderDay = orderDay;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ShipInfoDTO getShipInfo() {
        return shipInfo;
    }

    public void setShipInfo(ShipInfoDTO shipInfo) {
        this.shipInfo = shipInfo;
    }

    public List<OrderItemDTO> getDevices() {
        return devices;
    }

    public void setDevices(List<OrderItemDTO> devices) {
        this.devices = devices;
    }

    public double getTotal() {
        this.total = 0;
        for (OrderItemDTO item : this.getDevices()){
            this.total += item.getPrice();
        }
        return this.total;
    }
}
