package activitytest.example.com.tg_test;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BlueToothActivity extends AppCompatActivity  {
    private Button btn_post;
    private static String TAG = "kww1";
    /*handler主要根据传递的message中的what字段进行区分，
     我们一般需要提前声明一系列相关的字段，如UPDATE_IMG：将我之前写好的静态类的结构中activity换成我们当前activity*/
    private static final int UPDATE_IMG = 0x0001;
    private MyHandler myHandler = new MyHandler(this);  //实例化handler
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE}; // 要申请的权限
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_tooth);
        btn_post= findViewById(R.id.btn_post);
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequest();
            }
        });
        /*动态权限申请
          版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
          权限是否已经 授权 GRANTED---授权  DINIED---拒绝
          如果没有授予该权限，就去提示用户请求
          */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        showDialogTipUserRequestPermission();
                    }
                }
            }

            // 提示用户该请求权限的弹出框
            private void showDialogTipUserRequestPermission() {

                new AlertDialog.Builder(this)
                        .setTitle("存储权限不可用")
                        .setMessage("由于旅行吧需要获取存储空间的文件；\n否则，您将无法正常使用旅行吧")
                        .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startRequestPermission();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setCancelable(false).show();
            }

            // 开始提交请求权限
            private void startRequestPermission() {
                ActivityCompat.requestPermissions(this, permissions, 321);
            }

            // 用户权限 申请 的回调方法
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

                if (requestCode == 321) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                            // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                            boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                            if (!b) {
                                // 用户还是想用我的 APP 的
                                // 提示用户去应用设置界面手动开启权限
                                showDialogTipUserGoToAppSettting();
                            } else
                                finish();
                        } else {
                            Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            // 提示用户去应用设置界面手动开启权限

            private void showDialogTipUserGoToAppSettting() {

                dialog = new AlertDialog.Builder(this)
                        .setTitle("存储权限不可用")
                        .setMessage("请在-应用设置-权限-中，允许旅行吧使用存储权限来保存用户数据")
                        .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 跳转到应用设置界面
                                goToAppSetting();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).setCancelable(false).show();
            }

            // 跳转到当前应用的设置界面
            private void goToAppSetting() {
                Intent intent = new Intent();

                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);

                startActivityForResult(intent, 123);
            }

            //
            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 123) {

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // 检查该权限是否已经获取
                        int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                        // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                        if (i != PackageManager.PERMISSION_GRANTED) {
                            // 提示用户应该去应用设置界面手动开启权限
                            showDialogTipUserGoToAppSettting();
                        } else {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }



    /*请求网络

     */
    private void postRequest() {
        OkHttpClient client = new OkHttpClient();
        /* post上传文件*/
        MediaType fileType = MediaType.parse("File/*");//数据类型为json格式，
        File file = new File("/storage/emulated/0/DCIM/Camera/IMG_20190609_150532.jpg");//file对象.
        RequestBody body = RequestBody.create(fileType , file );
        Request request = new Request.Builder()
                .url("https://  www.smartglass.site")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            //这里是异步操作的回调，成功或者失败，会分别调用onFailure或者onSuccess
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("kww1", "response.code()==" + response.code());
                Log.d("kww1", "response.message==" + response.message());
                /*下载文件

                 */
                try {
                    InputStream is = response.body().byteStream();//从服务器得到输入流对象
                    long sum = 0;
                    String mdestFileName = "/storage/emulated/0/";
                    File dir = new File(mdestFileName);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, mdestFileName);//根据目录和文件名得到file对象
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buf = new byte[1024 * 8];
                    int len = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    return;
                }catch (Exception e){
                    Log.e(TAG,e.toString());
                }


                /*这里是成功的回调，异步操作是在子线程，无法直接进行ui更新，所以我们需要使用到handler这个线程间通信的机制
                 将子线程的数据通过通信返回到主线程，并进行UI相关更新
                 先实例化一个静态内部类handler，有简单版本的handler不过会存在内存泄漏的危险
                 上面已经完成了handler的实例化，在这里可以使用hanler传递
                 首先创建一个需要传递的message*/
                Message message = Message.obtain();
                message.what = UPDATE_IMG;  //这个就是判断消息来源的字段
                //这里可以携带其他数据，比如两个定义好的int ，或者obj对象类型
                message.obj = new String("你好啊");
                myHandler.sendMessage(message);  //发送完毕
            }
        });

    }


    //这里新建一个类MyHandler 继承 Handler，并重写handlerMessage方法，使用弱引用声明activity，以便容易被回收掉
    private static class MyHandler extends Handler {
        WeakReference<BlueToothActivity> mActivityReference;

        MyHandler(BlueToothActivity activity) {
            mActivityReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final BlueToothActivity activity = mActivityReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case UPDATE_IMG:
                        //这里是运行在主线程，所以一切UI可以在这里操作
                        String str = (String) msg.obj;
                        Toast.makeText(activity, "收到收到" + str, Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        }
    }






















    /*public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get:
                //这个是同步
                getRequest();
                break;

            case R.id.btn_post:
                postRequest();
                break;
            case R.id.btn_Aget:
                //这个是异步操作
                AgetRequest();
                break;
            case R.id.btn_Apost:
                ApostRequest();
                break;

        }
    }*/
    /*异步无需手动创建线程，enqueue方法会自动将网络请求放在子线中执行
    * post方法不常用，会比get耗时*/

   /* private void ApostRequest() {
        final FormBody.Builder formbody = new FormBody.Builder();
        formbody.add("usename", "guoguo");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .post(formbody.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("kww1", "response.code()==" + response.code());
                    Log.d("kww1", "response.message==" + response.message());
                    Log.d("kww1", "res==" + response.body().toString());
                }

            }
        });

    }*/

    private void AgetRequest() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .build();
        client.newCall(request).enqueue(new Callback() {
            //这里是异步操作的回调，成功或者失败，会分别调用onFailure或者onSuccess
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("kww1", "response.code()==" + response.code());
                Log.d("kww1", "response.message==" + response.message());
                Log.d("kww1", "res==" + response.body().string());
                /*成功的回调，异步操作是在子线程，无法直接进行ui更新，需要使用handler这个线程间通信的机制
                将子线程的数据通过通信返回到主线程，并进行UI相关更新
                先实例化一个静态内部类handler，有简单版本的handler不过会存在内存泄漏的危险
                上面已经完成了handler的实例化，在这里可以使用hanler传递
                首先创建一个需要传递的message*/
                Message message = Message.obtain();
                message.what = UPDATE_IMG;  //这个就是判断消息来源的字段
                //这里可以携带其他数据，比如两个定义好的int ，或者obj对象类型
                message.obj = new String("你好啊");
                myHandler.sendMessage(message);  //发送完毕
            }
        });

    }
}
/*同步要手动创建线程

 */
  /* private void getRequest() {

    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                Request request = new Request.Builder()
                        .url("https://www.baidu.com") //服务器接口
                        .build();//创建request对象
                Response response =null;
                response = client.newCall(request).execute();//回调并返回值
                Handler handler =new Handler(Looper.getMainLooper());
                Log.d(TAG, "run: test");
                if(response.isSuccessful()){
                    Log.d("kww1","response.code=="+response.code());//这里打印的code值是http协议默认的，而不是服务器设置的
                    Log.d("kww1","response.message=="+response.message());//这里打印的code值是服务器设置的
                    Log.d("kww1","res=="+response.body().string());//相当于输入流操作

                }else {
                    Log.d(TAG, "run: " + response.body().toString());
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
                            .url("https://www.baidu.com")
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
    }*/

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








