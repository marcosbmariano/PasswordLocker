package com.example.mark.passwordlocker.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mark.passwordlocker.AccountRecord;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.adapters.CustomAdapter;
import com.example.mark.passwordlocker.alerts.DeleteAccountAlert;
import com.example.mark.passwordlocker.alerts.DisplayPassDialog;
import com.example.mark.passwordlocker.helpers.DatabaseKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 1/13/15.
 */
public class AccountListFrag extends BaseFragment implements
        AdapterView.OnItemClickListener, AccountRecord.DatabaseListener,
        AdapterView.OnItemLongClickListener{

    private ListView mListView;
    private List<AccountRecord> mRecords;
    private List<String> mList;
    private DatabaseKey mDatabaseKey;
    private CustomAdapter mAdapter;


    public AccountListFrag(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.account_list_frag, container, false);
        mList = new ArrayList<>();
        setupViews(rootView);
        hideActionBar(false);
        AccountRecord.addListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateAccounts();
        mDatabaseKey = new DatabaseKey(getActivity());
        notifyDataChanged();
    }

    @Override
    public void notifyDataChanged() {
        mAdapter.addList(getCurrentAccountsList());
    }

    private List<String> getCurrentAccountsList(){
        List <String> result = new ArrayList<>();
        updateAccounts();
        for ( AccountRecord account : mRecords){
            result.add(account.getAccount(mDatabaseKey));
        }
        return result;
    }

    private void updateAccounts(){
        mRecords = AccountRecord.getAllAccounts();
    }

    private void setupViews(View v){
        mListView = (ListView)v.findViewById(R.id.lV_user_accounts);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        mAdapter = new CustomAdapter(getActivity(),
                mList, R.layout.list_item );

        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        DisplayPassDialog dialog = new DisplayPassDialog();
        Bundle args = new Bundle();

        args.putString(
                DisplayPassDialog.BUNDLE_PASSWORD,
                mRecords.get(position).getAccountPassword(mDatabaseKey));

        dialog.setArguments(args);
        dialog.show(getActivity().getSupportFragmentManager(), null);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle args = new Bundle();
        long accountId =  mRecords.get(position).getId();
        Log.e("Account position", " " + accountId);
        args.putLong(DeleteAccountAlert.ACCOUNT_ID, accountId);
        DeleteAccountAlert alert = new DeleteAccountAlert();
        alert.setArguments(args);
        alert.show(getActivity().getSupportFragmentManager(), null);

        return true;
    }
}
