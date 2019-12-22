package com.example.abc.sigmak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import ezy.ui.view.RoundButton;

/**
 * Created by abc on 2019/12/21.
 */

public class User_Adapter extends BaseAdapter implements View.OnClickListener {
    private final Context mContext;
    private final List<User> mlist;
    private InnerItemOnclickListener mListener;

    public User_Adapter(Context mContext, List<User> mlist) {
        this.mContext = mContext;
        this.mlist = mlist;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mlist.get(i).userid;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final User_Adapter.ViewHolder viewHolder;
        if(view==null)
        {
            view= LayoutInflater.from(mContext).inflate(R.layout.user,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.name=(TextView)view.findViewById(R.id.username);
            viewHolder.photo=(RoundedImageView)view.findViewById(R.id.profile_photo);
            viewHolder.status=(RoundButton)view.findViewById(R.id.follow_status);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.status.setOnClickListener(this);
        User user=mlist.get(position);
        if(user.postion)
        {
            viewHolder.status.setText(R.string.Following);
            viewHolder.status.setPressed(true);
        }
        else
        {
            viewHolder.status.setText(R.string.Follow);
            viewHolder.status.setPressed(false);
        }
        viewHolder.photo.setImageBitmap(user.photo);
        viewHolder.name.setText(user.name);
        viewHolder.status.setTag(position);
        return view;
    }
    interface InnerItemOnclickListener {
        void itemClick(View v, int position);
    }
    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }
    @Override
    public void onClick(View view) {
        int position;
        position = (int) view.getTag();
        if(mListener!=null)
        {
            mListener.itemClick(view,position);
        }
    }

    private static class ViewHolder{
        RoundedImageView photo;
        TextView name;
        RoundButton status;
    }
    public void freshButton(int position)
    {
        User user=mlist.get(position);
        boolean flag;
        if(user.postion)
        {
            flag=false;
        }
        else
        {
            flag=true;
        }
        mlist.set(position,new User(user.userid,user.photo,user.name,flag));
        notifyDataSetChanged();
    }
}
