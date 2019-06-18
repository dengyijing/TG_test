package activitytest.example.com.tg_test;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;

import activitytest.example.com.tg_test.net.CallBackUtil;
import activitytest.example.com.tg_test.net.LogUtil;
import activitytest.example.com.tg_test.net.OkHttpUtil;
import activitytest.example.com.tg_test.net.Util;
import activitytest.example.com.tg_test.net.WaitDialog;
import okhttp3.MultipartBody;

public class NetTestActivity extends AppCompatActivity implements View.OnClickListener {
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private AlertDialog dialog;

    private WaitDialog waitDialog;

    private Button uploadBtn;
    private Button downloadBtn;
    private TextView showTv;

    private String mp3_url = "http://47.107.177.206/synthesis";
    private String img_url = "http://47.107.177.206/img_classify";
    private String img_mp3_url = "http://47.107.177.206/guide";

    private static final int UPDATE_UI = 0x0001;
    private static final int UPDATE_SHOW = 0x0002;
    private MyHandler myHandler = new MyHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadBtn = findViewById(R.id.id_upload_btn);
        downloadBtn = findViewById(R.id.id_download_btn);
        showTv = findViewById(R.id.id_msg_tv);
        uploadBtn.setOnClickListener((View.OnClickListener) this);
        downloadBtn.setOnClickListener((View.OnClickListener) this);

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取CallBackUtil
            int i = ContextCompat.checkSelfPermission(NetTestActivity.this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 提示用户应该去应用设置界面手动开启权限
                showDialogTipUserRequestPermission();
            }
        }

    }

    private static class MyHandler extends Handler{
        WeakReference<NetTestActivity> mActivityReference;

        MyHandler(NetTestActivity activity) {
            mActivityReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final NetTestActivity activity = mActivityReference.get();
            if (activity != null) {
                switch (msg.what){
                    case UPDATE_UI:
                        String result = (String) msg.obj;
                        activity.showTv.setText(result);
                        Toast.makeText(activity, "解析成功", Toast.LENGTH_SHORT).show();
                        break;
                    case UPDATE_SHOW:

                        break;
                    default:
                        break;
                }
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
                    Toast.makeText(this, "权限获取成功,现在可以保存深度图", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 0x1 && resultCode == 0) {

            Uri uri = data.getData();
            //uri转换成file
            String[] arr = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(uri, arr, null, null, null);
            int img_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String img_path = cursor.getString(img_index);
            File file = new File(img_path);
            LogUtil.d("文件长度：" + file.length() + "--: " + file.getName());

            if(waitDialog == null){
                waitDialog = new WaitDialog(NetTestActivity.this);
            }
            waitDialog.show();
            //上传数据
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            OkHttpUtil.uploadImg2mp3(img_mp3_url, "photo", builder, file,Util.getDownloadCachePath(), System.currentTimeMillis() + "_test.mp3",new CallBackUtil.IDownloadCallback() {
                @Override
                public void onDownloadSuccess(File file) {
                    LogUtil.d("下载成功");
                    if(waitDialog != null){
                        waitDialog.dismiss();
                    }
                    Looper.prepare();
                    Toast.makeText(NetTestActivity.this, "下载成功，文件路径：" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }

                @Override
                public void onDownloading(int progress) {
                    LogUtil.d("下载ing..." + progress);
                }

                @Override
                public void onDownloadFailed() {
                    if(waitDialog != null){
                        waitDialog.dismiss();
                    }
                    LogUtil.d("下载失败");
                }
            });



//            OkHttpUtil.uploadImg(img_url, "photo", builder, file, new CallBackUtil.IRequestCallback() {
//                @Override
//                public void success(Object o) {
//                    LogUtil.d("success");
//                    Gson gson = new Gson();
//                    ClassifyBean bean = gson.fromJson(o.toString(),ClassifyBean.class);
//                    String result = bean.getResult().get(0).getBaike_info().getDescription();
//                    LogUtil.d(result); //gson 解析
//                    Message message = Message.obtain();
//                    message.what = UPDATE_UI;
//                    message.obj = result;
//                    myHandler.sendMessage(message);
//                    if (waitDialog != null){
//                        waitDialog.dismiss();
//                    }
//                }
//                @Override
//                public void error(String msg) {
//                    if (waitDialog != null){
//                        waitDialog.dismiss();
//                    }
//                    Looper.prepare();
//                    Toast.makeText(MainActivity.this, "错误：" + msg, Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//                    LogUtil.d("error：" + msg);
//                }
//            });
        }
    }

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {
        new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("应用需要读取权限；\n否则，您将无法正常使用")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
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
                    } else {
                        //                        finish();
                    }
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
                .setMessage("请在-应用设置-权限-中，允许应用使用存储权限来保存用户数据")
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_upload_btn:
//                OkHttpUtil.uploadImg()
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 0x1);

                break;
            case R.id.id_download_btn:
                OkHttpUtil.getMP3File(mp3_url, Util.getDownloadCachePath(), System.currentTimeMillis() + "_test.mp3", "你好啊，猪宝宝", new CallBackUtil.IDownloadCallback() {
                    @Override
                    public void onDownloadSuccess(File file) {
                        Looper.prepare();
                        Toast.makeText(NetTestActivity.this, "下载成功，文件路径：" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        Looper.loop();
                        LogUtil.d("下载成功");
                    }

                    @Override
                    public void onDownloading(int progress) {
                        LogUtil.d("下载ing..." + progress);
                    }

                    @Override
                    public void onDownloadFailed() {
                        LogUtil.d("下载失败");
                    }
                });
                break;
            default:
                break;
        }
    }

}
