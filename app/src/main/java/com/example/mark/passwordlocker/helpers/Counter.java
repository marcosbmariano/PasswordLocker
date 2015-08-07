package com.example.mark.passwordlocker.helpers;


import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by mark on 7/17/15.
 */
public class Counter {
    private CounterCallBack mObserver;
    private String mTag;
    private int mSeconds;

    public Counter( CounterCallBack observer, final int seconds){
        mSeconds = seconds;
        mObserver = observer;
    }

    public void startCounter(){
        Runnable runnable = getRunnable();
        new Thread(runnable).start();
    }

    private Runnable getRunnable(){
        return new Runnable() {
            @Override
            public void run() {
                try{
                    TimeUnit.SECONDS.sleep(mSeconds);
                    mObserver.calledByCounter(Counter.this);
                    Log.e("Counter", "lcalledByCoynter " + mSeconds);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public int getSeconds(){
        return mSeconds;
    }

    public void setTag(String tag){
        mTag = tag;
    }

    public String getTag(){
        if ( null == mTag){
            return "null";
        }
        return mTag;
    }

    public interface CounterCallBack{
        void calledByCounter(Counter counter);
    }
}
