package com.mark.passwordlocker.counter;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.mark.passwordlocker.adapters.AccountsAdapter;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//TODO clear all other tasks when the app is locked
/**
 * Created by mark on 1/5/16.
 */
public class TaskManager {
    private static TaskManager sInstance;
    private static int NUMBER_OF_CORES;
    private static final TimeUnit TIME_UNIT;
    private final Handler mHandler;
    private ScheduledThreadPoolExecutor mThreadPoolExecutor;

    static{
        NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        TIME_UNIT = TimeUnit.SECONDS;
        sInstance = new TaskManager();
    }

    private TaskManager(){
        mHandler = new MyHandler(Looper.getMainLooper());
        mThreadPoolExecutor = new ScheduledThreadPoolExecutor(NUMBER_OF_CORES);
    }

    public static TaskManager getInstance(){
        return sInstance;
    }

    ScheduledFuture insertTask(Runnable task, long delay){
       return mThreadPoolExecutor.schedule(task, delay, TIME_UNIT);
    }

    void handleMessage(ViewHolderTask task){
        mHandler.obtainMessage(0,task).sendToTarget();
    }

    void removeTask(Runnable task){
        if( mThreadPoolExecutor.getQueue().contains(task)){
            mThreadPoolExecutor.remove(task);
        }
    }

    private static class MyHandler extends Handler{

        MyHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            ViewHolderTask task = (ViewHolderTask)msg.obj;
            AccountsAdapter.VH viewHolder = task.getViewHolder();

            switch (task.getAction()){
                case ViewHolderTask.HIDE_PASSWORD:
                    viewHolder.setPasswordInvisible();
                    break;
                case ViewHolderTask.CLEAR_CLIPBOARD:
                    viewHolder.clearClipBoard();
                    default:
                        //do nothing
            }
        }
    }

    public void cancelAllTasks(){
        //mThreadPoolExecutor.
    }












}
