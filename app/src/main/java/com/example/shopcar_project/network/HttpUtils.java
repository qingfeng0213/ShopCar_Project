package com.example.shopcar_project.network;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtils {
    private OkHttpClient client;
    private static volatile HttpUtils instance;
    public Handler handler = new Handler();
    private Interceptor getAppInterceptor(){
        //添加拦截器
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Log.e("++++++++++","拦截前");
                //---------请求之前------------
                Response response = chain.proceed(request);
                Log.e("++++++++++","拦截后");
                //---------请求之后------------
                return response;
            }
        };
        return interceptor;
    }
    private HttpUtils() {
        File file = new File(Environment.getExternalStorageDirectory(), "cache11");
        client = new OkHttpClient().newBuilder()
                .readTimeout(3000, TimeUnit.SECONDS)   //设置读取超时时间
                .connectTimeout(3000, TimeUnit.SECONDS) //设置连接的超时时间
                .addInterceptor(getAppInterceptor())//Application拦截器
                .cache(new Cache(file, 10 * 1024))
                .build();
    }
    //单例okhttp
    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if(null == instance) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }
    public void doGet(String url, final Class clazz, final NetCallBack netcallback) {
        //创建一个请求对象
        Request request = new Request.Builder().get().url(url).build();
        //创建一个call对象
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netcallback.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Gson gson = new Gson();
                final Object o = gson.fromJson(s, clazz);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netcallback.onSuccess(o);
                    }
                });
            }
        });
    }

    public void doPost(String url, final Class clazz, Map<String ,String > parms, final NetCallBack netCallBack){
        //不是FormBody，而是一个Builder
        FormBody.Builder body = new FormBody.Builder();
        //key  value
        for (String key:parms.keySet()) {
            //value的值
            body.add(key,parms.get(key));
        }
        //创建一个请求对象
        Request request = new Request.Builder().url(url).post(body.build()).build();
        //创建一个Call对象
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netCallBack.onFailure(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                Gson gson = new Gson();
                final Object oj = gson.fromJson(s, clazz);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        netCallBack.onSuccess(oj);
                    }
                });
            }
        });

    }


    public interface NetCallBack {
        void onSuccess(Object oj);

        void onFailure(Exception e);
    }

}
