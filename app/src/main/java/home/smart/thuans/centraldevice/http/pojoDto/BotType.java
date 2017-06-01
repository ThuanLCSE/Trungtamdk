package home.smart.thuans.centraldevice.http.pojoDto;

import com.google.gson.annotations.SerializedName;


public class BotType {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("available")
    public boolean available;

}
