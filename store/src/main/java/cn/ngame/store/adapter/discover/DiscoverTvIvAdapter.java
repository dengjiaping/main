
package cn.ngame.store.adapter.discover;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.util.ToastUtil;

/**
 * @author gp
 */
public class DiscoverTvIvAdapter extends RecyclerView.Adapter<DiscoverTvIvAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<String> list;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, String tag);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setmOnItemClickListener(OnItemClickLitener mOnItemClickListener) {
        this.mOnItemClickLitener = mOnItemClickListener;
    }

    public DiscoverTvIvAdapter(Context context, List<String> list) {
        super();
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    public void setList(List<String> list) {
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
        holder.mTV.setText("游戏名字" + position);
        holder.mIV.setImageURI(list.get(position));
        //为ItemView设置监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int position1 = holder.getLayoutPosition();
                //mOnItemClickLitener.onItemClick(holder.itemView, position, (String) v.getTag()); // 2
                ToastUtil.show(context, position + ":" + list.get(position).substring(0,5));
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
        private TextView mTV;
        private SimpleDraweeView mIV;

        public ViewHolder(View itemView) {
            super(itemView);
            mTV = (TextView) itemView.findViewById(R.id.tviv_item_tv);
            mIV = (SimpleDraweeView) itemView.findViewById(R.id.tviv_item_iv);
        }
    }
}














