package mfy.com.simplerequest.http.download.interfaces;

import mfy.com.simplerequest.http.download.DownLoadItem;

public interface IDownloadCallable {
    void onDownloadStatusChanged(DownLoadItem downloadItemInfo);

    void onTotalLengthReceived(DownLoadItem downloadItemInfo);

    void onCurrentSizeChanged(DownLoadItem downloadItemInfo, double downLenth, long speed);

    void onDownloadSuccess(DownLoadItem downloadItemInfo);

    void onDownloadPause(DownLoadItem downloadItemInfo);

    void onDownloadError(DownLoadItem downloadItemInfo, int var2, String var3);
}
