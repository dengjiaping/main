package cn.ngame.store.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.hub.CircleActivity;
import cn.ngame.store.bean.PostsInfo;
import cn.ngame.store.core.utils.ImageUtil;

/**
 * Created by liguoliang
 * on 2017/11/29 0029.
 */

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<PostsInfo.DataBean> mDatas;
    private CircleActivity mContext;
    private LinearLayout.LayoutParams params;

    public CircleAdapter(CircleActivity context, List<PostsInfo.DataBean> datats) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        params = new LinearLayout.LayoutParams(ImageUtil.getScreenWidth(context) / 2 -
                8, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setData(List<PostsInfo.DataBean> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        GridLayout mLayoutTags;
        TextView mTxt;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public CircleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_hub_rv_item,
                viewGroup, false);
        CircleAdapter.ViewHolder viewHolder = new CircleAdapter.ViewHolder(view);
        viewHolder.mTxt = view.findViewById(R.id.singer_item_tv);
        viewHolder.mLayoutTags = view.findViewById(R.id.layout_tags2);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(CircleAdapter.ViewHolder viewHolder, final int position) {
        PostsInfo.DataBean dataBean = mDatas.get(position);
        if (dataBean == null) {
            return;
        }
        viewHolder.mTxt.setText(dataBean.getPostCategoryName());
        viewHolder.mLayoutTags.removeAllViews();

    }

}
