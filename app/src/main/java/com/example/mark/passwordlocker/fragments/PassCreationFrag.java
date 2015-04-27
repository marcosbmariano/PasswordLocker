package com.example.mark.passwordlocker.fragments;



import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.helpers.PasswordManager;
import com.example.mark.passwordmanager.RawData;
import com.example.mark.passwordmanager.meter.PasswordMeter;

/**
 * Created by mark on 1/13/15.
 */
public class PassCreationFrag extends BaseFragment implements PasswordMeter.PasswordMeterListener {

    private EditText mEdTPassword;
    private EditText mEdTPassConfirmation;
    private EditText mEdTHint;
    private boolean mDisplayHint;
    private boolean mPasswordValid = false;
    private TextView mTVStrengthLabel;
    private ImageView mImgVPassCheck;
    private PasswordManager mPasswordManager;



    //TODO handle HINT

    public PassCreationFrag(){
        mDisplayHint = false;
    }

    public void displayHint(){
        mDisplayHint = true;
    }

    public boolean isHintDisplayed(){
        return mDisplayHint;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.app_pass_creation_frag, container, false);
        mPasswordManager = new PasswordManager();

        setupWidgets(v);

        return v;
    }

    private void setupWidgets(View v){
        mEdTPassword = (EditText)v.findViewById(R.id.eT_app_pass_crea_pass);
        mEdTPassword.addTextChangedListener(new StrengthChecker());

        mEdTPassConfirmation = (EditText)v.findViewById(R.id.eT_app_pass_crea_confirm);
        mEdTPassConfirmation.addTextChangedListener(new ConfirmationChecker());

        mTVStrengthLabel = (TextView)v.findViewById(R.id.tv_pass_creat_strength);
        mTVStrengthLabel.setVisibility(View.INVISIBLE);

        mImgVPassCheck = (ImageView)v.findViewById(R.id.iv_app_pass_crea_check);
        mImgVPassCheck.setVisibility(View.INVISIBLE);

        setupHint(v);

    }

    public void setPassword(RawData password){
        mEdTPassword.setText(password.toString());
        mEdTPassConfirmation.setText(password.toString());
    }

    private void setupHint(View v){
        mEdTHint = (EditText)v.findViewById(R.id.eT_app_pass_crea_hint);

        if (mDisplayHint){
            mEdTHint.setVisibility(View.VISIBLE);
        }else{
            mEdTHint.setVisibility(View.GONE);
        }

    }
    //this method returns a
    public String getEncodedHint(){
        if (hasHint()){
            return mEdTHint.getText().toString();
        }else{
            return "";
        }
    }

    public RawData getRawPassword(){
        if (isPasswordValid()){
            return new RawData(mEdTPassword.getText().toString());
        }else{

            return new RawData(" ");
        }
    }

    public boolean isPasswordValid(){
        return mPasswordValid;
    }


    public boolean hasHint(){
        return !mEdTHint.getText().toString().isEmpty();
    }

    // this control if the password confirmation matches
    private void setPasswordConfirmationLabel(){

        if( isPasswordConfirmationValid() ){
            mImgVPassCheck.setImageResource(R.drawable.ic_check);
            mPasswordValid = true;
        }else{
            mImgVPassCheck.setImageResource(R.drawable.ic_no_match);
            mPasswordValid = false;
        }

    }

    public boolean isPasswordConfirmationValid(){
        mImgVPassCheck.setVisibility(View.VISIBLE);
        return ( mPasswordManager.isEqualAndNotEmpty(mEdTPassword, mEdTPassConfirmation));
    }

    private void setPassStrengthLabel(EditText ed){
        mTVStrengthLabel.setVisibility(View.VISIBLE);
        checkPasswordStrength(ed);
    }

    private void checkPasswordStrength(EditText ed){
        RawData password = new RawData(ed.getText().toString());
        PasswordMeter meter = new PasswordMeter(this);
        meter.checkPasswordStrength(password);
        //the passwordMeter returns its result by calling the setStrength()
    }

    @Override
    public void setStrength(PasswordMeter.PasswordStrength strength) {
        switch (strength) {
            case INVALID:
                mPasswordValid = false;
                Log.e("Inside SetStrength", "INVALID");
                mTVStrengthLabel.setText(R.string.invalid);
                mTVStrengthLabel.setTextColor(Color.RED);
                break;
            case WEAK:
                mPasswordValid = true;
                Log.e("Inside SetStrength", "wEAK");
                mTVStrengthLabel.setText(R.string.weak);
                mTVStrengthLabel.setTextColor(Color.RED);
                break;
            case MODERATED:
                mPasswordValid = true;
                Log.e("Inside SetStrength", "MODERATED");
                mTVStrengthLabel.setText(R.string.poor);
                mTVStrengthLabel.setTextColor(getResources().getColor(R.color.orange));
                break;
            case GOOD:
                mPasswordValid = true;
                Log.e("Inside SetStrength", "GOOD");
                mTVStrengthLabel.setText(R.string.good);
                mTVStrengthLabel.setTextColor(Color.BLUE);

                break;
            case STRONG:
                mPasswordValid = true;
                Log.e("Inside SetStrength", "STRONG");
                mTVStrengthLabel.setText(R.string.strong);
                mTVStrengthLabel.setTextColor(Color.GREEN);
                break;
            default:
        }
    }




    class StrengthChecker implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {  }

        @Override
        public void afterTextChanged(Editable s) {
            setPasswordConfirmationLabel();
            setPassStrengthLabel(mEdTPassword);

        }
    }

    class ConfirmationChecker implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            setPasswordConfirmationLabel();

        }
    }




}
