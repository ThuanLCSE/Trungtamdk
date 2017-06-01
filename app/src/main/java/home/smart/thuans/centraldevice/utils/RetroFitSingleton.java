package home.smart.thuans.centraldevice.utils;

import home.smart.thuans.centraldevice.http.CloudApi;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Thuans on 4/27/2017.
 */

public class RetroFitSingleton {
    private Retrofit retrofit;
    public static String hostBaseUrl = "http://192.168.137.1:8080";
    private CloudApi cloudApi;
    private static volatile RetroFitSingleton retroFitSingleton = null;


    private RetroFitSingleton() { }

    public static RetroFitSingleton getInstance() {
        if(retroFitSingleton == null) {
            synchronized(RetroFitSingleton.class) {
                if(retroFitSingleton == null) {
                    retroFitSingleton= new RetroFitSingleton();
                    retroFitSingleton.retrofit = new Retrofit.Builder()
                            .baseUrl(hostBaseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    retroFitSingleton.cloudApi = retroFitSingleton.retrofit.create(CloudApi.class);
                }
            }

        }
        return retroFitSingleton;
    }

    public CloudApi getCloudApi() {
        return cloudApi;
    }
}
