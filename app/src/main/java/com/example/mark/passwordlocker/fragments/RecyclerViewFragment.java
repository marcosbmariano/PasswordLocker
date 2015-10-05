package com.example.mark.passwordlocker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.adapters.AccountsAdapter;


/**
 * Created by mark on 7/14/15.
 */
public class RecyclerViewFragment extends BaseFragment {
    private AccountsAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                            ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recycler_view_layout, container, false);
        hideActionBar(false);
        setupViews(rootView);
        return rootView;
    }

    private void setupViews(View v){
        final RecyclerView rvAccounts = (RecyclerView)v.findViewById(R.id.rvAccounts);
        rvAccounts.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        rvAccounts.setLayoutManager(layoutManager);
        rvAccounts.setAdapter(getAdapter());
    }

    private AccountsAdapter getAdapter(){
        if ( null == mAdapter) {
            mAdapter = new AccountsAdapter(getActivity());
        }
        return mAdapter;
    }

}
