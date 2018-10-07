package mfy.com.simplerequest.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import mfy.com.simplerequest.http.interfaces.IHttpService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JsonHttpService<T,M> implements IHttpService  {
    private Class clazz = null;
    public  JsonHttpService(Class clazz){
        this.clazz = clazz;
    }
    private RequestHolder holder;
    private static final OkHttpClient mOkHttpClient;
    //访问接口时 参数的上传格式
    private static final MediaType MEDIATYPE_STRING = MediaType.parse("text/plain; charset=utf-8");
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    static {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false).build();
    }

    @Override
    public void setRequestHolder(RequestHolder holder) {
        this.holder = holder;
    }

    @Override
    public void execute() {
        if (holder != null) {
            T requestBean = (T) holder.getRequestBean();
            if (requestBean != null) {
                final String jsonStr = JSON.toJSONString(requestBean);
                RequestBody requestBody = RequestBody.create(MEDIATYPE_STRING, jsonStr);
                Request request = new Request.Builder().url(holder.getUrl()).post(requestBody).build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.getDataListener().onFail();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String strResponse = response.body().string();
                        Log.e("mfy", strResponse + "----");


                        final M responseBean = (M) JSONObject.parseObject(strResponse, clazz);
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.getDataListener().onSuccess(responseBean);
                            }
                        });

                    }
                });
            }
        }
    }

}
