package mfy.com.simplerequest.http.download;

import android.os.Environment;

import java.io.File;
import java.util.concurrent.FutureTask;

import mfy.com.simplerequest.http.HttpTask;
import mfy.com.simplerequest.http.RequestHolder;
import mfy.com.simplerequest.http.ThreadPoolManager;
import mfy.com.simplerequest.http.download.interfaces.IDownloadCallable;

public class DownFileManager implements IDownloadCallable {
    private static DownFileManager instance = new DownFileManager();
    private byte[] lock = new byte[0];

    private static DownFileManager getInstance() {
        return instance;
    }

    public void down(String url) {
        synchronized (lock) {
            String[] preFixs = url.split("/");
            String afterFix = preFixs[preFixs.length - 1];

            File file = new File(Environment.getExternalStorageDirectory(), afterFix);

            RequestHolder holder = new RequestHolder();

//            holder.setDataListener();
//
            DownLoadItem downLoadItem = new DownLoadItem(url, file.getAbsolutePath());
            holder.setDownLoadItem(downLoadItem);
            holder.setiDownloadCallable(this);

            HttpTask httpTask = new HttpTask();
            DownFileService downFileService = new DownFileService();
            downFileService.setRequestHolder(holder);
            httpTask.setHttpService(downFileService);
            try {
                ThreadPoolManager.getInstance().execute(new FutureTask<Object>(httpTask, null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

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
