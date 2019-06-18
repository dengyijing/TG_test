package activitytest.example.com.tg_test.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 用OkHttp封装的网络请求工具类
 * Created by Administrator on 2016/8/19.
 */
public class OkHttpUtil {

    //未带缓存的对象
    private static OkHttpClient client;

    //带缓存的对象
    private static OkHttpClient clientCache;

    //将对象单例
    public static OkHttpClient getInstance(){
        if(client == null){
            synchronized(OkHttpUtil.class){
                if(client == null){
                    client = new OkHttpClient();
                }
            }
        }
        return client;
    }

    //将对象单例
    /*public static OkHttpClient getInstanceCache(){
        if(clientCache == null){
            synchronized(OkHttpUtil.class){
                if(clientCache == null){
                    //缓存目录，缓存大小
                    Cache cache = new Cache(FileUtil.DIR_CACHE, 10 * 1024 * 1024);// 10 MiB
                    OkHttpClient.Builder builder = new OkHttpClient.Builder().cache(cache);
                    clientCache = builder.build();
                }
            }
        }
        return clientCache;
    }*/

    /**
     * ＧＥＴ请求,缓存json数据
     * @param url
     * @param callback
     */
    public static void doGetCache(String url, final CallBackUtil.IRequestCallback callback){
    //    getInstanceCache();
        Request request = new Request.Builder()
                .cacheControl(new CacheControl
                .Builder()
                .maxAge(1, TimeUnit.DAYS)
                .maxStale(1, TimeUnit.DAYS)//缓存时间为1天
                .build())
                .get()// get请求
                .url(url).build();
        Call call = clientCache.newCall(request);
        // 放在线程队列中去执行
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                if(callback != null){
                    callback.error(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                //将请求结果转换为字符串
                String result = body.string();
                LogUtil.w("请求成功 result = " + result);
                if(callback != null){
                    callback.success(result);
                }
            }
        });
    }

    /**
     * ＧＥＴ请求
     * @param url
     * @param callback
     */
    public static void doGet(String url, final CallBackUtil.IRequestCallback callback){
        LogUtil.d("url = " + url);
        getInstance();
        Request request = new Request.Builder().get()// get请求
                .url(url).build();
        Call call = client.newCall(request);
        // 放在线程队列中去执行
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                if(callback != null){
                    callback.error(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                //将请求结果转换为字符串
                String result = body.string();
                LogUtil.w("请求成功 result = " + result);
                if(callback != null){
                    callback.success(result);
                }
            }
        });
    }

    /**
     * ＰＯＳＴ请求
     *
     * @param url
     * @param requestBody 参数
     * @param callback
     */
    public static void doPost(String url, RequestBody requestBody, final CallBackUtil.IRequestCallback callback){
        LogUtil.d("url = " + url);
        getInstance();
        Request request = new Request.Builder().post(requestBody)// POST请求需要写入参数
                .url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                if(callback != null){
                    callback.error(e.getMessage());
                    LogUtil.w("请求错误");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String result = body.string();
                LogUtil.w("请求成功 result = " + result);
                if(callback != null){
                    callback.success(result);
                }
            }
        });
    }

    /**
     * ＰＯＳＴ请求，post Json数据
     * @param url
     * @param json     参数
     * @param callback
     */
    public static void doPostWithJson(String url, String json, final CallBackUtil.IRequestCallback callback){
        LogUtil.d("url = " + url);
        getInstance();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        // 放在线程队列中去执行
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                if(callback != null){
                    callback.error(e.getMessage());
                    LogUtil.w("请求失败");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String result = body.string();
                LogUtil.w("请求成功 result = " + result);
                if(callback != null){
                    callback.success(result);
                }
            }
        });
    }

    /**
     *  post 请求，上传图片到服务器
     * @param url
     * @param file
     * @param callback
     */
    public static void uploadImg(String url, String key, MultipartBody.Builder builder, File file, final CallBackUtil.IRequestCallback callback){
        LogUtil.d("url = " + url);
        getInstance();

        builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .post(requestBody)//添加请求体
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                if(callback != null){
                    callback.error(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String result = body.string();
                LogUtil.w("请求成功 result = " + result);
                if(callback != null){
                    callback.success(result);
                }
            }
        });
    }

    /**
     *  post 请求，上传图片到服务器获取mp3
     * @param url
     * @param file
     * @param callback
     */
    public static void uploadImg2mp3(String url, String key, MultipartBody.Builder builder, File file, final String saveDir, final String fileName,
                                     final CallBackUtil.IDownloadCallback callback){
        LogUtil.d("url = " + url);
        getInstance();

        builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(url)//地址
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .post(requestBody)//添加请求体
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                if(callback != null){
                    callback.onDownloadFailed();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[1024 * 8];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, fileName);
                    LogUtil.e("updata = " + file.getAbsolutePath());
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        if(callback != null){
                            callback.onDownloading(progress);
                        }
                    }
                    fos.flush();
                    // 下载完成
                    if(callback != null){
                        callback.onDownloadSuccess(file);
                    }
                } catch (Exception e) {
                    if(callback != null){
                        callback.onDownloadFailed();
                    }
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }


    /**
     * @param url 下载链接
     * @param saveDir 储存下载文件的SDCard目录
     * @param fileName 保存的文件名
     * @param callback 下载监听
     */
    public static void downloadFile(final String url, final String saveDir, final String fileName, final CallBackUtil.IDownloadCallback callback) {
        getInstance();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                if(callback != null){
                    callback.onDownloadFailed();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[1024 * 8];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, fileName);
                    LogUtil.e("updata = " + file.getAbsolutePath());
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        if(callback != null){
                            callback.onDownloading(progress);
                        }
                    }
                    fos.flush();
                    // 下载完成
                    if(callback != null){
                        callback.onDownloadSuccess(file);
                    }
                } catch (Exception e) {
                    if(callback != null){
                        callback.onDownloadFailed();
                    }
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }


    /**
     * @param url 下载链接
     * @param saveDir 储存下载文件的SDCard目录
     * @param fileName 保存的文件名
     * @param callback 下载监听
     */
    public static void getMP3File(final String url, final String saveDir, final String fileName, final String text, final CallBackUtil.IDownloadCallback callback) {
        getInstance();
        String urlStr = url + "?words=" + text;
        LogUtil.d(urlStr);
        Request request = new Request.Builder().url(urlStr).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                if(callback != null){
                    callback.onDownloadFailed();
                }
                LogUtil.d(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[1024 * 8];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, fileName);
                    LogUtil.e("updata = " + file.getAbsolutePath());
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        if(callback != null){
                            callback.onDownloading(progress);
                        }
                    }
                    fos.flush();
                    // 下载完成
                    if(callback != null){
                        callback.onDownloadSuccess(file);
                    }
                } catch (Exception e) {
                    if(callback != null){
                        callback.onDownloadFailed();
                    }
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private static String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        LogUtil.e("savePath = "+savePath);
        return savePath;
    }
}