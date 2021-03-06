package com.mark.passwordlocker.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mark.passwordlocker.R;
import com.mark.passwordlocker.adapters.AccountsAdapter;



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

        setupViews(rootView);
        return rootView;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//    }

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
