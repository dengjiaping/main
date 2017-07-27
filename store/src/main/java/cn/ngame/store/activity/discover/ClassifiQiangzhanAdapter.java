
package cn.ngame.store.activity.discover;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.classification.ClassifiHomeBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ngame.store.R;

import static cn.ngame.store.R.id.tv_content;

/**
 * 首页下方攻略列表
 *
 * @author gp
 */
public class ClassifiQiangzhanAdapter extends BaseAdapter {

    private Context context;
    private List<ClassifiHomeBean.DataBean.GunFireListBean> list;
    String type;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public ClassifiQiangzhanAdapter(Context context, List<ClassifiHomeBean.DataBean.GunFireListBean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    public void setList(List<ClassifiHomeBean.DataBean.GunFireListBean> list) {
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
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.classify_gridview_item, null);
            holder.tv_content = (TextView) convertView.findViewById(tv_content);
            holder.tv_content.setBackgroundResource(R.drawable.shape_blue_corner4dp_4ac7fc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position > (list.size() - 1)) {
            holder.tv_content.setText("");
        } else {
            holder.tv_content.setText(list.get(position).getTypeName());
        }
        return convertView;
    }

    class ViewHolder {
        private TextView tv_content;
    }
}














