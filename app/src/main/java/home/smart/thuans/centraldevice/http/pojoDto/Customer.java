package home.smart.thuans.centraldevice.http.pojoDto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sam on 4/14/2017.
 */
public class Customer {
    @SerializedName("id")
    private String id;
    @SerializedName("houseId")
    private String houseId;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("fullname")
    private String fullname;
    @SerializedName("address")
    private String address;
    @SerializedName("email")
    private String email;
    @SerializedName("telephone")
    private String telephone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
