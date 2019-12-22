package com.example.abc.sigmak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.like.LikeButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by abc on 2019/12/21.
 */

public class Homepage_Adapter extends BaseAdapter implements View.OnClickListener {
    private final Context mContext;
    private final List<Preview> mlist;
    private final User user;
    private Homepage_Adapter.InnerItemOnclickListener mListener;
    private final int TYPE_ONE=0,TYPE_TWO=1,TYPE_COUNT=2;
    public Homepage_Adapter(Context mContext, List<Preview> mlist, User user)
    {
        this.mContext = mContext;
        this.mlist = mlist;
        this.user=user;
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
        return mlist.get(i).id;
    }
    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return TYPE_COUNT;
    }
    @Override
    public int getItemViewType(int position)
    {
        if(position==0)
        {
            return TYPE_ONE;
        }
        else
        {
            return TYPE_TWO;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Homepage_Adapter.ViewHolder viewHolder=null;
        Homepage_Adapter.ViewHolder1 viewHolder1=null;
        int type=getItemViewType(position);
        if(view==null)
        {
            switch (type)
            {
                case TYPE_ONE:
                    viewHolder1=new ViewHolder1();
                    view=LayoutInflater.from(mContext).inflate(R.layout.profile_head,parent,false);
                    viewHolder1.followers=(TextView) view.findViewById(R.id.followers);
                    viewHolder1.following=(TextView)view.findViewById(R.id.following);
                    viewHolder1.likes=(TextView)view.findViewById(R.id.likes);
                    viewHolder1.username=(TextView)view.findViewById(R.id.username);
                    viewHolder1.profile_head=(RoundedImageView)view.findViewById(R.id.profile_photo);
                    view.setTag(viewHolder1);
                    break;
                case TYPE_TWO:
                    viewHolder=new ViewHolder();
                    view= LayoutInflater.from(mContext).inflate(R.layout.recommend,parent,false);
                    viewHolder.title=(TextView)view.findViewById(R.id.R_title);
                    viewHolder.text=(TextView)view.findViewById(R.id.R_text);
                    viewHolder.writer=(TextView)view.findViewById(R.id.R_writer);
                    viewHolder.like=(TextView)view.findViewById(R.id.R_LikeNum);
                    viewHolder.Like_Button=(LikeButton)view.findViewById(R.id.like_button);
                    view.setTag(viewHolder);
                    break;
            }
        }
        else
        {
            switch (type){
                case TYPE_ONE:
                    viewHolder1=(ViewHolder1)view.getTag();
                    break;
                case TYPE_TWO:
                    viewHolder=(ViewHolder)view.getTag();
                    break;
            }
        }
        switch (type){
            case TYPE_ONE:
                viewHolder1.username.setText(user.name);
                viewHolder1.profile_head.setImageBitmap(user.photo);
                break;
            case TYPE_TWO:
                Preview preview=mlist.get(position);
                viewHolder.writer.setText(preview.getWriter());
                viewHolder.text.setText(preview.getText());
                viewHolder.like.setText(preview.getLike());
                viewHolder.title.setText(preview.getTitle());
                viewHolder.Like_Button.setTag(position);
                if(preview.is_Liked)
                {
                    viewHolder.Like_Button.setLiked(true);
                }
                else
                {
                    viewHolder.Like_Button.setLiked(false);
                }
                break;
        }
        return view;
    }
    interface InnerItemOnclickListener {
        void itemClick(View v, int position);
    }
    public void setOnInnerItemOnClickListener(Homepage_Adapter.InnerItemOnclickListener listener){
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
    public void Fresh(int position)
    {
        Preview tmp=mlist.get(position);
        boolean flag=tmp.is_Liked();
        if(flag)
        {
            flag=false;
        }
        else
        {
            flag=true;
        }
        mlist.set(position,new Preview(tmp.getTitle(),tmp.getText(),tmp.getWriter(),tmp.like,flag));
        notifyDataSetChanged();
    }
    private static class ViewHolder{
        TextView title;
        TextView text;
        TextView writer;
        TextView like;
        LikeButton Like_Button;

    }
    private static class ViewHolder1{
        RoundedImageView profile_head;
        TextView following;
        TextView followers;
        TextView likes;
        TextView username;
    }
}
