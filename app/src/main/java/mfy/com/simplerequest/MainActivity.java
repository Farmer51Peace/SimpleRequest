package mfy.com.simplerequest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import mfy.com.simplerequest.http.SimpleRequest;
import mfy.com.simplerequest.http.interfaces.IDataListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 100; i++) {
            SimpleRequest.sendRequest(new DemoBean(), "http://app.shiqc.com/task/signIn/", new IDataListener<DemoResponse>() {
                @Override
                public void onSuccess(DemoResponse response) {
                    Log.e("mfy","success "+response.toString());
                    System.out.println(response.message);
                }

                @Override
                public void onFail() {
                    Log.e("mfy","onFail ");
                }
            });
        }

    }
}
