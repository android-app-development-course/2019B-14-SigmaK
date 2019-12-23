package com.example.abc.sigmak;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.abc.sigmak.MyClass.*;
import com.like.LikeButton;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

import ezy.ui.view.RoundButton;

public class Answer_Adapter extends BaseAdapter implements View.OnClickListener{

    private final Context mContext;
    private final SparseBooleanArray mCollapsedStatus;
    List mlist;
    private Answer_Adapter.InnerItemOnclickListener mListener;
    private final int TYPE_ONE=0,TYPE_TWO=1,TYPE_THREE=2,TYPE_COUNT=3;
    List<Integer> Type;
    public Answer_Adapter(Context context,List list,List<Integer> type) {
        mContext  = context;
        mCollapsedStatus = new SparseBooleanArray();
        mlist=list;
        Type=type;
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }
    @Override
    public int getItemViewType(int position)
    {
        return Type.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        ViewHolder1 viewHolder1=null;
        ViewHolder2 viewHolder2=null;
        int type=getItemViewType(position);
        if (convertView == null) {
            switch (type)
            {
                case TYPE_ONE:
                    viewHolder=new ViewHolder();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.problem_description, parent, false);
                    viewHolder.read=(TextView)convertView.findViewById(R.id.read);
                    viewHolder.likes=(TextView)convertView.findViewById(R.id.likes);
                    viewHolder.answer_num=(TextView)convertView.findViewById(R.id.answersNum);
                    viewHolder.answer_que=(ImageButton) convertView.findViewById(R.id.answer_que);
                    viewHolder.expandableTextView=(ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
                    viewHolder.Like_Button=(LikeButton) convertView.findViewById(R.id.like);
                    viewHolder.Title=(TextView)convertView.findViewById(R.id.title111);
                    viewHolder.Type=(RoundButton) convertView.findViewById(R.id.Type);
                    convertView.setTag(viewHolder);
                    break;
                case TYPE_TWO:
                    viewHolder1=new ViewHolder1();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.answer, parent, false);
                    viewHolder1.title=(TextView)convertView.findViewById(R.id.Title);
                    viewHolder1.commentsNum=(TextView)convertView.findViewById(R.id.commentsNum);
                    viewHolder1.Against=(RoundButton) convertView.findViewById(R.id.Against);
                    viewHolder1.Agree=(RoundButton) convertView.findViewById(R.id.Agree);
                    viewHolder1.expandableTextView=(ExpandableTextView) convertView.findViewById(R.id.expand_text_view);
                    convertView.setTag(viewHolder1);
                    break;
                case TYPE_THREE:
                    viewHolder2=new ViewHolder2();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.comment, parent, false);
                    viewHolder2.comment=(TextView)convertView.findViewById(R.id.comments);
                    viewHolder2.userid=(TextView)convertView.findViewById(R.id.user_id);
                    viewHolder2.postdate=(TextView)convertView.findViewById(R.id.postdate);
                    convertView.setTag(viewHolder2);
                    break;
            }
        } else {
            switch (type)
            {
                case TYPE_ONE:
                    viewHolder=(ViewHolder)convertView.getTag();
                    break;
                case TYPE_TWO:
                    viewHolder1=(ViewHolder1)convertView.getTag();
                    break;
                case TYPE_THREE:
                    viewHolder2=(ViewHolder2)convertView.getTag();
                    break;
            }
        }
        switch (type)
        {
            case TYPE_ONE:
                mix_content content=(mix_content)mlist.get(position);
                viewHolder.Title.setText(content.m_post.Title);
                viewHolder.Type.setText(content.m_post.Type.toString());
                if(content.Like)
                {
                    viewHolder.Like_Button.setLiked(true);
                }
                else
                {
                    viewHolder.Like_Button.setLiked(false);
                }
                viewHolder.expandableTextView.setText(content.m_content.Text.toString(),mCollapsedStatus,position);
                viewHolder.answer_num.setText(content.m_post.Comments);
                viewHolder.likes.setText(R.string.likes+content.m_post.Likes);
                viewHolder.read.setText(R.string.read+content.m_post.Reads);
                //点击事件
                viewHolder.Like_Button.setTag(position);
                viewHolder.answer_que.setTag(position);
                break;
            case TYPE_TWO:
                mix_content content1=(mix_content)mlist.get(position);
                viewHolder1.commentsNum.setText(content1.m_post.Comments);
                viewHolder1.title.setText(content1.m_post.Title);
                viewHolder1.expandableTextView.setText(content1.m_content.Text.toString(),mCollapsedStatus,position);
                viewHolder1.Agree.setText(content1.m_post.Likes+R.string.agree);
                //点击事件
                viewHolder1.Agree.setTag(position);
                viewHolder1.Against.setTag(position);
                viewHolder1.commentsNum.setTag(position);
                if(content1.Like)
                {
                    viewHolder1.Agree.setPressed(true);
                }
                else
                {
                    viewHolder1.Agree.setPressed(false);
                }
                if(content1.Dislike)
                {
                    viewHolder1.Against.setPressed(true);
                }
                else
                {
                    viewHolder1.Against.setPressed(false);
                }
                break;
            case TYPE_THREE:
                Comment comment=(Comment)mlist.get(position);
                viewHolder2.postdate.setText(comment.PostDate.toString());
                viewHolder2.comment.setText(comment.Content);
                viewHolder2.userid.setText(comment.UserID);
                break;
        }

        return convertView;
    }
    public void add(int position,List list,int type)
    {
        mlist.add(position+1,list);
        List<Integer> tmp=new ArrayList<Integer>();
        for(int i=0;i<list.size();i++)
        {
            tmp.add(type);
        }
        Type.addAll(position+1,tmp);
    }
    interface InnerItemOnclickListener {
        void itemClick(View v, int position);
    }
    public void setOnInnerItemOnClickListener(Answer_Adapter.InnerItemOnclickListener listener){
        this.mListener=listener;
    }
    @Override
    public void onClick(View v) {
        int position;
        position = (int) v.getTag();
        if(mListener!=null)
        {
            mListener.itemClick(v,position);
        }
    }

    private static class ViewHolder{
        RoundButton Type;
        TextView likes;
        TextView read;
        LikeButton Like_Button;
        ExpandableTextView expandableTextView;
        ImageButton answer_que;
        TextView answer_num;
        TextView Title;
    }
    private static class ViewHolder1{
        RoundButton Agree;
        RoundButton Against;
        TextView title;
        TextView commentsNum;
        ExpandableTextView expandableTextView;
    }
    private static class ViewHolder2{
        TextView userid;
        TextView comment;
        TextView postdate;
    }
}
