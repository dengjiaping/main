
package cn.ngame.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.gamehub.GameHubMainBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.util.DateUtil;
import cn.ngame.store.util.StringUtil;

/**
 * game圈
 *
 * @author gp
 */
public class MainHomeRaiderAdapter extends BaseAdapter {

    private Context context;
    private List<GameHubMainBean.DataBean> list;
    private String[] imgs;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public MainHomeRaiderAdapter(Context context, List<GameHubMainBean.DataBean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void setList(List<GameHubMainBean.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        MainHomeRaiderAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new MainHomeRaiderAdapter.ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.home_raider_item, null);
            holder.ll_show = (LinearLayout) convertView.findViewById(R.id.ll_show);
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (MainHomeRaiderAdapter.ViewHolder) convertView.getTag();
        }

        String url = "";
        if (!StringUtil.isEmpty(list.get(position).getPostImage())) {
            url = list.get(position).getPostImage().contains(",") ? list.get(position).getPostImage().split(",")[0] : list.get(position).getPostImage();
        } else {
            url = "";
        }
        try {
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.ic_def_logo_720_288)
                    .error(R.drawable.ic_def_logo_720_288)
                    .resizeDimen(R.dimen.list_detail_image_size_100, R.dimen.list_detail_image_size_80)
                    .centerInside()
                    .tag(context)
                    .into(holder.iv_img);
        } catch (Exception e) {

        }
        holder.tv_type.setText(StringUtil.getGameHubMsgType(list.get(position).getPostTagId()));
        holder.tv_date.setText(DateUtil.getStrTime_ymd(list.get(position).getCreateTime()));
        holder.tv_content.setText(list.get(position).getPostTitle());

        return convertView;
    }

    class ViewHolder {
        private LinearLayout ll_show;
        private ImageView iv_img;
        private TextView tv_type, tv_date, tv_content;
    }
}














