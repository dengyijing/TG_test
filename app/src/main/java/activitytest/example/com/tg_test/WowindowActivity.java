package activitytest.example.com.tg_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class WowindowActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "WowindowActivity";
    private Button bt_return,bt_tologin;
    private TextView mTvMessage;
    //声明控件
    private Spinner spinner;
    private ArrayAdapter arr_adapter = null;
    private List<String> list = new ArrayList<String>();
    private TextView text;
    private String spinnerText;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wowindow);
        initContentView();
        // 注册订阅者
        EventBus.getDefault().register(this);

        //跳转页面
       bt_return = findViewById(R.id.bt_return);
       bt_tologin = findViewById(R.id.bt_tologin);
       bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WowindowActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
       bt_tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WowindowActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        //关联控件
        spinner = (Spinner) findViewById(R.id.spinner_equment_name);
        //text = (TextView) findViewById(R.id.tvText);

        //list填充数据（如果是服务器接收的数据可动态填充）
        list.add("男");
        list.add("女");
        //适配器
        arr_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);

        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //加载spinner适配器
        spinner.setAdapter(arr_adapter);

        //Spinner 选择数据监听事件
       /* spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                spinnerText = arr_adapter.getItem(arg2).toString();
                text.setText(spinnerText);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                spinnerText = "";
                text.setText(spinnerText);
            }
        });*/
    }
    private void initContentView() {
        btnStart = findViewById(R.id.bt_tomodifyinformation);
        mTvMessage = findViewById(R.id.tv_1);
        btnStart.setOnClickListener(WowindowActivity.this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Log.i(TAG, "message is " + event.getMessage());
        // 更新界面
        mTvMessage.setText(event.getMessage());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_tomodifyinformation) {
            ModifyInformationActivity.start(this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}


