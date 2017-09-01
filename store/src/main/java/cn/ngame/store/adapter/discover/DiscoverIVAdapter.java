
package cn.ngame.store.adapter.discover;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.activity.main.TopicsDetailActivity;
import cn.ngame.store.bean.RecommendTopicsItemInfo;
import cn.ngame.store.core.utils.KeyConstant;

/**
 * @author liguoliang
 */
public class DiscoverIvAdapter extends RecyclerView.Adapter<DiscoverIvAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<RecommendTopicsItemInfo> list;
    private Intent singeTopicsDetailIntent = new Intent();
    ;


    public DiscoverIvAdapter(Context context, List<RecommendTopicsItemInfo> list) {
        super();
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
        singeTopicsDetailIntent.setClass(context, TopicsDetailActivity.class);
    }

    public void setList(List<RecommendTopicsItemInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int vieype) {
        ViewHolder holder = new ViewHolder(mInflater.inflate(R.layout.item_discover_iv, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mIV.setImageURI(list.get(position).getSelectImage());
        //为ItemView设置监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendTopicsItemInfo topicsInfo = list.get(position);
                singeTopicsDetailIntent.putExtra(KeyConstant.category_Id, topicsInfo.getId());
                singeTopicsDetailIntent.putExtra(KeyConstant.TITLE, topicsInfo.getTitle());
                singeTopicsDetailIntent.putExtra(KeyConstant.DESC, topicsInfo.getSelectDesc());
                singeTopicsDetailIntent.putExtra(KeyConstant.URL, topicsInfo.getSelectImage());
                context.startActivity(singeTopicsDetailIntent);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView mIV;

        public ViewHolder(View itemView) {
            super(itemView);
            mIV = (SimpleDraweeView) itemView.findViewById(R.id.iv_item_iv);
        }
    }
}














