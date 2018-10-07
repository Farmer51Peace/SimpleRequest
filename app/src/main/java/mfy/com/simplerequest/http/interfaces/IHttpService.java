package mfy.com.simplerequest.http.interfaces;

import mfy.com.simplerequest.http.RequestHolder;

public interface IHttpService {

    void setRequestHolder(RequestHolder holder);

    void execute();
}
