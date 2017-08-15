/*
 * 	Flan.Zeng 2011-2016	http://git.oschina.net/signup?inviter=flan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.ngame.store.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.ngame.store.R;
import cn.ngame.store.bean.Category;
import cn.ngame.store.core.utils.CommonUtil;

/**
 * 首页 GridView控件适配器
 *
 * @author zeng
 * @since 2016-06-07
 */
public class GvHomeAdapter extends BaseAdapter {

    private static final String TAG = GvHomeAdapter.class.getSimpleName();

    List<Category> categories;
    private Context context;

    public GvHomeAdapter(Context context) {
        super();
        this.context = context;
    }

    public void setData(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int getCount() {

        if (categories != null) {
            return categories.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        if (categories != null) {
            return categories.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clean() {
        if (categories != null) {
            categories.clear();
        }
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        Category category = categories.get(position);

        TextView tv = new TextView(context);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.height = CommonUtil.dip2px(context,50);

        tv.setLayoutParams(params);
        tv.setBackgroundColor(context.getResources().getColor(R.color.mainColor));
        tv.setTextColor(context.getResources().getColor(R.color.white));
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(12);
        if(category != null){
            tv.setText(category.typeName);
            tv.setId((int)category.id);
        }else {
            tv.setText("游戏分类");
            tv.setId(position);
        }
        return tv;
    }

}














