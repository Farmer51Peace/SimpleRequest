package mfy.com.simplerequest.http.interfaces;

public interface IDataListener<M> {
    void onSuccess(M response);

    void onFail();
}