package mfy.com.simplerequest.http;

import mfy.com.simplerequest.http.interfaces.IHttpService;

public class HttpTask implements Runnable {
    private IHttpService httpService;

    public IHttpService getHttpService() {
        return httpService;
    }

    public void setHttpService(IHttpService httpService) {
        this.httpService = httpService;
    }

    @Override
    public void run() {
        httpService.execute();
    }
}
