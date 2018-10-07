package mfy.com.simplerequest;

public class DemoResponse {
    public int  errno;
    public String message;
    public Object data;

    public DemoResponse() {
    }

    public DemoResponse(int errno, String message, Object data) {
        this.errno = errno;
        this.message = message;
        this.data = data;
    }

    public DemoResponse(int errno, String message) {
        this.errno = errno;
        this.message = message;
    }
}
