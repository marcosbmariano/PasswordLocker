package com.example.mark.passwordlocker.adapters;



import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.mark.passwordlocker.AccountRecord;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.alerts.DeleteAccountAlert;
import com.example.mark.passwordlocker.helpers.ApplicationPreferences;
import com.example.mark.passwordlocker.helpers.Counter;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by mark on 7/14/15.
 */
public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.VH>
        implements AccountRecord.DatabaseListener {
    private static List<AccountRecord> mAccountsRecord;
    private static List<String> mAccountsList;
    private FragmentActivity mContext;
    private static AccountsAdapterUpdate mActivity;


    public AccountsAdapter( FragmentActivity context){
        mContext = context;
        mActivity = (AccountsAdapterUpdate)context;
        if ( null == context){
            throw new IllegalArgumentException("Context must not be null!");
        }
        AccountRecord.addListener(this);
        updatedAccounts();
        mAccountsList = getCurrentAccountsList();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.account_item_recycler_view, parent, false);
        return new VH(itemView, mContext);
    }

    private List<String> getCurrentAccountsList(){
        List <String> result = new ArrayList<>();
        for ( AccountRecord account : mAccountsRecord){
            result.add(account.getAccount());
        }
        return result;
    }

    private void updatedAccounts(){
        mAccountsRecord = AccountRecord.getAllAccounts();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        AccountRecord account = mAccountsRecord.get(position);
        holder.rootView.setTag(account);
        holder.account.setText(mAccountsList.get(position));
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


    public static final class VH extends RecyclerView.ViewHolder implements Counter.CounterCallBack{
        final static private ApplicationPreferences mAppPreferences
                = ApplicationPreferences.getInstance();
        private static final String PASSWORD_VISIBLE_TAG = "passwordVisible";
        private static final String CLEAR_CLIPBOARD_TAG = "clearClipboard";
        final View rootView;
        final ImageButton btnCopyToClipBoard;
        final ImageButton btnViewPassword;
        final TextView account;
        final TextView password;
        final FragmentActivity mActivity;
        private boolean passwordVisible = false;


        public VH(View itemView, final FragmentActivity context) {
            super(itemView);
            mActivity = context;
            rootView = itemView;
            btnCopyToClipBoard = (ImageButton)itemView.findViewById(R.id.imBtnCopyClipboardPassword);
            btnViewPassword = (ImageButton)itemView.findViewById(R.id.imBtnViewPassword);
            account = (TextView) itemView.findViewById(R.id.tvAccount);
            password = (TextView) itemView.findViewById(R.id.tvPassword);
            password.setVisibility(View.GONE);

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
                    AccountsAdapter.mActivity.callOnBackPressed();
                }
            });

            btnViewPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    password.setText(getAccountRecord().getAccountPassword());
                    toglePasswordVisibility();
                }
            });
        }

        private void toglePasswordVisibility(){
            if ( passwordVisible){
                setPasswordInvisible();
            } else{
                setPasswordVisible();
            }
        }

        private void setPasswordVisible(){
            passwordVisible = true;
            password.setVisibility(View.VISIBLE);
            setCounterToHidePassword();
        }

        private void setPasswordInvisible(){
            if ( passwordVisible){
                passwordVisible = false;
                password.setText(" ");
                password.setVisibility(View.GONE);
            }
        }

        private void setCounterToHidePassword(){
            Counter counter = new Counter(this, mAppPreferences.getSecondsToHidePassword());
            counter.setTag(PASSWORD_VISIBLE_TAG);
            counter.startCounter();
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
            Counter counter = new Counter(this, mAppPreferences.getClipBoardSeconds());
            counter.setTag(CLEAR_CLIPBOARD_TAG);
            counter.startCounter();
        }

        @Override
        public void calledByCounter(Counter counter) {
            String tag = counter.getTag();
            switch (tag){
                case CLEAR_CLIPBOARD_TAG:
                    cleanClipBoard();
                    break;
                case PASSWORD_VISIBLE_TAG:
                    hidePassword();
                    break;
                default:
            }
        }

        private void hidePassword(){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setPasswordInvisible();
                }
            });
        }

        private void cleanClipBoard(){
            ClipboardManager manager = (ClipboardManager) mActivity.
                    getSystemService(Context.CLIPBOARD_SERVICE);
            manager.setPrimaryClip(ClipData.newPlainText(" "," "));
        }
    }


    public interface AccountsAdapterUpdate{
        void callOnBackPressed();
    }
}
