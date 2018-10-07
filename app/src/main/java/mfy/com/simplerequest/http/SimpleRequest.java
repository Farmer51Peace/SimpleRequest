package mfy.com.simplerequest.http;

import java.lang.reflect.ParameterizedType;
import java.util.concurrent.FutureTask;

import mfy.com.simplerequest.http.interfaces.IDataListener;
import mfy.com.simplerequest.http.interfaces.IHttpService;

public class SimpleRequest {

    public static <T, M> void sendRequest(T requestBean, String url, IDataListener<M> dataListener) {
        RequestHolder<T> requestHolder = new RequestHolder<>();
        requestHolder.setUrl(url);
        requestHolder.setRequestBean(requestBean);
        requestHolder.setDataListener(dataListener);

        Class<? super M> responseType = getResponseType(dataListener);

        IHttpService<T, M> httpService = new JsonHttpService<T, M>(responseType);
        httpService.setRequestHolder(requestHolder);

        HttpTask httpTask = new HttpTask();
        httpTask.setHttpService(httpService);

        try {
            ThreadPoolManager.getInstance().execute(new FutureTask(httpTask, null));
        } catch (InterruptedException e) {
            dataListener.onFail();
        }
    }

    private static <M> Class<? super M> getResponseType(IDataListener<M> dataListener) {
        if (dataListener != null) {
            ParameterizedType mySuperClass = (ParameterizedType) dataListener.getClass().getGenericInterfaces()[0];
            return (Class<? super M>) mySuperClass.getActualTypeArguments()[0];
        }
        return null;
    }
}
