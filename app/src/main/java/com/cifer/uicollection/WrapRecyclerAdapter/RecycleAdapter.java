package com.cifer.uicollection.WrapRecyclerAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by xiaojinggong on 4/9/18.
 * 1.RecycleView添加点击事件
 *
 * 2.装饰设计模式为自定义recycleview添加头和底部
 *
 */

public class RecycleAdapter extends RecyclerView.Adapter {
    private int mposition;

    private ArrayList<View> mHeaderView;
    private ArrayList<View> mFooterView;
    private RecyclerView.Adapter mRealAdapter;

    public RecycleAdapter(RecyclerView.Adapter adapter){
        this.mRealAdapter = adapter;
        mHeaderView = new ArrayList<>();
        mFooterView = new ArrayList<>();
    }



    @Override
    public int getItemCount() {
        return mposition;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        int numHeaders = getHeadCount();

        if(position < numHeaders){
            return createFooterHeaderViewHolder(mHeaderView.get(position));
        }

        final int adjPostion = position - numHeaders;
        int adapterCount = 0;
        if(mRealAdapter != null){
            adapterCount = mRealAdapter.getItemCount();
            if(adjPostion < adapterCount){
                return mRealAdapter.onCreateViewHolder(parent,mRealAdapter.getItemViewType(adjPostion));
            }

        }

        return createFooterHeaderViewHolder(mFooterView.get(adjPostion-adapterCount));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //点击事件
        if(itemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onClickListener(position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemClickListener.onClickListener(position);
                    return false;
                }
            });
        }

        int numHeaders = getHeadCount();
        if(position < numHeaders){
                return;
        }

        final int adjPosition = position - numHeaders;
        if(mRealAdapter != null){
            int adaptercount = mRealAdapter.getItemCount();
            if(adjPosition < adaptercount){
                mRealAdapter.onBindViewHolder(holder,adjPosition);
            }

        }

    }
    //点击事件
    private onItemClickListener itemClickListener;

    public void setOnItemClickListener(onItemClickListener listener){
        this.itemClickListener = listener;
    }

    interface onItemClickListener{
        void onClickListener(int position);
        void onLongClickListener(int position);
    }

    private int getHeadCount(){
        return mHeaderView.size();
    }

    private RecyclerView.ViewHolder createFooterHeaderViewHolder(View view){
        return new RecyclerView.ViewHolder(view){

        };
    }

    //添加头部
    public void addHeaderView(View view){
        if(!mHeaderView.contains(view)){
            mHeaderView.add(view);
            notifyDataSetChanged();
        }
    }
    //移除头部
    public void removeHeaderView(View view){
        if(!mHeaderView.contains(view)){
            mHeaderView.remove(view);
            notifyDataSetChanged();
        }
    }
    //添加底部
    public void addFooterView(View view){
        if(!mFooterView.contains(view)){
            mFooterView.remove(view);
            notifyDataSetChanged();
        }
    }
    //移除底部
    public void removeFooterView(View view){
        if(!mHeaderView.contains(view)){
            mHeaderView.add(view);
            notifyDataSetChanged();
        }
    }

}
