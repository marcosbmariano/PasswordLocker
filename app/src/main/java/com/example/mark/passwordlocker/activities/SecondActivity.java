package com.example.mark.passwordlocker.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.mark.passwordlocker.R;
import com.example.mark.passwordlocker.alerts.NewAccountDialog;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);




    }


    private void setupWidgets(){
        mToolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);

        mFloatingButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTransitionHelper.toggleScene();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.add_account_layout, new NewAccountDialog())
                        .commit();
            }
        });

    }





//
//    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//    setSupportActionBar(toolbar);
//
//    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//    fab.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//        }
//    });
//    getSupportActionBar().setDisplayHomeAsUpEnabled(true);



}
