
package cn.ngame.store.adapter.discover;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;

/**
 * @author gp
 */
public class DiscoverClassifyTopAdapter extends RecyclerView.Adapter<DiscoverClassifyTopAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<String> list;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, String text);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setmOnItemClickListener(OnItemClickLitener mOnItemClickListener) {
        this.mOnItemClickLitener = mOnItemClickListener;
    }

    public DiscoverClassifyTopAdapter(Context context, List<String> list) {
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
        ViewHolder holder = new ViewHolder(mInflater.inflate(R.layout.item_discover_top_classify_singe_tv, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_content.setText(list.get(position));
        if (mOnItemClickLitener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int position1 = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, position, list.get(position)); // 2
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
        private TextView tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.singer_item_tv);
        }
    }
}














