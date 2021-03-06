package home.smart.thuans.centraldevice.http;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import home.smart.thuans.centraldevice.MainActivity;
import home.smart.thuans.centraldevice.house.HouseConfig;

/**
 * Created by Thuans on 4/19/2017.
 */

public class HttpResponseThread extends Thread {
    private final String TAG = "HttpResponseThread";
    Socket socket;

    HttpResponseThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader is;
        PrintWriter os;
        String request;


        try {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            request = is.readLine();
            Log.d(TAG,request);
            //kiem tra request url
            String response = null;
            HouseConfig house = HouseConfig.getInstance();
            if (house.getFaceDetected() > 0) {
                String encoded = Base64.encodeToString(house.getFaceImage(), Base64.DEFAULT);
                response = encoded;
            } if (house.getVoiceContent().length() > 0) {
                String content = house.getVoiceContent();
                response = content;
            }
            else {
                response = "No body home";
            }
            os = new PrintWriter(socket.getOutputStream(), true);







            os.print("HTTP/1.0 200" + "\r\n");
            os.print("Content type: text/html" + "\r\n");
            os.print("Content length: " + response.length() + "\r\n");
            os.print("\r\n");
            os.print(response + "\r\n");
            os.flush();
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return;
    }
}
