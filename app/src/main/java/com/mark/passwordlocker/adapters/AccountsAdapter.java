package com.mark.passwordlocker.adapters;



import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.mark.passwordlocker.account.AccountRecord;
import com.mark.passwordlocker.R;
import com.mark.passwordlocker.alerts.DeleteAccountAlert;
import com.mark.passwordlocker.counter.ViewHolderTask;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mark on 7/14/15.
 */
public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.VH>
        implements AccountRecord.DatabaseObserver {
    private List<AccountRecord> mAccountsRecord;
    private List<String> mAccountsList;
    private final FragmentActivity mContext;
    private static AccountsAdapterUpdate mActivity;



    public AccountsAdapter(FragmentActivity context){
        mContext = context;
        mActivity = (AccountsAdapterUpdate)context;
        if ( null == context){
            throw new IllegalArgumentException("Context must not be null!");
        }

        AccountRecord.addObservers(this);
        updatedAccounts();
        mAccountsList = getCurrentAccountsList();

    }


    private List<String> getCurrentAccountsList(){
        List <String> result = new ArrayList<>();
        for ( AccountRecord account : mAccountsRecord){
            result.add(account.getAccount());
        }
        return result;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.account_item_recycler_view, parent, false);
        return new VH(itemView, mContext);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        AccountRecord account = mAccountsRecord.get(position);
        holder.rootView.setTag(account);
        holder.account.setText(mAccountsList.get(position));
        holder.setname(account.getAccount());
    }

    @Override
    public int getItemCount() {
        return mAccountsRecord.size();
    }

    @Override
    public void notifyDataChanged() {
        updatedAccounts();
        mAccountsList = getCurrentAccountsList();
        notifyDataSetChanged();
    }
    private void updatedAccounts(){
        mAccountsRecord = AccountRecord.getAllAccounts();
    }


    public static final class VH extends RecyclerView.ViewHolder {
        private ViewHolderTask mViewHolderTask;
        final View rootView;
        final ViewGroup itemLayout;
        final ImageButton btnCopyToClipBoard;
        final ImageButton btnViewPassword;
        final TextView account;
        final TextView passwordLabel;
        final TextView password;
        final FragmentActivity mActivity;
        final ViewGroup recyclerViewMainLayout;
        private boolean passwordVisible = false;


        //TODO remove this
        String name;

        public void setname(String name){
            this.name = name;
        }
        public String getname(){
            return name;
        }


        public VH(View itemView, final FragmentActivity context) {
            super(itemView);
            mActivity = context;
            rootView = itemView;
            itemLayout = (ViewGroup)itemView.findViewById(R.id.rl_recycler_item_layout);
            recyclerViewMainLayout = (ViewGroup) context.findViewById(R.id.rvAccounts);

            btnCopyToClipBoard = (ImageButton)itemView.findViewById(R.id.imBtnCopyClipboardPassword);
            btnViewPassword = (ImageButton)itemView.findViewById(R.id.imBtnViewPassword);
            account = (TextView) itemView.findViewById(R.id.tvAccount);
            password = (TextView) itemView.findViewById(R.id.tvPassword);
            password.setVisibility(View.GONE);
            passwordLabel = (TextView) itemView.findViewById(R.id.tvPasswordLabel);
            passwordLabel.setVisibility(View.GONE);

            rootView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    displayDeleteAlert( getAccountRecord().getAccount(),
                            getAccountRecord().getId());
                    return true;
                }
            });

            btnCopyToClipBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    copyPasswordToClipboard(getAccountRecord().getAccountPassword());
                    AccountsAdapter.mActivity.copyToClipboardPressed();
                }
            });

            btnViewPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    password.setText(getAccountRecord().getAccountPassword());
                    togglePasswordVisibility();
                }
            });
        }


        private void togglePasswordVisibility(){
            if ( passwordVisible){
                setPasswordInvisible();
                getViewHolderTask().cancelTask();
            } else{
                setPasswordVisible();
            }
        }

        private void setPasswordVisible(){
            passwordVisible = true;
            runTransition();
            password.setVisibility(View.VISIBLE);
            passwordLabel.setVisibility(View.VISIBLE);
            itemLayout.getLayoutParams().height = getHeightInDP(84);
            setTaskToHidePassword();
        }

        public void setPasswordInvisible(){
            if ( passwordVisible){
                passwordVisible = false;
                runTransition();
                itemLayout.getLayoutParams().height = getHeightInDP(48);//TODO // FIXME: 1/6/16
                password.setText(" ");
                password.setVisibility(View.GONE);
                passwordLabel.setVisibility(View.GONE);
            }
        }

        private int getHeightInDP(int pixels){
            final  float scale = mActivity.getApplicationContext()
                    .getResources().getDisplayMetrics().density;
            return (int) (pixels * scale + 0.5f);
        }

        private void runTransition(){
            if (Build.VERSION.SDK_INT >= 21 ){
                AutoTransition transition = new AutoTransition();
                transition.setDuration(100);
                TransitionManager.beginDelayedTransition(recyclerViewMainLayout, transition);
            }
        }

        private void setTaskToHidePassword(){
            getViewHolderTask().hidePassword();
        }

        private AccountRecord getAccountRecord(){
            return (AccountRecord)rootView.getTag();
        }

        private void displayDeleteAlert(String account, long id){
            Bundle args = new Bundle();
            args.putLong(DeleteAccountAlert.ACCOUNT_ID, id);
            args.putString(DeleteAccountAlert.ACCOUNT, account);
            DeleteAccountAlert alert = new DeleteAccountAlert();
            alert.setArguments(args);
            alert.show(mActivity.getSupportFragmentManager(), null);
        }

        private void copyPasswordToClipboard(String password) {
            ClipboardManager manager = (ClipboardManager) mActivity.
                    getSystemService(Context.CLIPBOARD_SERVICE);
            manager.setPrimaryClip( ClipData.newPlainText("", password));
            setTimerToClearClipboard();
        }

        private ViewHolderTask getViewHolderTask(){
            if( null != mViewHolderTask){
                return mViewHolderTask;
            }
            mViewHolderTask = new ViewHolderTask(this);
            return mViewHolderTask;
        }

        private void setTimerToClearClipboard(){
            getViewHolderTask().clearClipboard();
        }


        public final void clearClipBoard(){ //TODO test this
            ClipboardManager manager = (ClipboardManager) mActivity.
                    getSystemService(Context.CLIPBOARD_SERVICE);
            manager.setPrimaryClip(ClipData.newPlainText(" "," "));
        }
    }


    public interface AccountsAdapterUpdate{
        void copyToClipboardPressed();
    }
}
