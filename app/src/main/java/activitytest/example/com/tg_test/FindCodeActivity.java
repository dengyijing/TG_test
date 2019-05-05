package activitytest.example.com.tg_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class FindCodeActivity extends AppCompatActivity {
    private Button bt_confirm;
    private  Button bt_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_code);
        bt_confirm = findViewById(R.id.bt_confirm);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindCodeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        bt_return = findViewById(R.id.bt_return);
        bt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindCodeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
