package home.smart.thuans.centraldevice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;

import home.smart.thuans.centraldevice.artificialBot.IntentConstant;
import home.smart.thuans.centraldevice.artificialBot.IntentLearned;
import home.smart.thuans.centraldevice.house.ChooseBotTypeDialog;
import home.smart.thuans.centraldevice.house.HouseConfig;
import home.smart.thuans.centraldevice.utils.BotUtils;
import home.smart.thuans.centraldevice.utils.SharedPrefConstant;

public class ConfigurationActivity extends AppCompatActivity {
    public static final String CONFIGURATION_DONE = "config bot role bot name owner name done";
    TextView txtBotName;
    EditText editOwnerName;
    Spinner spinOwnerRole;
    Spinner spinBotRole;
    ArrayAdapter<String> ownerRoleAdapter = null;
    ArrayAdapter<String> botRoleAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        txtBotName = (TextView) findViewById(R.id.txt_bot_name);
        editOwnerName = (EditText) findViewById(R.id.edt_owner_name);
        spinOwnerRole = (Spinner) findViewById(R.id.spn_owner_role);
        spinBotRole = (Spinner) findViewById(R.id.spn_bot_role);
        Button btnDone = (Button) findViewById(R.id.btn_Config_Done);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseConfig houseConfig = HouseConfig.getInstance();
                houseConfig.setBotRole(botRoleAdapter.getItem(spinBotRole.getSelectedItemPosition()));
                houseConfig.setOwnerName(editOwnerName.getText().toString());
                houseConfig.setOwnerRole(ownerRoleAdapter.getItem(spinOwnerRole.getSelectedItemPosition()));

                IntentLearned sayHi = BotUtils.getIntentByName(IntentConstant.SAY_HELLO);
                String sayHiSentence = BotUtils.completeSentence(sayHi.getSentence(), "", "");
                Intent intent = new Intent(ConfigurationActivity.this,MainActivity.class);
                intent.putExtra(CONFIGURATION_DONE,sayHiSentence);
                startActivity(intent);
            }
        });
        Button btnRefresh = (Button) findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HouseConfig house = HouseConfig.getInstance();
                refreshViewData(house);
            }
        });
        Button btnDevBot = (Button) findViewById(R.id.btn_dev_upbot);
        btnDevBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseBotTypeDialog chooseBotTypeDialog = new ChooseBotTypeDialog(ConfigurationActivity.this);
                chooseBotTypeDialog.setTitle("Chọn quản gia");
                chooseBotTypeDialog.show();
                return;
            }
        });

    }
    private void refreshViewData(HouseConfig house){

        txtBotName.setText(house.getBotName());
        editOwnerName.setText(house.getOwnerName());
        if (house.getBotTypeName().equals(SharedPrefConstant.BOT_TYPE_QUAN_GIA_GIA)){
            String[] botRole = SharedPrefConstant.QUAN_GIA_GIA_BOT_ROLE_ARR;
            botRoleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, botRole);
            spinBotRole.setAdapter(botRoleAdapter);
            String[] ownerRole = SharedPrefConstant.QUAN_GIA_GIA_OWNER_ROLE_ARR;
            ownerRoleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ownerRole);
            spinOwnerRole.setAdapter(ownerRoleAdapter);
        } else {
            //truong hop bot type khac
        }
        if (ownerRoleAdapter.getPosition(house.getOwnerRole()) >= 0){
            spinOwnerRole.setSelection(ownerRoleAdapter.getPosition(house.getOwnerRole()));
        }
        if (botRoleAdapter.getPosition(house.getBotRole()) >= 0){
            spinBotRole.setSelection(ownerRoleAdapter.getPosition(house.getBotRole()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        HouseConfig house = HouseConfig.getInstance();
        if (house.getBotTypeName()== null) {
            ChooseBotTypeDialog chooseBotTypeDialog = new ChooseBotTypeDialog(ConfigurationActivity.this);
            chooseBotTypeDialog.setTitle("Chọn quản gia");
            chooseBotTypeDialog.show();
            return;
        }
        refreshViewData(house);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences(SharedPrefConstant.SMART_HOUSE_SHARED_PREF,MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        HouseConfig house = HouseConfig.getInstance();

        edit.putString(SharedPrefConstant.BOT_NAME,house.getBotName());
        edit.putString(SharedPrefConstant.BOT_ROLE,house.getBotRole());
        edit.putString(SharedPrefConstant.OWNER_NAME,house.getOwnerName());
        edit.putString(SharedPrefConstant.OWNER_ROLE,house.getOwnerRole());
        edit.putString(SharedPrefConstant.BOT_TYPE,house.getBotTypeName());
        edit.commit();
    }
}
