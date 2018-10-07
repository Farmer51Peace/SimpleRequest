package mfy.com.simplerequest.http.download.interfaces;

import mfy.com.simplerequest.http.download.DownLoadItem;

public class DownLoadCallableAdapter implements IDownloadCallable {
    @Override
    public void onDownloadStatusChanged(DownLoadItem downloadItemInfo) {

    }

    @Override
    public void onTotalLengthReceived(DownLoadItem downloadItemInfo) {

    }

    @Override
    public void onCurrentSizeChanged(DownLoadItem downloadItemInfo, double downLenth, long speed) {

    }

    @Override
    public void onDownloadSuccess(DownLoadItem downloadItemInfo) {

    }

    @Override
    public void onDownloadPause(DownLoadItem downloadItemInfo) {

    }

    @Override
    public void onDownloadError(DownLoadItem downloadItemInfo, int var2, String var3) {

    }
}
