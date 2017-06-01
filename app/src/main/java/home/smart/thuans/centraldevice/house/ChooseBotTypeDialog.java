package home.smart.thuans.centraldevice.house;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import home.smart.thuans.centraldevice.R;
import home.smart.thuans.centraldevice.artificialBot.DetectIntent;
import home.smart.thuans.centraldevice.artificialBot.DetectIntentSQLite;
import home.smart.thuans.centraldevice.artificialBot.IntentLearned;
import home.smart.thuans.centraldevice.artificialBot.IntentLearnedSQLite;
import home.smart.thuans.centraldevice.artificialBot.Term;
import home.smart.thuans.centraldevice.artificialBot.TermSQLite;
import home.smart.thuans.centraldevice.device.ConnectedDevice;
import home.smart.thuans.centraldevice.device.DeviceSQLite;
import home.smart.thuans.centraldevice.http.CloudApi;
import home.smart.thuans.centraldevice.artificialBot.BotData;
import home.smart.thuans.centraldevice.http.pojoDto.BotType;
import home.smart.thuans.centraldevice.http.pojoDto.SmartHouse;
import home.smart.thuans.centraldevice.utils.RetroFitSingleton;
import home.smart.thuans.centraldevice.utils.SharedPrefConstant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thuans on 4/26/2017.
 */

public class ChooseBotTypeDialog extends Dialog {
    private static final String TAG = "Chon bot Dialog";
    public Activity c;
    public Dialog d;
    public Button btnDone;
    public Spinner spinBotType;
    public Spinner spinBotName;
    List<BotType> botTypeList;
    List<String> botNameList;
    CloudApi cloudApi;
    View load;

    private boolean flag = false;

    public ChooseBotTypeDialog(Activity context) {
        super(context);
        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_bot_type_dialog);
        spinBotType = (Spinner) findViewById(R.id.spn_bot_type_choose);
        spinBotName = (Spinner) findViewById(R.id.spn_bot_name_choose);
        btnDone = (Button) findViewById(R.id.btn_done);load = findViewById(R.id.load_gif);
        load.setVisibility(View.VISIBLE);
        RetroFitSingleton retro = RetroFitSingleton.getInstance();
        cloudApi = retro.getCloudApi();

        Call<List<BotType>> listAllBotApi = cloudApi.getListBotType();
        listAllBotApi.enqueue(new Callback<List<BotType>>() {
            @Override
            public void onResponse(Call<List<BotType>> call, Response<List<BotType>> response) {
                botTypeList = response.body();
                Log.d(TAG,botTypeList.size()+" SIze bot");
                List<String> botTypeNameList = new ArrayList<String>();
                for (BotType botType: botTypeList){
                    botTypeNameList.add(botType.name);
                }
                String[] botTypeArr = new String[botTypeList.size()];
                botTypeArr = botTypeNameList.toArray(botTypeArr);
                ArrayAdapter botRoleAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_dropdown_item, botTypeArr);
                spinBotType.setAdapter(botRoleAdapter);
                load.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                TextView txtRes = (TextView) findViewById(R.id.txt_Result);
                txtRes.setText("Tải danh sách bot thất bại");
                load.setVisibility(View.INVISIBLE);
                call.cancel();
            }
        });

        Call<List<String>> listAllBotName = cloudApi.getListBotName();
        listAllBotName.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                botNameList = response.body();
                Log.d(TAG,botNameList.size()+" tất cả tên bot");

                String[] botNameArr = new String[botNameList.size()];
                botNameArr = botNameList.toArray(botNameArr);
                ArrayAdapter botNameAdap = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_dropdown_item, botNameArr);
                spinBotName.setAdapter(botNameAdap);
                load.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                TextView txtRes = (TextView) findViewById(R.id.txt_Result);
                txtRes.setText("Tải ten bot thất bại");
                load.setVisibility(View.INVISIBLE);
                call.cancel();
            }
        });

         btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SmartHouse smartHouse = new SmartHouse();
                Log.d("CHOOSE BOT",botTypeList.get(spinBotType.getSelectedItemPosition()).id);
                smartHouse.setBotTypeId(botTypeList.get(spinBotType.getSelectedItemPosition()).id);
                Call getBotData = cloudApi.getBotData(smartHouse);
                getBotData.enqueue(new Callback<BotData>() {
                    @Override
                    public void onResponse(Call<BotData> call, Response<BotData> response) {

                        BotData botData = response.body();
                        if (botData != null) {
                            TermSQLite termSQLite = new TermSQLite();
                            termSQLite.delete();
                            for (Term term : botData.getListTerm()) {
                                termSQLite.insert(term);
                            }
                            DetectIntentSQLite detectIntentSQLite = new DetectIntentSQLite();
                            detectIntentSQLite.delete();
                            for (DetectIntent detectIntent : botData.getDetectIntentList()) {
                                detectIntentSQLite.insert(detectIntent);
                            }
                            IntentLearnedSQLite intentLearnedSQLite = new IntentLearnedSQLite();
                            intentLearnedSQLite.delete();
                            for (IntentLearned intentLearned : botData.getLearnedReplyList()) {
                                intentLearnedSQLite.insert(intentLearned);
                            }
                            HouseConfig house = HouseConfig.getInstance();
                            house.setBotTypeName(botTypeList.get(spinBotType.getSelectedItemPosition()).name);
                            house.setBotName(botNameList.get(spinBotType.getSelectedItemPosition()));
                            Log.d(TAG,botTypeList.get(spinBotType.getSelectedItemPosition()).name+ " name bot setted");

                            TextView txtRes = (TextView) findViewById(R.id.txt_Result);
                            txtRes.setText("Tải dữ liệu thành công");
                        } else {
                            TextView txtRes = (TextView) findViewById(R.id.txt_Result);
                            txtRes.setText("Tải dữ liệu thất bại");
                            load.setVisibility(View.INVISIBLE);
                        }
                        dismiss();
                    }

                    @Override
                    public void onFailure(Call<BotData> call, Throwable t) {
                        call.cancel();
                        TextView txtRes = (TextView) findViewById(R.id.txt_Result);
                        txtRes.setText("Tải dữ liệu thất bại");
                        load.setVisibility(View.INVISIBLE);
                    }
                });
                load.setVisibility(View.VISIBLE);
            }
        });

    }
}
