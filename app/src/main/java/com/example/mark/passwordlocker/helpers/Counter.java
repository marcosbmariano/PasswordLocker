package com.example.mark.passwordlocker.helpers;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 7/17/15.
 */
public class Counter {
    private CounterCallBack mCalled;

    public Counter( CounterCallBack called){
        mCalled = called;
    }

    public void startCounter(final int seconds){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    TimeUnit.SECONDS.sleep(seconds);
                    Log.e("Counter", "Counter called after " + seconds);
                    mCalled.calledByCounter(seconds);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();

    }


    public interface CounterCallBack{
        void calledByCounter(int seconds);
    }
}
