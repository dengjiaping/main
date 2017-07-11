
package cn.ngame.store.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzt.hol.android.jkda.sdk.bean.classification.ClassifiHomeBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.ngame.store.R;

/**
 *  首页下方攻略列表
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
        this.type = type;
    }

    public void setList(List<ClassifiHomeBean.DataBean.GunFireListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size() < 6 ? 6 : list.size();
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
            convertView = View.inflate(parent.getContext(), R.layout.classifi_home_item, null);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
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














