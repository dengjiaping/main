package cn.ngame.store.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.hub.HubActivity;
import cn.ngame.store.core.utils.ImageUtil;
import cn.ngame.store.util.ToastUtil;

/**
 * Created by liguoliang
 * on 2017/11/29 0029.
 */

public class HubAdapter extends RecyclerView.Adapter<HubAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<String> mDatas;
    private TextView tv;
    private HubActivity mContext;
    private View itemView;
    private LinearLayout.LayoutParams params;
    public HubAdapter(HubActivity context, List<String> datats) {
        mContext=context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        params = new LinearLayout.LayoutParams(ImageUtil.getScreenWidth(context) / 2-
                8, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setData(ArrayList<String> data) {
        this.mDatas = data;
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
    public HubAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_hub_rv_item,
                viewGroup, false);
        HubAdapter.ViewHolder viewHolder = new HubAdapter.ViewHolder(view);
        viewHolder.mTxt = view.findViewById(R.id.singer_item_tv);
        viewHolder.mLayoutTags = view.findViewById(R.id.layout_tags2);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(HubAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.mTxt.setText(mDatas.get(position));
        for (int i = 0; i < 5; i++) {
            itemView = mInflater.inflate(R.layout.layout_hub_gl_item, null);
            itemView.setLayoutParams(params);
            //这是显示数据的控件
            tv = itemView.findViewById(R.id.hub_gl_item_tv);
            tv.setText("条目" + i);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.show(mContext,"条目:");
                }
            });
            viewHolder.mLayoutTags.addView(itemView);
        }
    }

}
