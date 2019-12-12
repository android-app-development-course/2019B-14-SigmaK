package com.example.abc.sigmak;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.like.IconType;
import com.like.LikeButton;

import java.util.List;

/**
 * Created by abc on 2019/11/22.
 */

public class My_Adapter extends RecyclerView.Adapter<My_Adapter.ViewHolder> implements OnClickListener {
    private List<Preview> m_list;
    public class ViewHolder extends RecyclerView.ViewHolder{
        View myview;
        TextView title;
        TextView text;
        TextView writer;
        TextView like;
        LikeButton Like_Button;
    public ViewHolder(View itemView) {
        super(itemView);
        myview=itemView;
        title=(TextView)itemView.findViewById(R.id.R_title);
        text=(TextView)itemView.findViewById(R.id.R_text);
        writer=(TextView)itemView.findViewById(R.id.R_writer);
        like=(TextView)itemView.findViewById(R.id.R_LikeNum);
        Like_Button=(LikeButton)itemView.findViewById(R.id.like_button);
        Like_Button.setOnClickListener(My_Adapter.this);
        itemView.setOnClickListener(My_Adapter.this);
    }
}
    public My_Adapter(List<Preview>list)
    {
        this.m_list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommend, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Preview preview=m_list.get(position);
        holder.Like_Button.setLiked(false);
        holder.writer.setText(preview.getWriter());
        holder.text.setText(preview.getText());
        holder.like.setText(preview.getLike());
        holder.title.setText(preview.getTitle());
        holder.Like_Button.setTag(position);
        holder.itemView.setTag(position);
        final Preview p=this.m_list.get(position);
        if(p.is_Liked==true)
        {
            holder.Like_Button.setLiked(true);
        }
        else
        {
            holder.Like_Button.setLiked(false);
        }
    }

    @Override
    public int getItemCount() {
        return m_list.size();
    }
    public void add(List<Preview>list)
    {
        int position=m_list.size();
        m_list.addAll(position,list);
        notifyItemInserted(position);
    }
    public void refresh(List<Preview>list)
    {
        m_list.removeAll(m_list);
        m_list.addAll(list);
        notifyDataSetChanged();
    }
    public void Fresh(int position)
    {
        Preview tmp=m_list.get(position);
        boolean flag=tmp.is_Liked();
        if(flag)
        {
            flag=false;
        }
        else
        {
            flag=true;
        }
        m_list.set(position,new Preview(tmp.getTitle(),tmp.getText(),tmp.getWriter(),tmp.like,flag));
        notifyDataSetChanged();
    }

    public enum ViewName
    {
        ITEM,
        INNER
    }
    public interface OnItemClickListener  {
        void onItemClick(View v, ViewName viewName, int position);
    }

    private OnItemClickListener mOnItemClickListener=null;//声明自定义的接口

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(My_Adapter.OnItemClickListener  listener) {
        this.mOnItemClickListener  = listener;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();      //getTag()获取数据
        if (mOnItemClickListener != null) {
            switch (v.getId()){
                case R.id.like_button:
                    mOnItemClickListener.onItemClick(v, ViewName.INNER, position);
                    break;
                default:
                    mOnItemClickListener.onItemClick(v, ViewName.ITEM, position);
                    break;
            }
        }
    }

}
