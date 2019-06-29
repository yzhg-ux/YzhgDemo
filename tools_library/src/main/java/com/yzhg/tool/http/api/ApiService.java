package com.yzhg.tool.http.api;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * 类 名: ApiService
 * 作 者: yzhg
 * 创 建: 2018/9/26 0026
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述:
 */
public interface ApiService {


    /**
     * 作 者: yzRxJava  onCompletehg
     * 历 史: (版本) 1.0
     * 描 述: RxJava方式请求
     */
    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> postRx(@Url String urlName, @FieldMap Map<String, Object> map);

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: RxJava get请求方式
     */
    @GET()
    Observable<ResponseBody> getRx(@Url String urlName, @QueryMap Map<String, Object> queryMap);

    @GET()
    Observable<ResponseBody> getRx(@Url String urlName);

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: RxJava 上传多张图片
     *
     * @param urlName 网络连接
     * @param params  图片集合
     */
    @Multipart
    @POST()
    Observable<ResponseBody> postImage(@Url String urlName, Map<String, RequestBody> params);

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 用户上传头像
     */
    @Multipart
    @POST()
    Observable<ResponseBody> uploadImage(
            @Url String urlName,
            @Part MultipartBody.Part file
    );

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST()
    Observable<ResponseBody> postPay(@Url String urlName, @Body RequestBody route);


    @FormUrlEncoded
    @POST()
    Call<String> getErpToken(@Url String urlName, Map<String, Object> params);

}











