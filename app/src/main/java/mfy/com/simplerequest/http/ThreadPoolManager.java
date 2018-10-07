package mfy.com.simplerequest.http;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
    private static ThreadPoolManager instance = new ThreadPoolManager();
    private static ThreadPoolExecutor threadPoolExecutor;
    private LinkedBlockingQueue<Future<?>> taskQueue = new LinkedBlockingQueue<>();

    private ThreadPoolManager() {
        threadPoolExecutor = new ThreadPoolExecutor(4, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(4), handler);
        threadPoolExecutor.execute(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                FutureTask futureTask = null;
                try {
                    futureTask = (FutureTask) taskQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (futureTask != null) {
                    threadPoolExecutor.execute(futureTask);
                }
            }
        }
    };

    public static ThreadPoolManager getInstance() {
        return instance;
    }

    public void execute(FutureTask futureTask) throws InterruptedException {
        taskQueue.put(futureTask);
    }

    private RejectedExecutionHandler handler = new RejectedExecutionHandler() {

        @Override
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            try {
                taskQueue.put(new FutureTask<Object>(runnable, null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}
