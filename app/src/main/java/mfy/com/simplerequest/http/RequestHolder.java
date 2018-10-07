package mfy.com.simplerequest.http;

import mfy.com.simplerequest.http.download.DownLoadItem;
import mfy.com.simplerequest.http.download.interfaces.IDownloadCallable;
import mfy.com.simplerequest.http.interfaces.IDataListener;

public class RequestHolder<T> {
    private String url;
    private T requestBean;
    private IDataListener dataListener;
    private DownLoadItem downLoadItem;
    private IDownloadCallable iDownloadCallable;

    public IDownloadCallable getiDownloadCallable() {
        return iDownloadCallable;
    }

    public void setiDownloadCallable(IDownloadCallable iDownloadCallable) {
        this.iDownloadCallable = iDownloadCallable;
    }

    public DownLoadItem getDownLoadItem() {
        return downLoadItem;
    }

    public void setDownLoadItem(DownLoadItem downLoadItem) {
        this.downLoadItem = downLoadItem;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public T getRequestBean() {
        return requestBean;
    }

    public void setRequestBean(T requestBean) {
        this.requestBean = requestBean;
    }

    public IDataListener getDataListener() {
        return dataListener;
    }

    public void setDataListener(IDataListener dataListener) {
        this.dataListener = dataListener;
    }
}
