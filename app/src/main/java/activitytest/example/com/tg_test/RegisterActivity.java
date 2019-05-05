package activitytest.example.com.tg_test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import activitytest.example.com.tg_test.util.Global;
import activitytest.example.com.tg_test.util.SpUtil;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private Button bt_return, bt_save;
    private EditText et_phonenumber, et_code;
    private int number,code, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bt_return = findViewById(R.id.bt_return);
        et_phonenumber = findViewById(R.id.et_phonenumber);
        et_code = findViewById(R.id.et_code);
        bt_save = findViewById(R.id.bt_save);

        bt_return.setOnClickListener(this);
        bt_save.setOnClickListener(this);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_save:
                saveInfo();
                break;
            case R.id.bt_return:
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void saveInfo() {
        number = Integer.parseInt(et_phonenumber.getText().toString());
        code = Integer.parseInt(et_code.getText().toString());
        //未初始化，会报空指针错误,关于sp存储的最好直接使用封装好的类
//        editor.putInt("number", number);
//        editor.putInt("code", code);
//        editor.commit();

        //全局变量，可以在全局根据关键字读写SP
        SpUtil.writeInt(Global.KEY_NUMBER,number);
        SpUtil.writeInt(Global.KEY_CODE,code);
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
    }
}
