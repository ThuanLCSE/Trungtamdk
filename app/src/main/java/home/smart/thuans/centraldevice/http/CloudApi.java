package home.smart.thuans.centraldevice.http;

import java.util.List;

import home.smart.thuans.centraldevice.artificialBot.BotData;
import home.smart.thuans.centraldevice.http.pojoDto.BotType;
import home.smart.thuans.centraldevice.http.pojoDto.Customer;
import home.smart.thuans.centraldevice.http.pojoDto.SmartHouse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Thuans on 4/26/2017.
 */

public interface CloudApi {
    @GET("/api/central/getAllBotType")
    Call<List<BotType>> getListBotType();

    @GET("/api/central/getAllBotName")
    Call<List<String>> getListBotName();

    @POST("/api/central/initiating")
    Call<SmartHouse> mobileLogin(@Body Customer customer);

    @POST("/api/central/getBotData")
    Call<BotData> getBotData(@Body SmartHouse smartHouse);

    @GET("/{state}/{port}")
    Call<ResponseBody> sendCommand(@Path("port") String port,@Path("state") String state);
    @GET("/")
    Call<ResponseBody> cbeckArduinoConnect();
}
