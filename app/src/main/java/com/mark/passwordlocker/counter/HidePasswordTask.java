package com.mark.passwordlocker.counter;

import android.os.Process;

import com.mark.passwordlocker.adapters.AccountsAdapter;
import com.mark.passwordlocker.helpers.ApplicationPreferences;

import java.lang.ref.WeakReference;

/**
 * Created by mark on 1/5/16.
 */
public class HidePasswordTask{
    private WeakReference<AccountsAdapter.VH> mViewHolder;
    private long mSeconds;
    private Runnable mRunnable;
    private Thread mCurrentThread;
    private static TaskManager mManager = TaskManager.getInstance();

    public HidePasswordTask(AccountsAdapter.VH viewHolder){
        setSeconds();
        mViewHolder = new WeakReference<>(viewHolder);
        mManager.insertTask(getRunnable(), mSeconds);
    }
    private void setSeconds(){
        mSeconds = ApplicationPreferences.getInstance().getSecondsToHidePassword();
    }


    private Runnable getRunnable(){
        if( null != mRunnable){
            return mRunnable;
        }
        mRunnable = new Runnable() {
            @Override
            public void run() {
                android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                mCurrentThread = Thread.currentThread(); //TODO why keep this??
                if( null != mViewHolder ){
                    mManager.handleMessage(HidePasswordTask.this);
                }
            }
        };
        return mRunnable;
    }

    AccountsAdapter.VH getViewHolder(){
        return mViewHolder.get();
    }

    private long getSeconds(){
        return mSeconds;
    }

    void recycle(){
//        if( null != mView){
//            mView.clear();
//            mView = null;
//        }
    }

}
