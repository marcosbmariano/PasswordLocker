package com.example.mark.passwordlocker.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mark.passwordlocker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mark on 1/6/15.
 */
public class CustomAdapter extends BaseAdapter {

    private Activity mActivity = null;
    private List<String> mList = null;
    private static LayoutInflater mInflater = null;
    public int mLayout;



    public CustomAdapter(Activity activity, List list, int layout ){
        mActivity = activity;
        mList = list;
        mLayout = layout;
        mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void addList(List<String> list){
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = getItem(position).toString();
        View view;
        TextView textView = null;

        if (convertView == null){
            view = mInflater.inflate(mLayout, null);

        }else{
            view = convertView;

        }
        textView = (TextView) view.findViewById(R.id.list_item_text);

        textView.setText(item);



        return view;
    }





}