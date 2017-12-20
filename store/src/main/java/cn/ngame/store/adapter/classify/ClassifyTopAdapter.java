
package cn.ngame.store.adapter.classify;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.main.DiscoverListBean;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.core.utils.KeyConstant;
import cn.ngame.store.game.view.SeeMoreActivity;

/**
 * @author gp
 */
public class ClassifyTopAdapter extends RecyclerView.Adapter<ClassifyTopAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private Context context;
    private List<DiscoverListBean.DataBean.GameCategroyListBean> list;

    public interface OnItemClickLitener {
        void onItemClick(View view, int position, int text);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setmOnItemClickListener(OnItemClickLitener mOnItemClickListener) {
        this.mOnItemClickLitener = mOnItemClickListener;
    }

    public ClassifyTopAdapter(Context context, List<DiscoverListBean.DataBean.GameCategroyListBean> list) {
        super();
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    public void setList(List<DiscoverListBean.DataBean.GameCategroyListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int vieype) {
        ViewHolder holder = new ViewHolder(mInflater.inflate(R.layout.item_classify_top_classify_singe_tv, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DiscoverListBean.DataBean.GameCategroyListBean gameCategroyListBean = list.get(position);
        final String cName = gameCategroyListBean.getCName();
        holder.tv_content.setText(cName);
        //为ItemView设置监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent classifyIntent = new Intent(context, SeeMoreActivity.class);
                classifyIntent.putExtra(KeyConstant.category_Id, gameCategroyListBean.getId()+"");//原生手柄 id 367
                classifyIntent.putExtra(KeyConstant.TITLE, cName);
                context.startActivity(classifyIntent);
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
        private TextView tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.singer_item_tv);
        }
    }
}














