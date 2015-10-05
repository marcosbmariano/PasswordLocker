package com.example.mark.expandabletextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by mark on 1/15/15.
 */
public class ExpandableView extends RelativeLayout implements View.OnClickListener{

    private ImageView mIcon;
    private TextView mLabel;
    private TextView mText;
    private boolean mHideText;


    public ExpandableView(Context context, AttributeSet attr) {
        super(context, attr);
        mHideText = true;

        LayoutInflater inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(com.example.mark.expandabletextview.
                R.layout.expandabletextview,this);

        setupViews(v);
        setAttributes(context,attr);
        toggleText();



    }

    private void setAttributes(Context context, AttributeSet attr){
        String text, label;
        TypedArray a = context.getTheme().
                obtainStyledAttributes(attr, R.styleable.ExpandableView, 0, 0);

        try{
            label = a.getString(R.styleable.ExpandableView_LabelText);
            mLabel.setText(label);
            text = a.getString(R.styleable.ExpandableView_MainText);
            mText.setText(text);

        } finally {
            a.recycle();
        }


    }

    private void setupViews(View v){
        mIcon = (ImageView)v.findViewById(R.id.iv_icon);
        mIcon.setOnClickListener(this);
        mLabel = (TextView)v.findViewById(R.id.et_label);
        mLabel.setOnClickListener(this);
        mText = (TextView)v.findViewById( R.id.et_text);
    }





    @Override
    public boolean isInEditMode() {
        return super.isInEditMode();
    }

    @Override
    public void onClick(View v) {
        toggleText();
    }

    public void toggleText(){
        if (mHideText){
            mHideText = !mHideText;
            mText.setVisibility(GONE);
            mIcon.setImageResource(R.drawable.ic_closed1);
        }else{
            mHideText = !mHideText;
            mText.setVisibility(VISIBLE);
            mIcon.setImageResource(R.drawable.ic_open);

        }
    }


}
