package mfy.com.simplerequest.http;

public class RequestManager {
    private static RequestManager instance = new RequestManager();

    private RequestManager() {
    }

    public static RequestManager getInstance() {
        return instance;
    }

    public void execute(HttpTask httpTask) {

    }
}
