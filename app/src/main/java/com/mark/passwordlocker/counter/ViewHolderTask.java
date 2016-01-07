package com.mark.passwordlocker.counter;

import android.os.Process;
import android.util.Log;

import com.mark.passwordlocker.adapters.AccountsAdapter;
import com.mark.passwordlocker.helpers.ApplicationPreferences;

import java.lang.ref.WeakReference;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by mark on 1/5/16.
 */
public class ViewHolderTask {
    public static final int HIDE_PASSWORD = 1;
    public static final int CLEAR_CLIPBOARD = 2;
    private WeakReference<AccountsAdapter.VH> mViewHolder;
    private int mAction;
    private long mSeconds;
    private boolean taskIsSet = false;
    private Runnable mRunnable;
    private Thread mCurrentThread;
    private static TaskManager mManager = TaskManager.getInstance();
    private ScheduledFuture mThisScheduledTask;

    public ViewHolderTask(AccountsAdapter.VH viewHolder){
        setSeconds();
        mViewHolder = new WeakReference<>(viewHolder);
    }

    public void hidePassword(){
        mAction = HIDE_PASSWORD;
        mSeconds = ApplicationPreferences.getInstance().getSecondsToHidePassword();
        checkIfTaskIsSet();
    }

    public void clearClipboard(){
        mAction = CLEAR_CLIPBOARD;
        mSeconds = ApplicationPreferences.getInstance().getClipBoardSeconds();
        checkIfTaskIsSet();
    }

    private void checkIfTaskIsSet(){
        if(taskIsSet){
            updateTask();
        }else {
            taskIsSet = true;
            insertTask();
        }
    }

    private void insertTask(){
        mThisScheduledTask = mManager.insertTask(getRunnable(), mSeconds);
    }

    private void updateTask(){
        cancelTask();
        insertTask();
    }

    public void cancelTask(){
        mThisScheduledTask.cancel(true);
        mManager.removeTask(mRunnable);
        synchronized (this){
            if( null != mCurrentThread){
                mCurrentThread.interrupt();
                mCurrentThread = null;
            }
        }
    }

    private void setSeconds(){
        mSeconds = ApplicationPreferences.getInstance().getSecondsToHidePassword();
    }

    public int getAction(){
        return mAction;
    }
    public String getName(){
       return mViewHolder.get().getname();
    }


    private Runnable getRunnable(){
        if( null != mRunnable){
            return mRunnable;
        }
        mRunnable = new Runnable() {

            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                mCurrentThread = Thread.currentThread();
                if( null != mViewHolder ){
                    mManager.handleMessage(ViewHolderTask.this);
                }
            }
        };
        return mRunnable;
    }

    AccountsAdapter.VH getViewHolder(){
        return mViewHolder.get();
    }

    void recycle(){
//        if( null != mView){
//            mView.clear();
//            mView = null;
//        }
    }

}
