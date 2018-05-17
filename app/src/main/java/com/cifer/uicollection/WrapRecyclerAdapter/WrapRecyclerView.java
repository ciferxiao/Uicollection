package com.cifer.uicollection.WrapRecyclerAdapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiaojinggong on 4/11/18.
 * description: 支持添加底部和头部的 RecyclerView
 *
 */

public class WrapRecyclerView extends RecyclerView {
    // 支持添加头部和底部的 RecyclerView.Adapter
    private RecycleAdapter mWrapAdapter;

    public WrapRecyclerView(Context context, RecycleAdapter mWrapAdapter) {
        super(context);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, RecycleAdapter mWrapAdapter) {
        super(context, attrs);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle, RecycleAdapter mWrapAdapter) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new RecycleAdapter(adapter);
        super.setAdapter(adapter);
        
    }

    //添加头部
    public void addHeaderView(View view){
        if(mWrapAdapter != null ){
            mWrapAdapter.addHeaderView(view);
        }
    }
    //移除头部
    public void removeHeaderView(View view){
        if(mWrapAdapter != null ){
            mWrapAdapter.removeHeaderView(view);
        }
    }
    //添加底部
    public void addFooterView(View view){
        if(mWrapAdapter != null ){
            mWrapAdapter.addFooterView(view);
        }
    }
    //移除底部
    public void removeFooterView(View view){
        if(mWrapAdapter != null ){
            mWrapAdapter.removeFooterView(view);
        }
    }
}
