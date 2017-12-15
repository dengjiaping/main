package cn.ngame.store.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.hub.CircleActivity;
import cn.ngame.store.bean.PostsInfo;

/**
 * Created by liguoliang
 * on 2017/11/29 0029.
 */

public class CircleAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PostsInfo.DataBean> mDatas;
    private CircleActivity mContext;

    public CircleAdapter(CircleActivity context, List<PostsInfo.DataBean> datats) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;

    }

    public void setData(List<PostsInfo.DataBean> data) {
        this.mDatas = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDatas == null) {
            return 0;
        } else {
            return mDatas.size();
        }
    }

    @Override
    public Object getItem(int i) {
        if (mDatas == null) {
            return null;
        } else {
            return mDatas.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new CircleAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.item_hub_circle_1, viewGroup, false);
            holder.mTxt = convertView.findViewById(R.id.singer_item_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PostsInfo.DataBean dataBean = mDatas.get(position);
        if (dataBean != null) {
            holder.mTxt.setText(dataBean.getPostCategoryName());
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView mTxt;
    }

}
