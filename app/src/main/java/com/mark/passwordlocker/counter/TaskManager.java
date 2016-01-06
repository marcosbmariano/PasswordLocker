package com.mark.passwordlocker.counter;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mark.passwordlocker.adapters.AccountsAdapter;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by mark on 1/5/16.
 */
public class TaskManager {
    private static TaskManager sInstance;
    private static int NUMBER_OF_CORES;
    private static final TimeUnit TIME_UNIT;
    private final Handler mHandler;
    private ScheduledThreadPoolExecutor mTheadPoolExecutor;

    static{
        NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        TIME_UNIT = TimeUnit.SECONDS;
        sInstance = new TaskManager();
    }

    private TaskManager(){
        mHandler = new MyHandler(Looper.getMainLooper());
        setTheadPoolExecutor();
    }

    private void setTheadPoolExecutor(){
        mTheadPoolExecutor = new ScheduledThreadPoolExecutor(NUMBER_OF_CORES);
    }

    public static TaskManager getInstance(){
        return sInstance;
    }

    void insertTask(Runnable task, long delay){
        mTheadPoolExecutor.schedule(task, delay, TIME_UNIT);
    }

    void handleMessage(HidePasswordTask task){
        mHandler.obtainMessage(0,task).sendToTarget();
    }

    void removeTask(Runnable task){
        if( mTheadPoolExecutor.getQueue().contains(task)){
            mTheadPoolExecutor.remove(task);
        }
    }

    private static class MyHandler extends Handler{

        MyHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            HidePasswordTask task = (HidePasswordTask)msg.obj;
            AccountsAdapter.VH viewHolder = task.getViewHolder();
            viewHolder.setPasswordInvisible();
        }
    }












}
