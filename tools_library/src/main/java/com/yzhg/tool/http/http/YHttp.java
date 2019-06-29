package com.yzhg.tool.http.http;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.yzhg.tool.http.api.ApiService;
import com.yzhg.tool.http.tools.HttpCacheInterceptor;
import com.yzhg.tool.http.tools.ResponseLogInterceptor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

/**
 * 类 名: YHttp
 * 作 者: yzhg
 * 创 建: 2018/9/21 0021
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述: Retrofit + OkHttp 封装
 */
public class YHttp {

    private static String TAG = "--------------------  YHTTP --------------------";

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static String URL_HOME;

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 总连接
     */
    private String BASE_URL;

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 创建 读取时间和连接时间  默认30秒
     */
    private long readTimeOut;
    private long connectTimeOut;

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 设置缓存目录,以及缓存时间(默认24个小时)
     */
    private String cachePath;
    // private long cacheTime = 1024 * 1024 * 100;
    private long cacheTime;

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: url连接, 拼接在BaseUrl后面
     */
    private String url;
    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 添加请求参数
     */
    private Map<String, Object> params;


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 构建单利设计模式
     */
    @SuppressLint("StaticFieldLeak")
    private static YHttp instance = null;


    public static void initYHttp(Context context, String URL_HOME) {
        YHttp.context = context.getApplicationContext();
        YHttp.URL_HOME = URL_HOME;
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述:  获取单一实例
     */
    public static YHttp getInstance() {
        synchronized (YHttp.class) {
            if (instance == null) {
                instance = new YHttp();
            }
        }
        return instance;
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: YHttp 构造方法 ,  在这里复制初始化值
     */
    private YHttp() {
        this.BASE_URL = URL_HOME;
        this.readTimeOut = 60000;
        this.connectTimeOut = 60000;
        this.cachePath = "YHttpCache";
        this.cacheTime = 1024 * 1024 * 100;
        this.params = new HashMap<>();
    }


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 处理网络请求结果,返回解析好的对象给主线程  post请求
     *
     * @param urlName  网络请求连接
     * @param paramMap 网络请求参数
     */
/*    public <T> void requestPostRx(Observer<T> subscriber, Function<String, T> mapper, String urlName, Map<String, Object> paramMap) {
        Observable<T> observable = createRetrofit().create(ApiService.class).postRx(urlName, paramMap).map(new ResponseResultMapper()).map(mapper);
        toSubscribe(observable, subscriber, true);
    }*/
    public Observable<ResponseBody> requestPostRx(String urlName, Map<String, Object> paramMap) {
        return createRetrofit().create(ApiService.class).postRx(urlName, paramMap);
        // toSubscribe(observable, subscriber, true);
    }

    public Observable<ResponseBody> requestPostPayRx(String urlName, @Body RequestBody route) {
        return createRetrofit().create(ApiService.class).postPay(urlName, route);
        // toSubscribe(observable, subscriber, true);
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 处理网络请求结果,返回解析好的对象给主线程  get请求
     *
     * @param urlName  网络请求连接
     * @param paramMap 网络请求参数
     */
    public Observable<ResponseBody> requestGetRx(String urlName, Map<String, Object> paramMap) {
        return createRetrofit().create(ApiService.class).getRx(urlName, paramMap);
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 处理网络请求结果,返回解析好的对象给主线程  get请求
     *
     * @param urlName 网络请求连接
     */
    public Observable<ResponseBody> requestGetRx(String urlName) {
        return createRetrofit().create(ApiService.class).getRx(urlName);
    }


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 用户上传头像
     *
     * @param urlName 网络请求连接
     * @param outFile 图片File
     */
    public Observable<ResponseBody> uploadImage(String urlName, File outFile) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), outFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", outFile.getName(), requestBody);
        return createRetrofit().create(ApiService.class).uploadImage(urlName, body);
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 观察者启动器
     */
    private <T> void toSubscribe(Observable<T> o, Observer<T> s, boolean isMainThread) {
        Scheduler observeScheduler = Schedulers.io();
        if (isMainThread) observeScheduler = AndroidSchedulers.mainThread();
        o
                .subscribeOn(Schedulers.io()) //绑定IO线程
                .observeOn(observeScheduler)  //绑定主线程
                .subscribe(s);
    }


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 创建OkHttpClient
     */
    private Retrofit createRetrofit() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 创建OkHttpClient
     */
    private OkHttpClient getOkHttpClient() {
        File cacheFile = new File(context.getCacheDir(), cachePath);
        Cache cache = new Cache(cacheFile, cacheTime);
        OkHttpClient.Builder build = new OkHttpClient.Builder();
        build.addInterceptor(new ResponseLogInterceptor());
        build.addNetworkInterceptor(new HttpCacheInterceptor());
        build.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
        build.connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS);
        build.cache(cache);
        return build.build();
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: Builder 设计模式打造网络请求框架
     */
    public static final class Builder {
        //总连接
        private String baseUrl = "";
        //请求参数
        private Map<String, Object> params = new HashMap<>();
        //数据类型,默认json类型
        /*
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置超时时间  默认不设置为0
         */
        private long readTimeOut = 60000;
        private long connectTimeOut = 60000;

        /*
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置缓存目录,以及缓存时间(默认24个小时)
         */
        private String cachePath = "YHttpCache";
        private long cacheTime = 1024 * 1024 * 100;


        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置  总连接地址
         */
        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }


        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置网络请求参数
         */
        public Builder setParams(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 直接设置Map集合参数
         */
        public Builder setMapParams(Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }


        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置读取时间
         */
        public Builder setReadTimeOut(long readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置连接超时时间
         */
        public Builder setConnectTimeOut(long connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置网络读取时间和网络超时时间  优先级高
         *
         * @param readTimeOut    网络读取时间
         * @param connectTimeOut 网络超时时间
         */
        public Builder setReadAndConnectTimeOut(long readTimeOut, long connectTimeOut) {
            this.readTimeOut = readTimeOut;
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置缓存路径 , 设置缓存时间
         *
         * @param cachePath 缓存路径
         * @param cacheTime 缓存时间
         */
        public Builder setCachePath(String cachePath, long cacheTime) {
            this.cachePath = cachePath;
            this.cacheTime = cacheTime;
            return this;
        }


        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 给应用赋值
         */
        void applyConfig(YHttp yHttp) {
            if (!"".equals(baseUrl)) yHttp.BASE_URL = this.baseUrl;
            yHttp.params = this.params;  //设置参数
            if (this.readTimeOut != 0) yHttp.readTimeOut = this.readTimeOut; //设置读取时间
            if (this.connectTimeOut != 0) yHttp.connectTimeOut = this.connectTimeOut;  //设置网络连接时间
            if (!"".equals(this.cachePath)) yHttp.cachePath = this.cachePath;  //设置缓存路径
            if (0 != this.cacheTime) yHttp.cacheTime = this.cacheTime;  //设置缓存时间
        }

        public YHttp build() {
            YHttp yHttp = YHttp.getInstance();
            applyConfig(yHttp);
            return yHttp;
        }
    }
}







