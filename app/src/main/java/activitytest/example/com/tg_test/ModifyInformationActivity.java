package activitytest.example.com.tg_test;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;


public class ModifyInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button bt_return;
    private Button bt_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyinformation);
        bt_return = findViewById(R.id.bt_return);
        bt_save = findViewById(R.id.bt_save);
        bt_return.setOnClickListener(this);
        bt_save.setOnClickListener(this);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, ModifyInformationActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_return:
                Intent intent = new Intent(ModifyInformationActivity.this,WowindowActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_save:
                EventBus.getDefault().post(new MessageEvent("Hello EventBus!"));
                break;
            default:
                break;
        }

    }
}


