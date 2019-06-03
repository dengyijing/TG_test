package activitytest.example.com.tg_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BlueToothActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_post,btn_get,btn_Aget,btn_Apost;
    private TextView tv_read;
    private  int message;

    /*private static String mStrResponse;// 服务器返回的信息
    public Thread transmission = null;//网络访问线程
    private static Handler handler = new Handler();//用于将更新界面的线程发送到GUI线程的消息队列*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        btn_post = findViewById(R.id.btn_post);
        btn_get = findViewById(R.id.btn_get);
        btn_Apost=findViewById(R.id.btn_Apost);
        btn_Aget=findViewById(R.id.btn_Aget);
        tv_read =findViewById(R.id.tv_read);
        btn_Aget.setOnClickListener((View.OnClickListener) this);
        btn_Apost.setOnClickListener((View.OnClickListener) this);
        btn_post.setOnClickListener((View.OnClickListener) this);
        btn_get.setOnClickListener((View.OnClickListener) this);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_get:
                getRequest();
                break;

            case R.id.btn_post:
                postRequest();
                break;
            case R.id.btn_Aget:
                AgetRequest();
                break;
            case  R.id.btn_Apost:
                ApostRequest();
                break;

        }
    }
    /*异步无需手动创建线程，enqueue方法会自动将网络请求放在子线中执行*/

    private void ApostRequest() {
        final FormBody.Builder formbody = new FormBody.Builder();
             formbody.add("usename","guoguo");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://www.baidu.com")
                    .post(formbody.build())
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                        Log.d("kww1", "response.code()==" + response.code());
                        Log.d("kww1", "response.message==" + response.message());
                        message = response.code();
                       tv_read= (TextView) getText(message);
                    }

                }
            });

    }

    private void AgetRequest() {
        OkHttpClient client = new OkHttpClient();
        Request request =new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("kww1", "response.code()==" + response.code());
                Log.d("kww1", "response.message==" + response.message());
                Log.d("kww1", "res==" + response.body().string());

            }
        });

    }
/*同步要手动创建线程

 */
   private void getRequest() {

    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                Request request = new Request.Builder()
                        .url("http://www.baidu.com") //服务器接口
                        .build();//创建request对象
                Response response =null;
                response = client.newCall(request).execute();//回调并返回值
                if(response.isSuccessful()){
                    Log.d("kww1","response.code=="+response.code());//这里打印的code值是http协议默认的，而不是服务器设置的
                    Log.d("kww1","response.message=="+response.message());//这里打印的code值是服务器设置的
                    Log.d("kww1","res=="+response.body().string());//相当于输入流操作

                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }).start();
    }

    private void postRequest() {
        final FormBody.Builder formbody =new FormBody.Builder();//创建表单请求体，也是post方法与get方法的区别之处
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();//创建okkhttpclient对象
                    Request request = new Request.Builder()
                            .url("http://www.baidu.com")
                            .post(formbody.build())
                            .build();//创建request对象
                    Response response = null;
                    response=client.newCall(request).execute();
                    if (response.isSuccessful()){
                        Log.d("kww1","response.code=="+response.code());
                        Log.d("kww1","response.massge=="+response.message());
                        Log.d("kww1","res="+response.body().string());

                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

        //使用post方法传递参数
       /* btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transmission = new Thread(communicationWokerbyPost);
                transmission.start();//启动网络访问线程
            }
        });

    }

    //更新界面的Runnale对象
    private static Runnable RefreshGUI = new Runnable() {
        @Override
        public void run() {
            //将服务器返回的信息更新到界面的控件
            mtvResponse.setText(mStrResponse);
        }
    };
    private Runnable communicationWokerbyPost = new Runnable() {
        @Override
        public void run() {
            //访问的网络字符串
            String uriAPI = "";
            //建立Httppost联机
            HttpPost httpRequest = new HttpPost(uriAPI);
            //Post运行的传输变量必须用NameValuePair[]数组储存
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("msg", et_write.getText().toString()));
            JSONObject postData = new JSONObject();

            try {
                postData.put("supervisor", et_write.getEditableText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                httpRequest.setEntity(new StringEntity(postData.toString(), HTTP.UTF_8));

                //获得http的响应
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                //响应状态码为500表示服务器获得正常收到客户请求
                if (httpResponse.getStatusLine().getStatusCode() == 500)
                    //获取字符串
                    mStrResponse = EntityUtils.toString(httpResponse.getEntity());
                else
                    mStrResponse = "Response Erro:" + httpResponse.getStatusLine().toString();

            } catch (ClientProtocolException e) {
                mStrResponse = e.getMessage().toString();
            } catch (IOException e) {
                mStrResponse = e.getMessage().toString();
            } finally {
                handler.post(RefreshGUI);
                //使用Hander对象的POst方法将更新的界面操作线程发送给GUI线程的消息列队
            }
        }
    };*/







}
