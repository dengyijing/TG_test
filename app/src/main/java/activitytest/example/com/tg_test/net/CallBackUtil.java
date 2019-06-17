package com.example.testnet;

import android.os.AsyncTask;

import java.io.File;

/**
 * Created by zhuyaoting on 2018/5/17.
 */
public class CallBackUtil extends AsyncTask<Void, Void, Object> {
    private IRequestCallback callback;
    private IRequest request;

    //用于封装 HttpURLConnection 的请求回调
    public CallBackUtil(IRequest request, IRequestCallback callback){
        if(request == null || callback == null){
            throw new NullPointerException("IRequest or IRequestCallback can not be null");
        }

        this.request = request;
        this.callback = callback;
    }


    @Override
    protected Object doInBackground(Void... params){
        return request.request();
    }

    @Override
    protected void onPostExecute(Object o){
        if(o == null){
            callback.error("请求失败了");

        }else {
            callback.success(o);
        }
    }

    /**
     * 请求的接口
     */
    public interface IRequest{

        /**
         * 发起网络请求的操作
         */
        Object request();

    }

    /**
     * 请求的接口回调
     */
    public interface IRequestCallback{
        //请求成功
        void success(Object o);
        //请求失败
        void error(String msg);
    }
    /**
     *下载进度监听
     */
    public interface IDownloadCallback {
        /**
         * 下载成功
         */
        void onDownloadSuccess(File file);

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }

}
