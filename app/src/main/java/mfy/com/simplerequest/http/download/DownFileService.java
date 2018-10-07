package mfy.com.simplerequest.http.download;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import mfy.com.simplerequest.http.RequestHolder;
import mfy.com.simplerequest.http.download.interfaces.IDownloadCallable;
import mfy.com.simplerequest.http.interfaces.IHttpService;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownFileService implements IHttpService {

    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final OkHttpClient mOkHttpClient;
    private RequestHolder holder;
    private DownLoadItem downLoadItem;
    private IDownloadCallable iDownloadCallable;

    static {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false).build();
    }

    private File file;

    @Override
    public void setRequestHolder(RequestHolder holder) {
        this.holder = holder;
    }

    @Override
    public void execute() {
        if (holder != null) {
            downLoadItem = holder.getDownLoadItem();
            String url = downLoadItem.getUrl();
            iDownloadCallable = holder.getiDownloadCallable();

            /**
             * 得到已经下载的长度
             */
            file = new File(downLoadItem.getFilePath());
            final long breakPoint = file.length();
            Request request = null;
            Request.Builder builder = new Request.Builder();
            request = builder.url(url).build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream inputStream = response.body().byteStream();

                    long startTime = System.currentTimeMillis();
                    //用于计算每秒多少k
                    long speed = 0L;
                    //花费时间
                    long useTime = 0L;
                    //下载的长度
                    long getLen = 0L;
                    //接受的长度
                    long receiveLen = 0L;
                    boolean bufferLen = false;
                    //得到下载的长度
                    long dataLength = response.body().contentLength();
                    //单位时间下载的字节数
                    long calcSpeedLen = 0L;
                    //总数
                    long totalLength = breakPoint + dataLength;
                    //更新数量
                    receviceTotalLength(totalLength);
                    //更新状态
                    downloadStatusChange(DownloadStatus.downloading);
                    byte[] buffer = new byte[1024];
                    int count = 0;
                    long currentTime = System.currentTimeMillis();
                    BufferedOutputStream bos = null;
                    FileOutputStream fos = null;

                    try {
                        if (!makeDir(getFile().getParentFile())) {
                            iDownloadCallable.onDownloadError(downLoadItem, 1, "创建文件夹失败");
                        } else {
                            fos = new FileOutputStream(getFile(), true);
                            bos = new BufferedOutputStream(fos);
                            int length = 1;
                            while ((length = inputStream.read(buffer)) != -1) {
//                                if (getHttpService().isCancle()) {
//                                    iDownloadCallable.onDownloadError(downLoadItem, 1, "用户取消了");
//                                    return;
//                                }
//
//                                if (getHttpService().isPause()) {
//                                    iDownloadCallable.onDownloadError(downLoadItem, 2, "用户暂停了");
//                                    return;
//                                }

                                bos.write(buffer, 0, length);
                                getLen += (long) length;
                                receiveLen += (long) length;
                                calcSpeedLen += (long) length;
                                ++count;
                                if (receiveLen * 10L / totalLength >= 1L || count >= 5000) {
                                    currentTime = System.currentTimeMillis();
                                    useTime = currentTime - startTime;
                                    startTime = currentTime;
                                    speed = 1000L * calcSpeedLen / useTime;
                                    count = 0;
                                    calcSpeedLen = 0L;
                                    receiveLen = 0L;
                                    downloadLengthChange(breakPoint + getLen, totalLength, speed);
                                }
                            }
                            bos.close();
                            inputStream.close();
                            if (dataLength != getLen) {
                                iDownloadCallable.onDownloadError(downLoadItem, 3, "下载长度不相等");
                            } else {
                                downloadLengthChange(breakPoint + getLen, totalLength, speed);
                                iDownloadCallable.onDownloadSuccess(downLoadItem.copy());
                            }
                        }
                    } catch (IOException ioException) {
//                        if (this.getHttpService() != null) {
////                this.getHttpService().abortRequest();
//                        }
                        return;
                    } catch (Exception e) {
//                        if (this.getHttpService() != null) {
////                this.getHttpService().abortRequest();
//                        }
                    } finally {
                        try {
                            if (bos != null) {
                                bos.close();
                            }

                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        }

    }

    /* 创建文件夹的操作
     * @param parentFile
     * @return
     */
    private boolean makeDir(File parentFile) {
        return parentFile.exists() && !parentFile.isFile()
                ? parentFile.exists() && parentFile.isDirectory() :
                parentFile.mkdirs();
    }

    /**
     * 回调  长度的变化
     *
     * @param totalLength
     */
    private void receviceTotalLength(long totalLength) {
        downLoadItem.setCurrentLength(totalLength);
        final DownLoadItem copyDownloadItemInfo = downLoadItem.copy();
        if (iDownloadCallable != null) {
            synchronized (this.iDownloadCallable) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDownloadCallable.onTotalLengthReceived(copyDownloadItemInfo);
                    }
                });
            }
        }

    }

    /**
     * 更改下载时的状态
     *
     * @param downloading
     */
    private void downloadStatusChange(DownloadStatus downloading) {
        downLoadItem.setStatus(downloading);
        final DownLoadItem copyDownloadItemInfo = downLoadItem.copy();
        if (iDownloadCallable != null) {
            synchronized (this.iDownloadCallable) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDownloadCallable.onDownloadStatusChanged(copyDownloadItemInfo);
                    }
                });
            }
        }
    }

    public File getFile() {
        return file;
    }

    private void downloadLengthChange(final long downlength, final long totalLength, final long speed) {

        downLoadItem.setCurrentLength(downlength);
        if (iDownloadCallable != null) {
            final DownLoadItem copyDownItenIfo = downLoadItem.copy();
            synchronized (this.iDownloadCallable) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        iDownloadCallable.onCurrentSizeChanged(copyDownItenIfo, downlength / totalLength, speed);
                    }
                });
            }

        }

    }

}
