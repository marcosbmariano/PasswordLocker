package com.mark.passwordlocker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.mark.passwordlocker.R;
import com.mark.passwordlocker.helpers.ApplicationPassword;
import com.example.mark.passwordmanager.RawData;

/**
 * Created by mark on 1/21/15.
 */
public class NewUserPassFrag extends BaseFragment {
    private PassCreationFrag mFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_user_pass_frag,container, false);
        mFrag = new PassCreationFrag();
        mFrag.displayHint();
        setupWidgets(v);
        setupFragment(mFrag);

        return v;
    }

    private void setupWidgets(View v){

        v.findViewById(R.id.btn_new_user_save).setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFrag.isPasswordConfirmationValid()){
                    savePassword(mFrag.getRawPassword());

                    if(mFrag.hasHint()){
                        saveHint(mFrag.getEncodedHint());
                    }
                }else{
                    Toast.makeText(getActivity(), "Invalid Password!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void saveHint(String hint){
        ApplicationPassword password = ApplicationPassword.getInstance();
        password.saveHint(hint);
    }

    private void savePassword(RawData rawPassword){
        ApplicationPassword password = ApplicationPassword.getInstance();
        password.saveApplicationPassword(rawPassword.toString());
        updateFragment();
    }

    private void setupFragment(Fragment frag){
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.container_new_user_pass, frag)
                .commit();
    }

    private void updateFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainFragContainer, new AppPassEnterFrag())
                .commit();
    }

}
