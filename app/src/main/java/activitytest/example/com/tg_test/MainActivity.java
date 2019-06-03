package activitytest.example.com.tg_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class MainActivity extends AppCompatActivity {
    private Button  bt_share,bt_person,takephoto,bt_bluetooth;
    private static final int take_photo =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        takephoto = findViewById(R.id.bt_camera);
        //对照相功能的响应
        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TakePhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",take_photo);//传递参数id
                intent.putExtras(bundle);
                MainActivity.this.startActivity(intent);//启动新的intent
            }
        });
    }

    private void init() {
        bt_share = findViewById(R.id.bt_share);
        bt_person = findViewById(R.id.bt_person);
        bt_bluetooth=findViewById(R.id.bt_bluetooth);
        bt_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WowindowActivity.class);
                startActivity(intent);
            }
        });
        bt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SharingActivity.class);
                startActivity(intent);
            }
        });
        bt_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent =new Intent(MainActivity.this,BlueToothActivity.class);
                startActivity(intent);
            }
        });

    }
}
