package com.example.abc.sigmak;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.abc.sigmak.MyClass.*;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class Answer_Adapter extends BaseAdapter {

    private final Context mContext;
    private final SparseBooleanArray mCollapsedStatus;
    private final String[] sampleStrings;
    com.example.abc.sigmak.MyClass.Question question;

    public Answer_Adapter(Context context) {
        mContext  = context;
        mCollapsedStatus = new SparseBooleanArray();
        sampleStrings = mContext.getResources().getStringArray(R.array.sample_names);
    }
    @Override
    public int getCount() {
        return sampleStrings.length;
    }

    @Override
    public Object getItem(int position) {
        return sampleStrings[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.answer, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.expandableTextView = (ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.expandableTextView.setText(sampleStrings[position], mCollapsedStatus, position);

        return convertView;
    }


    private static class ViewHolder{
        ExpandableTextView expandableTextView;
    }
}
