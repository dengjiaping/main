
package cn.ngame.store.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;

/**
 * @author gp
 */
public class DiscoverTvIvAdapter extends RecyclerView.Adapter<DiscoverTvIvAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<Integer> list;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, String tag);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setmOnItemClickListener(OnItemClickLitener mOnItemClickListener) {
        this.mOnItemClickLitener = mOnItemClickListener;
    }

    public DiscoverTvIvAdapter(Context context, List<Integer> list) {
        super();
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    public void setList(List<Integer> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int vieype) {
        ViewHolder holder = new ViewHolder(mInflater.inflate(R.layout.item_discover_tviv, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ViewHolder vh = holder;
        holder.mTV.setText("游戏名字" + position);
        holder.mIV.setImageResource(list.get(position));
        if (mOnItemClickLitener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int position1 = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, position, (String) v.getTag()); // 2
                }
            });
        }
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
        private TextView mTV;
        private ImageView mIV;

        public ViewHolder(View itemView) {
            super(itemView);
            mTV = (TextView) itemView.findViewById(R.id.tviv_item_tv);
            mIV = (ImageView) itemView.findViewById(R.id.tviv_item_iv);
        }
    }
}














