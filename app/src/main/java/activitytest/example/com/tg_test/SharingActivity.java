package activitytest.example.com.tg_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SharingActivity extends AppCompatActivity {
    private Button bt_return,bt_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        bt_return = findViewById(R.id.bt_return);
        bt_share = findViewById(R.id.bt_share);
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SharingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        bt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SharingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
